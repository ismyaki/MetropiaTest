package project.main

import android.Manifest
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.google.maps.android.ui.IconGenerator
import com.wang.metropiatest.R
import com.wang.metropiatest.databinding.ActivityMainBinding
import project.main.api.model.TestModel
import project.main.database.BikeStationEntity
import project.main.repository.TravelStationRepository
import project.main.tools.toJsonString
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity(), OnMapReadyCallback, EasyPermissions.PermissionCallbacks {
    private val TAG = javaClass::class.java.simpleName

    private val hightPixel by lazy { context.resources.displayMetrics.heightPixels }
    private val widthPixel by lazy { context.resources.displayMetrics.widthPixels }

    private val context by lazy { this }

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mMap: GoogleMap
    private val bottomView by lazy { BottomSheetBehaviorView(mBinding.flSheet, mBinding.icBottom) }

    private val stationList = mutableListOf<BikeStationEntity>()
    private val dataRepository by lazy { TravelStationRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initMap()
        initEvent()
    }

    private fun initEvent(){
        mBinding.ivMyLocation.setOnClickListener {
            zoomToMyLocation()
        }
        mBinding.ivZoomToBounds.setOnClickListener {
            val model = dataRepository.executeData().model
            val pointList = mutableListOf<LatLng>()
            model.steps.forEach {
                pointList.addAll(PolyUtil.decode(it.polyline))
            }
            zoomToBounds(pointList)
        }
    }

    private fun initMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = SupportMapFragment.newInstance()
        mapFragment.getMapAsync(this)
        supportFragmentManager.beginTransaction().replace(R.id.map, mapFragment).commit()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyle(R.raw.map_style)
        mMap.uiSettings.isMyLocationButtonEnabled = false
//        mMap.isTrafficEnabled = true // 顯示路況

        if (checkPermission() == false) {
            requestPermissions()
        } else {
            mMap.isMyLocationEnabled = true
//            zoomToMyLocation(false)
        }

        Log.e(TAG, "onMapReady: ${dataRepository.executeData().model.steps.toJsonString()}", )
        val model = dataRepository.executeData().model
        addTransportMarker(model.steps)
        model.steps.forEach {
            drawPolyLine(it.polyline, context.getColor(R.color.colorPrimary))
        }
        val pointList = mutableListOf<LatLng>()
        model.steps.forEach {
            pointList.addAll(PolyUtil.decode(it.polyline))
        }
        zoomToBounds(pointList)
        bottomView.setValue(model)
        bottomView.show()
    }

    private fun addTransportMarker(list: List<TestModel.Step>){
        val lastBitmap = getBitmapForRid(R.drawable.ic_twotone_location_on_24, R.color.colorPrimary)?.let { BitmapDescriptorFactory.fromBitmap(it) }
        val walkBitmap = getBitmapForRid(R.drawable.ic_baseline_directions_walk_24, R.color.colorSecondary)?.let { BitmapDescriptorFactory.fromBitmap(it) }
        val busBitmap = getBitmapForRid(R.drawable.ic_baseline_directions_bus_24, R.color.colorSecondary)?.let { BitmapDescriptorFactory.fromBitmap(it) }
        val tramBitmap = getBitmapForRid(R.drawable.ic_baseline_tram_24, R.color.colorSecondary)?.let { BitmapDescriptorFactory.fromBitmap(it) }
        val markerList = list.map {
            val color = when (it.mode) {
                "walk" -> walkBitmap
                "bus" -> busBitmap
                "tram" -> tramBitmap
                else -> walkBitmap
            }
            var lat = it.stepsDetail.first().startLocation.lat
            var lng = it.stepsDetail.first().startLocation.lng
            if (it.mode == "tram") {
                lat = it.stepsDetail.first().departureStop.location.lat
                lng = it.stepsDetail.first().departureStop.location.lng
            }
            MarkerOptions()
                .position(LatLng(lat, lng))
                .icon(color)
        }.toMutableList()
        var lat = list.last().stepsDetail.first().startLocation.lat
        var lng = list.last().stepsDetail.first().startLocation.lng
        if (list.last().mode == "tram") {
            lat = list.last().stepsDetail.first().departureStop.location.lat
            lng = list.last().stepsDetail.first().departureStop.location.lng
        }
        markerList.add(
            MarkerOptions()
                .position(LatLng(lat, lng))
                .icon(lastBitmap)
        )
        addMarkerOptions(markerList)
    }

    private fun setMapStyle(rawId: Int){
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, rawId))
            if (!success) {
                Log.e("TAG", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("TAG", "Can't find style. Error: ", e)
        }
    }

    private fun getMyLocation(): Location?{
        if (checkPermission() == false) {
            return null
        }
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermission(): Boolean {
        val perms = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
//                Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return EasyPermissions.hasPermissions(this, *perms)
    }

    private var PERMISSIONS_REQUEST_CODE = 456
    private fun requestPermissions() {
        val perms = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
//                Manifest.permission.ACCESS_COARSE_LOCATION
        )
        EasyPermissions.requestPermissions(
            this,
            "請給我權限",
            PERMISSIONS_REQUEST_CODE,
            *perms
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        //權限被拒
        if (requestCode == PERMISSIONS_REQUEST_CODE) {

        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        //權限允許
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (::mMap.isInitialized && checkPermission()) {
                mMap.isMyLocationEnabled = true
                zoomToMyLocation()
            }
        }
    }

    private fun zoomToMyLocation(needAnimation: Boolean = true){
        if (::mMap.isInitialized == false) {
            return
        }
        val myLocation = getMyLocation() ?: return
        zoomToPoint(LatLng(myLocation.latitude, myLocation.longitude), needAnimation)
    }

    private fun zoomToPoint(latLng: LatLng, needAnimation: Boolean = true, zoom: Float = 16f){
        val position = CameraPosition.builder().target(latLng).zoom(zoom).build()
        if (needAnimation) {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 500, null)
        } else {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position))
        }
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng), 1000, null)
    }

    private fun zoomToBounds(latLng: LatLng, latLng2: LatLng){
        val bounds = LatLngBounds.builder().include(latLng).include(latLng2).build()
        val padding = (resources.displayMetrics.widthPixels * 0.05).toInt()
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding), 500, null)
    }

    private fun zoomToBounds(polyline: String){
        zoomToBounds(PolyUtil.decode(polyline))
    }

    private fun zoomToBounds(list: MutableList<LatLng>){
        val builder = LatLngBounds.builder()
        list.forEach {
            builder.include(it)
        }
        val bounds = builder.build()
        val padding = (resources.displayMetrics.widthPixels * 0.35).toInt()
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding), 500, null)
    }

    private fun addMarker(latLng: LatLng){
        mMap.addMarker(MarkerOptions().position(latLng).title("Marker in Sydney"))
    }

    private fun addMarker(list: MutableList<LatLng>){
        list.forEach { addMarker(it) }
    }

    private fun addMarkerOptions(list: MutableList<MarkerOptions>){
        list.forEach { mMap.addMarker(it) }
    }

    private fun addStartEndMarker(start: TestModel.Location, end: TestModel.Location){
        val startMarker = MarkerOptions()
            .position(LatLng(start.lat, start.lng))
            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapForRid(R.drawable.ic_twotone_album_24, R.color.blue)))
        val endMarker = MarkerOptions()
            .position(LatLng(end.lat, end.lng))
            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapForRid(R.drawable.ic_twotone_location_on_24, R.color.red)))
        addMarkerOptions(mutableListOf(startMarker, endMarker))
    }

    private fun addMarkerList(list: MutableList<TestModel.Location>){
        val rid = R.drawable.ic_baseline_album_24
//        val rid = R.drawable.ic_bike
        val markerList = list.map {
            val color = BitmapDescriptorFactory.fromBitmap(getBitmapForRid(rid, R.color.red))
            MarkerOptions()
                .position(LatLng(it.lat, it.lng))
//                .title(it.name)
                .icon(color)
        }.toMutableList()
        addMarkerOptions(markerList)
    }

    private fun getBitmapForRid(rid: Int, @ColorRes tintColor: Int? = null): Bitmap? {
        // retrieve the actual drawable
        val drawable = ContextCompat.getDrawable(context, rid) ?: return null
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)

        // add the tint if it exists
        tintColor?.let {
            DrawableCompat.setTint(drawable, ContextCompat.getColor(context, it))
        }

        // draw it onto the bitmap
        val canvas = Canvas(bitmap)
        drawable.draw(canvas)
        return bitmap
    }

    private fun drawPolyLine(encodeedPath: String, color: Int) {
        val line = mMap.addPolyline(PolylineOptions().addAll(PolyUtil.decode(encodeedPath)))
        line.width = 10f
        line.color = color
        line.isGeodesic = false
    }
}
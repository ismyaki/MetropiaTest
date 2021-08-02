package project.main.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.internal.http2.StreamResetException
import project.main.api.ApiConnect
import project.main.api.ApiServer
import project.main.database.BikeStationEntity
import project.main.database.database
import project.main.tools.DateTool
import project.main.tools.ErrorType
import project.main.tools.ResponseError
import project.main.tools.getError
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.*
import kotlin.math.absoluteValue

class BikeStationRepository(private val context: Context){
    private val TAG = javaClass::class.java.simpleName

    enum class Type {
        API,
        Database
    }

    interface Listener {
        fun onStart()
        fun onResult(type: Type, code: Int, dataList: List<BikeStationEntity>)
        fun onFail(type: Type, code: Int, responseError: ResponseError)
    }

    class Result(val type: Type, val code: Int, val dataList: List<BikeStationEntity>, val responseError: ResponseError?)

    fun getCache(): List<BikeStationEntity> {
        return context.database.bikeStationDao.allData
    }

    private var lastSyncApiTime = 0L
    /**
     * 異步取資料
     * */
    fun enqueueData(listener: Listener) {
        if ((Date().time - lastSyncApiTime).absoluteValue < DateTool.oneMin) {
            listener.onStart()
            val dao = context.database.bikeStationDao
            listener.onResult(Type.Database, 0, dao.allData)
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                GlobalScope.launch(Dispatchers.Main) { listener.onStart() }
                getTaipeiApiData()?.let {
                    GlobalScope.launch(Dispatchers.Main) { listener.onFail(Type.API, it.code, it) }
                    return@launch
                }
                getNewTaipeiApiData()?.let {
                    GlobalScope.launch(Dispatchers.Main) { listener.onFail(Type.API, it.code, it) }
                    return@launch
                }
                getTaoyuanApiData()?.let {
                    GlobalScope.launch(Dispatchers.Main) { listener.onFail(Type.API, it.code, it) }
                    return@launch
                }
                getTaichungApiData()?.let {
                    GlobalScope.launch(Dispatchers.Main) { listener.onFail(Type.API, it.code, it) }
                    return@launch
                }
                getTainanApiData()?.let {
                    GlobalScope.launch(Dispatchers.Main) { listener.onFail(Type.API, it.code, it) }
                    return@launch
                }
                val dao = context.database.bikeStationDao
                GlobalScope.launch(Dispatchers.Main) { listener.onResult(Type.API, 200, dao.allData) }
            }
        }
    }

    /**
     * 同步取資料
     * */
    fun executeData(): Result {
        val dao = context.database.bikeStationDao
        return if ((Date().time - lastSyncApiTime).absoluteValue < DateTool.oneMin) {
            Result(Type.Database, 0, dao.allData, null)
        } else {
            getTaipeiApiData()?.let {
                return Result(Type.API, it.code, dao.allData, it)
            }
            getNewTaipeiApiData()?.let {
                return Result(Type.API, it.code, dao.allData, it)
            }
            getTaoyuanApiData()?.let {
                return Result(Type.API, it.code, dao.allData, it)
            }
            getTaichungApiData()?.let {
                return Result(Type.API, it.code, dao.allData, it)
            }
            getTainanApiData()?.let {
                return Result(Type.API, it.code, dao.allData, it)
            }
            Result(Type.API, 200, dao.allData, null)
        }
    }

    private fun getTaipeiApiData(): ResponseError?{
        val cell = ApiConnect.getService(ApiServer.TAIPEI).getTaipeiBikeStation()
        try {
            val response = cell.execute()
            if (response.isSuccessful) {
                val system = "Youbike"
                val city = "Tiapei"
                val retVal = response.body()?.retVal
                val entityList = retVal?.map {
                    val entity = BikeStationEntity()
                    entity.id = city + "_" + it.value.sno
                    entity.name = it.value.sna
                    entity.e_name = it.value.snaen
                    entity.address = it.value.ar
                    entity.e_address = it.value.aren
                    entity.area = it.value.sarea
                    entity.e_area = it.value.sareaen
                    entity.open = if (it.value.act == "1") 1 else 0
                    entity.total = it.value.tot.toIntOrNull() ?: 0
                    entity.available = it.value.sbi.toIntOrNull() ?: 0
                    entity.empty = it.value.bemp.toIntOrNull() ?: 0
                    entity.lat = it.value.lat.toDoubleOrNull() ?: 0.0
                    entity.lng = it.value.lng.toDoubleOrNull() ?: 0.0
                    entity.city = city
                    entity.system = system
                    entity.update_time = Date().time
                    entity
                } ?: mutableListOf()
                val dao = context.database.bikeStationDao
                dao.insert(entityList)
            }
            return response.getError()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            return ResponseError(0, null, ErrorType.TIME_OUT, e)
        } catch (e: StreamResetException) {
            e.printStackTrace()
            return ResponseError(0, null, ErrorType.STREAM, e)
        } catch (e: ConnectException) {
            e.printStackTrace()
            return ResponseError(0, null, ErrorType.CONNECT, e)
        }
        return null
    }

    private fun getNewTaipeiApiData(): ResponseError?{
        val cell = ApiConnect.getService(ApiServer.NEW_TAIPEI).getNewTaipeiBikeStation()
        try {
            val response = cell.execute()
            if (response.isSuccessful) {
                val system = "Youbike"
                val city = "NewTiapei"
                val retVal = response.body()
                val entityList = retVal?.map {
                    val entity = BikeStationEntity()
                    entity.id = city + "_" + it.sno
                    entity.name = it.sna
                    entity.e_name = it.snaen
                    entity.address = it.ar
                    entity.e_address = it.aren
                    entity.area = it.sarea
                    entity.e_area = it.sareaen
                    entity.open = if (it.act == "1") 1 else 0
                    entity.total = it.tot.toIntOrNull() ?: 0
                    entity.available = it.sbi.toIntOrNull() ?: 0
                    entity.empty = it.bemp.toIntOrNull() ?: 0
                    entity.lat = it.lat.toDoubleOrNull() ?: 0.0
                    entity.lng = it.lng.toDoubleOrNull() ?: 0.0
                    entity.city = city
                    entity.system = system
                    entity.update_time = Date().time
                    entity
                } ?: mutableListOf()
                val dao = context.database.bikeStationDao
                dao.insert(entityList)
            }
            return response.getError()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            return ResponseError(0, null, ErrorType.TIME_OUT, e)
        } catch (e: StreamResetException) {
            e.printStackTrace()
            return ResponseError(0, null, ErrorType.STREAM, e)
        } catch (e: ConnectException) {
            e.printStackTrace()
            return ResponseError(0, null, ErrorType.CONNECT, e)
        }
        return null
    }

    private fun getTaoyuanApiData(): ResponseError?{
        val cell = ApiConnect.getService(ApiServer.TAOYUAN).getTaoyuanBikeStation()
        try {
            val response = cell.execute()
            if (response.isSuccessful) {
                val system = "Youbike"
                val city = "Taoyuan"
                val retVal = response.body()?.retVal
                val entityList = retVal?.map {
                    val entity = BikeStationEntity()
                    entity.id = city + "_" + it.value.sno
                    entity.name = it.value.sna
                    entity.e_name = it.value.snaen
                    entity.address = it.value.ar
                    entity.e_address = it.value.aren
                    entity.area = it.value.sarea
                    entity.e_area = it.value.sareaen
                    entity.open = if (it.value.act == "1") 1 else 0
                    entity.total = it.value.tot.toIntOrNull() ?: 0
                    entity.available = it.value.sbi.toIntOrNull() ?: 0
                    entity.empty = it.value.bemp.toIntOrNull() ?: 0
                    entity.lat = it.value.lat.toDoubleOrNull() ?: 0.0
                    entity.lng = it.value.lng.toDoubleOrNull() ?: 0.0
                    entity.city = city
                    entity.system = system
                    entity.update_time = Date().time
                    entity
                } ?: mutableListOf()
                val dao = context.database.bikeStationDao
                dao.insert(entityList)
            }
            return response.getError()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            return ResponseError(0, null, ErrorType.TIME_OUT, e)
        } catch (e: StreamResetException) {
            e.printStackTrace()
            return ResponseError(0, null, ErrorType.STREAM, e)
        } catch (e: ConnectException) {
            e.printStackTrace()
            return ResponseError(0, null, ErrorType.CONNECT, e)
        }
        return null
    }

    private fun getTaichungApiData(): ResponseError?{
        val cell = ApiConnect.getService(ApiServer.TAICHUNG).getTaichungBikeStation()
        try {
            val response = cell.execute()
            if (response.isSuccessful) {
                val system = "iBike"
                val city = "Taichung"
                val retVal = response.body()
                val entityList = retVal?.map {
                    val entity = BikeStationEntity()
                    entity.id = city + "_" + it.id
                    entity.name = it.position
                    entity.e_name = it.eName
                    entity.address = it.cAddress
                    entity.e_address = it.eAddress
                    entity.area = it.cArea
                    entity.e_area = it.eArea
                    entity.open = 1
                    entity.total = (it.availableCNT.toIntOrNull() ?: 0) + (it.empCNT.toIntOrNull() ?: 0)
                    entity.available = it.availableCNT.toIntOrNull() ?: 0
                    entity.empty = it.empCNT.toIntOrNull() ?: 0
                    entity.lat = it.y.toDoubleOrNull() ?: 0.0
                    entity.lng = it.x.toDoubleOrNull() ?: 0.0
                    entity.city = city
                    entity.system = system
                    entity.update_time = Date().time
                    entity
                } ?: mutableListOf()
                val dao = context.database.bikeStationDao
                dao.insert(entityList)
            }
            return response.getError()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            return ResponseError(0, null, ErrorType.TIME_OUT, e)
        } catch (e: StreamResetException) {
            e.printStackTrace()
            return ResponseError(0, null, ErrorType.STREAM, e)
        } catch (e: ConnectException) {
            e.printStackTrace()
            return ResponseError(0, null, ErrorType.STREAM, e)
        }
        return null
    }

    private fun getTainanApiData(): ResponseError?{
        val cell = ApiConnect.getService(ApiServer.TAINAN).getTainanBikeStation()
        try {
            val response = cell.execute()
            if (response.isSuccessful) {
                val system = "TBike"
                val city = "Tainan"
                val retVal = response.body()
                val entityList = retVal?.map {
                    val entity = BikeStationEntity()
                    entity.id = city + "_" + it.id
                    entity.name = it.stationName
                    entity.e_name = it.englishStationName
                    entity.address = it.address
                    entity.e_address = it.englishAddress
                    entity.area = it.district
                    entity.e_area = it.district
                    entity.open = 1
                    entity.total = it.capacity.toIntOrNull() ?: 0
                    entity.available = it.avaliableBikeCount.toIntOrNull() ?: 0
                    entity.empty = it.avaliableSpaceCount.toIntOrNull() ?: 0
                    entity.lat = it.latitude.toDoubleOrNull() ?: 0.0
                    entity.lng = it.longitude.toDoubleOrNull() ?: 0.0
                    entity.city = city
                    entity.system = system
                    entity.update_time = Date().time
                    entity
                } ?: mutableListOf()
                val dao = context.database.bikeStationDao
                dao.insert(entityList)
            }
            return response.getError()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            return ResponseError(0, null, ErrorType.TIME_OUT, e)
        } catch (e: StreamResetException) {
            e.printStackTrace()
            return ResponseError(0, null, ErrorType.STREAM, e)
        } catch (e: ConnectException) {
            e.printStackTrace()
            return ResponseError(0, null, ErrorType.CONNECT, e)
        }
        return null
    }
}
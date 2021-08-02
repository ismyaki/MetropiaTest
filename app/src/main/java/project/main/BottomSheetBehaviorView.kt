package project.main

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.wang.metropiatest.R
import com.wang.metropiatest.databinding.*
import project.main.api.model.TestModel
import project.main.tools.BackgroundDrawable
import project.main.tools.toDate
import project.main.tools.toString
import tools.setMarginByDpUnit

class BottomSheetBehaviorView(private val frameLayout: FrameLayout, private val mBinding: BottomSheetBinding) {
    private val TAG = "BottomSheetBehaviorView"

    private val context by lazy { frameLayout.context }
    private val hightPixel by lazy { context.resources.displayMetrics.heightPixels }
    private val widthPixel by lazy { context.resources.displayMetrics.widthPixels }

    private var model: TestModel = TestModel()

    private val behavior by lazy {
        BottomSheetBehavior.from(frameLayout).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    init {
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                val state = when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING ->  "STATE_DRAGGING"
                    BottomSheetBehavior.STATE_COLLAPSED ->  "STATE_COLLAPSED"
                    BottomSheetBehavior.STATE_EXPANDED ->  "STATE_EXPANDED"
                    BottomSheetBehavior.STATE_HALF_EXPANDED ->  "STATE_HALF_EXPANDED"
                    BottomSheetBehavior.STATE_HIDDEN ->  "STATE_HIDDEN"
                    BottomSheetBehavior.STATE_SETTLING ->  "STATE_SETTLING"
                    else -> ""
                }
                Log.e(TAG, "onStateChanged: nowState is $state")
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
        initView()
    }

    private fun initView(){
        mBinding.vDivider.background = BackgroundDrawable.getRectangleBg(
            context,
            2, 2, 2, 2,
            R.color.divider,
            0,
            0
        )
        mBinding.bottomSheet.background = BackgroundDrawable.getRectangleBg(
            context,
            24, 24, 0, 0,
            R.color.white,
            0,
            0
        )
        mBinding.vScrollTouch.setOnTouchListener { v, event ->
            mBinding.rvTransport.requestDisallowInterceptTouchEvent(true)
            false
        }
    }

    fun show(){
        frameLayout.visibility = View.VISIBLE
        behavior.isHideable = false
        mBinding.constraintLayout.post {
            behavior.peekHeight = mBinding.constraintLayout.measuredHeight
        }
        mBinding.bottomSheet.minHeight = (hightPixel * 0.85).toInt()
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun setValue(model: TestModel){
        this.model = model
        mBinding.tvEstimatedTime.text = context.getString(R.string.bottom_sheet_estimated_time).format(model.estimatedTime)
        mBinding.tvStartTime.text = model.startedOn.toDate().toString("hh:mm a")
        mBinding.tvEndTime.text = model.endedOn.toDate().toString("hh:mm a")
        mBinding.includeRvTransportHeader.tvOriginName.text = model.steps.first().originName
        mBinding.includeRvTransportHeader.tvStartTime.text = model.startedOn.toDate().toString("hh:mm a")
        mBinding.includeRvTransportFooter.tvDestinationName.text = model.steps.last().destinationName
        mBinding.includeRvTransportFooter.tvEndTime.text = model.endedOn.toDate().toString("hh:mm a")
        mBinding.rvTransportShort.adapter = shortAdapter
        shortAdapter.addItem(model.steps)
        mBinding.rvTransport.adapter = transportAdapter
        transportAdapter.addItem(model.steps)
    }

    private val shortAdapter by lazy { ShortAdapter(context) }
    private inner class ShortAdapter(val context: Context): BaseRecyclerViewDataBindingAdapter<TestModel.Step>(context, listOf(R.layout.adapter_bottom_sheet_transport_short)) {

        override fun initViewHolder(viewHolder: ViewHolder) {
            val binding = viewHolder.binding as AdapterBottomSheetTransportShortBinding
            binding.tvShortName.setPadding(2, 2, 2, 2)
            binding.tvShortName.background = BackgroundDrawable.getRectangleBg(
                context,
                4, 4, 4, 4
                , R.color.transparent
                , R.color.red
                , 1
            )
            binding.tvShortName.setTextColor(ContextCompat.getColor(context, R.color.red))
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int, data: TestModel.Step) {
            val binding = viewHolder.binding as AdapterBottomSheetTransportShortBinding
            binding.ivNext.visibility = if (position == 0) View.GONE else View.VISIBLE
            if (position == 0) {
                binding.root.setMarginByDpUnit(18, 0, 0, 0)
            } else if (position == itemCount - 1) {
                binding.root.setMarginByDpUnit(0, 0, 18, 0)
            } else {
                binding.root.setMarginByDpUnit(0, 0, 0, 0)
            }

            binding.tvShortName.visibility = View.GONE
            binding.tvEstimatedTime.visibility = View.GONE
            if (data.mode == "bus" || data.mode == "tram") {
                binding.tvShortName.visibility = View.VISIBLE
            } else {
                binding.tvEstimatedTime.visibility = View.VISIBLE
            }

            binding.ivTransport.setImageResource(
                when (data.mode) {
                    "bus" -> R.drawable.ic_baseline_directions_bus_24
                    "tram" -> R.drawable.ic_baseline_tram_24
                    else -> R.drawable.ic_baseline_directions_walk_24
                }
            )

            binding.tvShortName.text = data.shortName
            binding.tvEstimatedTime.text = data.estimatedTime.toString()
        }

        override fun onItemClick(view: View, position: Int, data: TestModel.Step): Boolean {
            return false
        }

        override fun onItemLongClick(view: View, position: Int, data: TestModel.Step): Boolean {
            return false
        }

        override fun search(
            constraint: CharSequence,
            list: MutableList<TestModel.Step>
        ): MutableList<TestModel.Step> {
            return list
        }
    }

    private val transportAdapter by lazy { TransportAdapter(context) }
    private inner class TransportAdapter(val context: Context): BaseRecyclerViewDataBindingAdapter<TestModel.Step>(
        context,
        listOf(
            R.layout.adapter_bottom_sheet_transport_walk,
            R.layout.adapter_bottom_sheet_transport_bus,
            R.layout.adapter_bottom_sheet_transport_tram
        )
    ) {

        override fun getItemViewType(position: Int): Int {
            return when (getItem(position).mode) {
                "walk" -> 0
                "bus" -> 1
                "tram" -> 2
                else -> 0
            }
        }

        override fun initViewHolder(viewHolder: ViewHolder) {
            val binding = when (viewHolder.itemViewType) {
                0 -> viewHolder.binding as AdapterBottomSheetTransportWalkBinding
                1 -> viewHolder.binding as AdapterBottomSheetTransportBusBinding
                2 -> viewHolder.binding as AdapterBottomSheetTransportTramBinding
                else -> viewHolder.binding
            }
            when (binding) {
                is AdapterBottomSheetTransportBusBinding -> {
                    // bus
                    binding.vLine.background = BackgroundDrawable.getRectangleBg(
                        context,
                        3, 3, 3, 3,
                        R.color.blue,
                        0,
                        0
                    )
                    binding.tvShortName.setPadding(2, 2, 2, 2)
                    binding.tvShortName.setTextColor(ContextCompat.getColor(context, R.color.blue))
                    binding.tvShortName.background = BackgroundDrawable.getRectangleBg(
                        context,
                        4, 4, 4, 4,
                        R.color.transparent,
                        R.color.blue,
                        1
                    )
                }
                is AdapterBottomSheetTransportTramBinding -> {
                    // tram
                    binding.vLine.background = BackgroundDrawable.getRectangleBg(
                        context,
                        3, 3, 3, 3,
                        R.color.red,
                        0,
                        0
                    )
                    binding.tvShortName.setPadding(2, 2, 2, 2)
                    binding.tvShortName.setTextColor(ContextCompat.getColor(context, R.color.red))
                    binding.tvShortName.background = BackgroundDrawable.getRectangleBg(
                        context,
                        4, 4, 4, 4,
                        R.color.transparent,
                        R.color.red,
                        1
                    )
                }
            }
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int, data: TestModel.Step) {
            val binding = when (viewHolder.itemViewType) {
                0 -> viewHolder.binding as AdapterBottomSheetTransportWalkBinding
                1 -> viewHolder.binding as AdapterBottomSheetTransportBusBinding
                2 -> viewHolder.binding as AdapterBottomSheetTransportTramBinding
                else -> viewHolder.binding
            }
            when (binding) {
                is AdapterBottomSheetTransportWalkBinding -> {
                    // walk
                    binding.tvArriveTime.text = context.getString(R.string.bottom_sheet_adapter_walk_arrive_time).format(data.estimatedTime, data.distance)
                }
                is AdapterBottomSheetTransportBusBinding -> {
                    // bus
                    binding.tvDestinationName.text = data.destinationName
                    binding.tvShortName.text = data.shortName
                    binding.tvStartTime.text = data.startedOn.toDate().toString("hh:mm a")
                }
                is AdapterBottomSheetTransportTramBinding -> {
                    // tram
                    binding.tvDestinationName.text = data.destinationName
                    binding.tvShortName.text = data.shortName
                    binding.tvStartTime.text = data.startedOn.toDate().toString("hh:mm a")
                }
            }
        }

        override fun onItemClick(view: View, position: Int, data: TestModel.Step): Boolean {
            return false
        }

        override fun onItemLongClick(view: View, position: Int, data: TestModel.Step): Boolean {
            return false
        }

        override fun search(
            constraint: CharSequence,
            list: MutableList<TestModel.Step>
        ): MutableList<TestModel.Step> {
            return list
        }
    }
}






























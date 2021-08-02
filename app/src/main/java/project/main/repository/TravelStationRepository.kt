package project.main.repository

import android.content.Context
import com.google.gson.Gson
import com.wang.metropiatest.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import project.main.api.model.TestModel
import project.main.database.BikeStationEntity
import project.main.database.database
import project.main.tools.ResponseError
import java.io.BufferedReader
import java.io.InputStreamReader


class TravelStationRepository(private val context: Context){
    private val TAG = javaClass::class.java.simpleName

    enum class Type {
        API,
        Database,
        raw
    }

    interface Listener {
        fun onStart()
        fun onResult(type: Type, code: Int, model: TestModel)
        fun onFail(type: Type, code: Int, responseError: ResponseError)
    }

    class Result(val type: Type, val code: Int, val model: TestModel, val responseError: ResponseError?)

    fun getCache(): List<BikeStationEntity> {
        return context.database.bikeStationDao.allData
    }

    private var lastSyncApiTime = 0L
    /**
     * 異步取資料
     * */
    fun enqueueData(listener: Listener) {
        GlobalScope.launch(Dispatchers.IO) {
            val raw = context.resources.openRawResource(R.raw.transit)
            val rd = BufferedReader(InputStreamReader(raw))
            val model = Gson().fromJson(rd, TestModel::class.java)
            listener.onResult(Type.raw, 0, model)
        }
    }

    /**
     * 同步取資料
     * */
    fun executeData(): Result {
        val raw = context.resources.openRawResource(R.raw.transit)
        val rd = BufferedReader(InputStreamReader(raw))
        val model = Gson().fromJson(rd, TestModel::class.java)
        return Result(Type.raw, 200, model, null)
    }
}
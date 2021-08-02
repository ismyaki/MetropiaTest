package project.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wang.metropiatest.R
import com.wang.metropiatest.databinding.ActivitySplashBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import project.main.repository.BikeStationRepository
import project.main.tools.DateTool

class SplashActivity : AppCompatActivity() {
    private val TAG = javaClass::class.java.simpleName

    private val context by lazy { this }

    private lateinit var mBinding: ActivitySplashBinding

    private val dataRepository by lazy { BikeStationRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        syncData()
    }

    private fun syncData(){
        GlobalScope.launch {
//            dataRepository.executeData()
            delay(DateTool.oneSec)
            GlobalScope.launch(Dispatchers.Main) {
                Intent().also {
                    it.setClass(context, MainActivity::class.java)
                    startActivity(it)
                    finish()
                }
            }
        }
    }
}
package com.journaler.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.example.velord.masteringandroiddevelopmentwithkotlin.R
import com.journaler.model.MODE

abstract class ItemActivity:BaseActivity() {
    override fun getActivityTitle(): Int = R.string.app_name
    protected var mode = MODE.VIEW
    protected var succes = Activity.RESULT_CANCELED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMode()
    }

    override fun onDestroy() {
        super.onDestroy()
        setResult(succes)
    }

    private fun setMode(){
        val data = intent.extras
        data?.let {
            val modeToSet = data.getInt(MODE.EXTRAS_KEY , MODE.VIEW.mode)
            mode = MODE.getByValue(modeToSet)
        }
        Log.v(tag , "Mode [$mode]")
    }
}
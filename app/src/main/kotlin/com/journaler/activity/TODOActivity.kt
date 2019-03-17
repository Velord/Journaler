package com.journaler.activity

import android.os.Bundle
import com.example.velord.masteringandroiddevelopmentwithkotlin.R
import com.journaler.`interface`.IRetrieveDataFromCausedActivity
import kotlinx.android.synthetic.main.activity_todo.*

class TODOActivity: ItemActivity() , IRetrieveDataFromCausedActivity{
    companion object {
        val EXTRA_DATE = "EXTRA_DATE"
        val EXTRA_TIME = "EXTRA_TIME"
    }

    override val tag: String = "TODOActivity"
    override fun getLayout(): Int = R.layout.activity_todo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrieveDataFromCausedActivity()
    }

    override fun retrieveDataFromCausedActivity() {
        val data = intent.extras
        data?.let {
            val date = data.getString(EXTRA_DATE , "")
            val time = data.getString(EXTRA_TIME , "")

            pick_date.text = date
            pick_time.text = time
        }
    }
}
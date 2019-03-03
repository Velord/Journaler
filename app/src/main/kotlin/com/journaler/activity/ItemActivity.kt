package com.journaler.activity

import com.example.velord.masteringandroiddevelopmentwithkotlin.R

abstract class ItemActivity:BaseActivity() {
    override fun getActivityTitle(): Int = R.string.app_name
}
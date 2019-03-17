package com.journaler.activity

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.velord.masteringandroiddevelopmentwithkotlin.R
import com.journaler.permission.PermissionCompatActivity
import kotlinx.android.synthetic.main.activity_header.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.jar.Manifest

abstract class BaseActivity(): PermissionCompatActivity() {
    companion object {
        private var fontExoBold: Typeface? = null
        private var fontExoRegular: Typeface? = null
        val REQUEST_GPS = 0

        fun applyFonts(view: View , ctx: Context){
            var vTag = ""
            if (view.tag is String)
                vTag = view.tag as String

            when(view){
                // if first enter Activity  get all view in this activity and call applyFonts for all
                is ViewGroup -> {
                    for (x in  0..view.childCount - 1)
                        applyFonts(view.getChildAt(x) ,ctx)
                }
                is Button -> {
                    // if vTag is string with the value of string resource by the name "tag_font_bold" ->
                    // typeface = fontExoBold , else typeface = fontExoRegular
                    when(vTag){
                        ctx.getString(R.string.tag_font_bold)  -> {
                            view.typeface = fontExoBold
                        }
                        else -> {
                        view.typeface = fontExoRegular
                    }
                    }
                }
                is TextView -> {
                    when(vTag){
                        ctx.getString(R.string.tag_font_bold) -> {
                            view.typeface = fontExoBold
                        }
                        else ->{
                            view.typeface = fontExoRegular
                        }
                    }
                }
                is EditText -> {
                    when(vTag){
                        ctx.getString(R.string.tag_font_bold) -> {
                            view.typeface = fontExoBold
                        }
                        else -> {
                            view.typeface = fontExoRegular
                        }
                    }
                }
            }
        }
    }
    protected abstract val tag: String
    protected abstract fun getLayout(): Int
    protected abstract fun getActivityTitle(): Int

    //initialisation like a layout for activity
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fade_in , R.anim.fade_out)
        setContentView(getLayout())
        setSupportActionBar(toolbar)
        requestPermissions(
            android.Manifest.permission.ACCESS_FINE_LOCATION ,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        Log.v(tag, "[ ON CREATE 1 ]")
    }

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.v(tag, "[ ON CREATE 2 ]")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Log.v(tag, "[ ON POST CREATE ]")
        applyFonts()
    }

    override fun onRestart() {
        super.onRestart()
        Log.v(tag, "[ ON RESTART ]")
    }

    override fun onStart() {
        super.onStart()
        Log.v(tag, "[ ON START ]")
    }

    override fun onResume() {
        super.onResume()
        Log.v(tag, "[ ON RESUME ]")
        val animation = getAnimation(R.anim.top_to_bottom)
        findViewById<View>(R.id.toolbar).startAnimation(animation)
    }

    override fun onPostResume() {
        super.onPostResume()
        Log.v(tag, "[ ON POST RESUME ]")
    }

    override fun onPause() {
        super.onPause()
        Log.v(tag, "[ ON PAUSE ]")
        val animation = getAnimation(R.anim.hide_to_top)
        findViewById<View>(R.id.toolbar).startAnimation(animation)
    }

    override fun onStop() {
        super.onStop()
        Log.v(tag, "[ ON STOP ]")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(tag, "[ ON DESTROY ]")
    }

    protected  fun applyFonts(){
        initFonts()
        Log.v(tag, "Applying fonts [START]")
        val rootView: View = findViewById(android.R.id.content)
        applyFonts(rootView , this)
        Log.v(tag , "Apllying fonts [END]")
    }

    private fun initFonts(){
        if(fontExoBold == null){
            Log.v(tag, "Initializing font [Exo2-Bold]")
            fontExoBold =
                Typeface.createFromAsset(assets , "fonts/Exo-Bold.otf")
        }
        if (fontExoRegular == null){
            Log.v(tag , "Initializing font [Exo2-Regular]")
            fontExoRegular =
                    Typeface.createFromAsset(assets , "fonts/Exo-Regular.otf")
        }
    }

    protected fun Activity.getAnimation(animation: Int): Animation =
            AnimationUtils.loadAnimation(this, animation)
}
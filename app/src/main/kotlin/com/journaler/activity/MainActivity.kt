package com.journaler.activity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.*
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.MenuItem
import com.example.velord.masteringandroiddevelopmentwithkotlin.R
import com.journaler.fragment.ItemsFragment
import com.journaler.fragment.MyDialogFragment
import com.journaler.navigation.NavigationDrawerAdapter
import com.journaler.navigation.NavigationDrawerItem
import com.journaler.preferences.PreferencesConfiguration
import com.journaler.preferences.PreferencesProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(){
    override val  tag = "MainActivity"
    override fun getLayout(): Int = R.layout.activity_main
    override fun getActivityTitle(): Int = R.string.app_name

    private val keyPagePosition = "keyPagePosition"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pagerProviderAndPositionViaSharedPreferences()
        instantiatedMenuItems()
    }

    //like a button onClickListener
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.drawing_menu->{
                drawer_layout.openDrawer(GravityCompat.START)
                Log.v(tag,"Main menu")
                return true
            }
            R.id.options_menu -> {
                Log.v(tag , "Options menu")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun instantiatedMenuItems(){
        val menuItems = mutableListOf<NavigationDrawerItem>()
        val today = NavigationDrawerItem(
            getString(R.string.today),
            Runnable {
                Log.v(tag , "Today Menu")
                pager.setCurrentItem(0 , true)
            }
        )
        val next7days = NavigationDrawerItem(
            getString(R.string.next7days),
            Runnable {
                Log.v(tag , "Next7Days Menu")
                pager.setCurrentItem(1 , true)
            }
        )
        val notes = NavigationDrawerItem(
            getString(R.string.notes),
            Runnable {
                Log.v(tag , "Notes Menu")
                pager.setCurrentItem(2, true)
            }
        )
        val todos = NavigationDrawerItem(
            getString(R.string.todos),
            Runnable {
                Log.v(tag , "TODOs Menu")
                pager.setCurrentItem(3 , true)
            }
        )

        menuItems.add(today)
        menuItems.add(next7days)
        menuItems.add(notes)
        menuItems.add(todos)

        val navDrawAdap = NavigationDrawerAdapter(this, menuItems)

        left_drawer.adapter = navDrawAdap
    }

    private fun buildAndCallNotification(context: Context){
        //That is how to call notification
        val notification = NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Hi!")
            .setContentText("Hello World!")
        //Define Intent for activity of your application
        val result = Intent(context , MainActivity::class.java)
        //Now define the stack builder object that will contain the back stack for the activity as follows
        val builder = TaskStackBuilder.create(context)
        //Add back stack for the intent
        builder.addParentStack(MainActivity::class.java)
        //Add intent at the top of the stack
        builder.addNextIntent(result)
        val resultPendingIntent = builder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT )
        //Define ID for the notification and notify:
        val id = 0
        notification.setContentIntent(resultPendingIntent)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        manager.notify(id, notification.build())
    }

    private fun callDialogFragment(manager: FragmentManager){
//        That is how to call dialog fragment
        val myDialog = MyDialogFragment()
        myDialog.show(manager, "Dialog")
    }

    private fun pagerProviderAndPositionViaSharedPreferences(){
        val provider = PreferencesProvider()
        val config = PreferencesConfiguration("journaler_prefs" , Context.MODE_PRIVATE)
        val preferences = provider.obtain(config , this)

        pager.adapter = ViewPagerAdapter(supportFragmentManager)
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {
                // Ignore
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                // Ignore
            }

            override fun onPageSelected(p0: Int) {
                Log.v(tag , "Page [$p0]")
                preferences.edit().putInt(keyPagePosition , p0).apply()
            }
        })
        //log when application is start or restart and set page 
        val pagerPosition = preferences.getInt(keyPagePosition , 0)
        pager.setCurrentItem(pagerPosition , true)
        Log.v(tag , "Page [$pagerPosition]")
    }

    inner class ViewPagerAdapter(manager: FragmentManager)
        : FragmentStatePagerAdapter(manager){
        override fun getItem(position: Int): Fragment = ItemsFragment()
        override fun getCount(): Int = 5
    }
}

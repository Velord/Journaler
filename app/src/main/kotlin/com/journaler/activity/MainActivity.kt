package com.journaler.activity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.velord.masteringandroiddevelopmentwithkotlin.R
import com.journaler.Journaler
import com.journaler.fragment.ItemsFragment
import com.journaler.fragment.ManualFragment
import com.journaler.fragment.MyDialogFragment
import kotlinx.android.synthetic.main.activity_header.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(){
    override val  tag = "MainActivity"
    override fun getLayout(): Int = R.layout.activity_main
    override fun getActivityTitle(): Int = R.string.app_name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pager.adapter = ViewPagerAdapter(supportFragmentManager)
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

    inner class ViewPagerAdapter(manager: FragmentManager)
        : FragmentStatePagerAdapter(manager){
        override fun getItem(position: Int): Fragment = ItemsFragment()
        override fun getCount(): Int = 5
    }
}

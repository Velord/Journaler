package com.journaler.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.ListView
import com.example.velord.masteringandroiddevelopmentwithkotlin.R
import com.journaler.Journaler
import com.journaler.activity.NoteActivity
import com.journaler.activity.TODOActivity
import com.journaler.fragment.ItemsFragment.companion.TODO_REQUEST
import com.journaler.fragment.ItemsFragment.companion.NOTE_REQUEST
import com.journaler.model.MODE
import java.text.SimpleDateFormat
import java.util.*

class ItemsFragment : BaseFragment() {
    object companion{
        val NOTE_REQUEST = 0
        val TODO_REQUEST = 1
    }

    override val logTag = "Items fragment"
    override fun getLayout(): Int = R.layout.fragment_items

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view  = inflater.inflate(getLayout() , container , false)
        setFABOnClickListener(view)
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onActivityResultTreatmentRequestAndResultCode(requestCode , resultCode)
    }

    override fun onResume() {
        super.onResume()
        animateFAB(
            view!!.findViewById<FloatingActionButton>(R.id.fab),
            false, 1L , 1L
        )
        changeBackgroundItems(3000L)
    }

    private fun changeBackgroundItems(delay: Long){
        val items = view?.findViewById<ListView>(R.id.items)
        items?.let {
            //we can use items.postDelayed or Handler().postDelayed
            Handler().postDelayed({
                if (!activity!!.isFinishing)
                    items.setBackgroundColor(
                        ContextCompat.getColor(
                            Journaler.ctx!!,
                            R.color.grey_text_middle))
            },delay)
        }
    }

    private fun onActivityResultTreatmentRequestAndResultCode(requestCode: Int ,  resultCode: Int){
        when(requestCode){
            TODO_REQUEST -> {
                if (resultCode == Activity.RESULT_OK)
                    Log.i(logTag , "We created new TODO")
                else
                    Log.w(logTag , "We didn't created new TODO")
            }
            NOTE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK)
                    Log.i(logTag , "We created new Note")
                else
                    Log.w(logTag , "We did't created new Note")
            }
        }
    }
    //always need view to find fab, given the fact that we are in fragment class
    private fun setFABOnClickListener(view: View){
        val btn = view.findViewById<FloatingActionButton>(R.id.fab)
        btn?.setOnClickListener {
            animateFAB(btn)

            val items = arrayOf(
                getString(R.string.notes),
                getString(R.string.todos)
            )
            val builder = AlertDialog.Builder(this@ItemsFragment.context)
                .setTitle(R.string.choose_a_type)
                .setCancelable(true)
                .setOnCancelListener {
                    animateFAB(btn ,false)
                }
                .setItems(
                    items,
                    {_, which ->
                        when(which){
                            0 -> openCreateNote()
                            1 -> openCreateTODO()
                            else -> Log.v(tag , "Unknown options selected [$which]")
                        }
                    }
                )
            builder.show()
            Log.v(tag, "FAB CLick")
        }
    }

    private fun openCreateNote(){
        val intent = Intent(context , NoteActivity::class.java)
        val data = Bundle()
        data.putInt(MODE.EXTRAS_KEY , MODE.CREATE.mode)
        intent.putExtras(data)
        startActivityForResult(intent , NOTE_REQUEST)
    }

    private fun openCreateTODO(){
        val dateTime = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH)
        val timeFormat = SimpleDateFormat("MM:HH", Locale.ENGLISH)

        val intent = Intent(context , TODOActivity::class.java)
        val data  = Bundle()

        data.putInt(MODE.EXTRAS_KEY , MODE.CREATE.mode)
        data.putString(TODOActivity.EXTRA_DATE , dateFormat.format(dateTime))
        data.putString(TODOActivity.EXTRA_TIME , timeFormat.format(dateTime))
        intent.putExtras(data)

        startActivityForResult(intent , TODO_REQUEST)
    }

    private  fun animateFAB(btn: FloatingActionButton , expand: Boolean = true ,
                            durationFirstSecondAnimation: Long = 1000 ,
                            durationThirdAnimation: Long = 500){
        val animationFirst =
            ObjectAnimator.ofFloat(btn , "scaleX",
                if (expand){1.5f} else {1.0f})
        animationFirst.duration = durationFirstSecondAnimation
        animationFirst.interpolator = BounceInterpolator()

        val animationSecond =
            ObjectAnimator.ofFloat(btn , "scaleY",
                if (expand){1.5f} else {1.0f})
        animationSecond.duration = durationFirstSecondAnimation
        animationSecond.interpolator = BounceInterpolator()

        val animationThird =
            ObjectAnimator.ofFloat(btn , "alpha" ,
                if (expand){0.3f} else {1.0f})
        animationThird.duration = durationThirdAnimation
        animationThird.interpolator = AccelerateInterpolator()

        val setAnimations = AnimatorSet()
        setAnimations.play(animationFirst).with(animationSecond).before(animationThird)
        setAnimations.start()
    }
}
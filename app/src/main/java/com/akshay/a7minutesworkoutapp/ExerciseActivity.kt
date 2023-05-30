package com.akshay.a7minutesworkoutapp

import android.app.Dialog
import android.content.Intent


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshay.a7minutesworkoutapp.databinding.ActivityExerciseBinding
import com.akshay.a7minutesworkoutapp.databinding.DialogCustomBackConformationBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    // progress bar use variables :
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0


    // Variable for the exercise list and current position of exercise
    private var exerciseList: ArrayList<ExerciseModel>? =null  // We will initialize the list later
    private var currentExercisePosition = -1    // Current position of Exercise


    private var binding : ActivityExerciseBinding? = null    // binding variable

    // Variable for Text to speech which will be initialization later on.
    private var tts: TextToSpeech?= null    // Variable for text to speech


    private var resttime : String = "0"
    private var exercisetime : String ="0"
    // Declaring a variable of an adapter class to bind it ro recycler view.
    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        resttime = intent.getStringExtra(Constants.Rest_Time).toString()
        exercisetime = intent.getStringExtra(Constants.Exercise_Time).toString()

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        tts =TextToSpeech(this,this)
        exerciseList = Constants.defaultExerciseList()

        setupRestView()

        setupExerciseStatusRecyclerView()
    }


    private fun setupRestView(){


        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE
        binding?.upcomingLable?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility= View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.ivimage?.visibility = View.INVISIBLE

        // here firstly we will check if the timer is running and it is not null then cancel the running timer and start the new one.
        if (restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }

        binding?.tvUpcomingExerciseName?.text = exerciseList!![currentExercisePosition +1].getName()
        setRestProgressBar()
    }

    private fun setRestProgressBar(){
        val po = currentExercisePosition
        if (po <= -1){
            binding?.progrssBar?.max = 15
            val num : Int = 15
            val tu :Long = 15
            binding?.progrssBar?.progress= restProgress  // Sets the current progress to the specified value
            // here we have started a timer of 10 second so the 10000 is millisecond is 10 second and the countdown interval is 1 second so it 1000.
            restTimer = object : CountDownTimer(tu*1000,1000) {
                override fun onTick(millisUntilFinished: Long) {
                    restProgress++
                    binding?.progrssBar?.progress = num - restProgress       // Indicates progress bar progress
                    binding?.tvTimer?.text = (num - restProgress).toString()     // Current progress is set to text view in terms of second
                }

                override fun onFinish() {   // when the 10 second will complete this will be executed.
                    currentExercisePosition++
                    exerciseList!![currentExercisePosition].setIsSelected(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupExerciseView()
                }
            }.start()
        }else {


            binding?.progrssBar?.max = resttime.toInt()
            val num: Int = resttime.toInt()
            val tu: Long = resttime.toLong()
            binding?.progrssBar?.progress =
                restProgress  // Sets the current progress to the specified value
            // here we have started a timer of 10 second so the 10000 is millisecond is 10 second and the countdown interval is 1 second so it 1000.
            restTimer = object : CountDownTimer(tu * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    restProgress++
                    binding?.progrssBar?.progress =
                        num - restProgress       // Indicates progress bar progress
                    binding?.tvTimer?.text =
                        (num - restProgress).toString()     // Current progress is set to text view in terms of second
                }

                override fun onFinish() {   // when the 10 second will complete this will be executed.
                    currentExercisePosition++
                    exerciseList!![currentExercisePosition].setIsSelected(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupExerciseView()
                }
            }.start()
        }
    }

    private fun setupExerciseView(){



        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE
        binding?.upcomingLable?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility= View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.ivimage?.visibility = View.VISIBLE

        if (restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.ivimage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text= exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()
    }

    private fun setExerciseProgressBar(){
        binding?.progressBarExercise?.progress = exercisetime.toInt()
        binding?.progressBarExercise?.max = exercisetime.toInt()
        val num : Long = exercisetime.toLong()

        restTimer = object : CountDownTimer(num*1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBarExercise?.progress = num.toInt() - restProgress
                binding?.tvTimerExercise?.text = (num.toInt() - restProgress).toString()
            }

            override fun onFinish() {

                if (currentExercisePosition < exerciseList?.size!! - 1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    var nu = intent.getStringExtra(Constants.Rest_Time).toString()
                    speakOut("Rest for"+ nu +"second")
                    setupRestView()
                }else{
                    finish()
                    var intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)

                }
            }

        }.start()
    }


    // Here in the Destroy fun we will reset the rest timer if it is running.
    public override fun onDestroy() {
        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        if (tts != null){
            tts?.stop()
            tts?.shutdown()
        }



        super.onDestroy()
        binding = null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.ENGLISH)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "The language specified is not supported!")
            }
        }else{
            Log.e("TTS", "Initialization Failed!")
        }
    }

    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH,null,"")
    }

    private fun setupExerciseStatusRecyclerView(){
        // Defining a layout manager for the recycler view
        // Here we have used a Linear Layout Manager With horizontal scroll.
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // As the adapter expects the exercise list and context so initialization it passing it.
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)

        //Adapter class is attached to recycler view
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }
    //end for recycler view

    override fun onBackPressed(){
        customDialogForBackButton()
    }

    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConformationBinding.inflate(layoutInflater)

        customDialog.setContentView(dialogBinding.root)

        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.tvYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.tvNo.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }
}
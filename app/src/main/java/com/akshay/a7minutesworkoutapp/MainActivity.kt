package com.akshay.a7minutesworkoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.akshay.a7minutesworkoutapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)



        //val flStartButton: FrameLayout = findViewById(R.id.flStart)
        binding?.flStart?.setOnClickListener {
                val intent = Intent(this, ExerciseActivity::class.java)
                intent.putExtra(Constants.Rest_Time, "30")
                intent.putExtra(Constants.Exercise_Time, "60")
                startActivity(intent)
        }

        binding?.btnBMI?.setOnClickListener {
            val intent = Intent(this, BMIActivity::class.java )
            startActivity(intent)
        }

        binding?.btnHistory?.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java )
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // To avoid memory leak we unassign the binding once the activity is destroyed
        binding = null
    }
}
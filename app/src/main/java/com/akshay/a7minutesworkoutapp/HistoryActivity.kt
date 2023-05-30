package com.akshay.a7minutesworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshay.a7minutesworkoutapp.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.ArrayList

class HistoryActivity : AppCompatActivity() {

    private var binding : ActivityHistoryBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarHistoryActivity)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "History"
            binding?.toolbarHistoryActivity?.setNavigationOnClickListener {
                onBackPressed()
            }
        }

        val dao = (application as WorkOutApp).db.historyDao()
        getAllCompletedDate(dao)
    }

    private fun getAllCompletedDate(historyDao: HistoryDao){
        Log.e("Date: ", "getAllCompletedDate run")
        lifecycleScope.launch {
            historyDao.fetchAllDates().collect {allCompletedDatesList ->
                if (allCompletedDatesList.isNotEmpty()){
                    binding?.tvHistory?.visibility = View.VISIBLE
                    binding?.rvHistory?.visibility = View.VISIBLE
                    binding?.tvNoDataAvailable?.visibility = View.GONE

                    binding?.rvHistory?.layoutManager = LinearLayoutManager(this@HistoryActivity)

                    val dates = ArrayList<String>()
                    for (date in allCompletedDatesList){
                        dates.add(date.date)
                    }
                    val historyAdapter = HistoryAdapter(dates)

                    binding?.rvHistory?.adapter = historyAdapter
                }else{
                    binding?.tvHistory?.visibility = View.GONE
                    binding?.rvHistory?.visibility = View.GONE
                    binding?.tvNoDataAvailable?.visibility = View.VISIBLE
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
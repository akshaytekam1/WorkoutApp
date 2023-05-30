package com.akshay.a7minutesworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.akshay.a7minutesworkoutapp.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" // metric unit view
        private const val US_UNIT_VIEW = "US_UNIT_VIEW" // Us Unit View
    }
    private var currentVisibleView: String = METRIC_UNITS_VIEW //a variable to hold a value to make a selected view visible
    private var binding : ActivityBmiBinding? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
            binding?.toolbarBmiActivity?.setNavigationOnClickListener {
                onBackPressed()
            }
        }

        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUsUnitsView()
            }
        }


        binding?.btnCalculateBmi?.setOnClickListener {
            if (currentVisibleView == METRIC_UNITS_VIEW) {
                if (validateMetricCountInt()) {
                    val heightValue: Float =
                        binding?.etMetricUnitHeight?.text.toString().toFloat() / 100

                    val weightValue: Float = binding?.etMetricUnitWeight?.text.toString().toFloat()

                    val bmi = weightValue / (heightValue * heightValue)

                    displayBmiResult(bmi)
                } else {
                    Toast.makeText(this, "Please Enter the Valid Values.", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                if (validateUsMetricCount()) {
                    val inInch : Float  = binding?.etUsMetricUnitHeight?.text.toString().toFloat() * 12
                    val height :  Float = binding?.etUsMetricUnitHeightInch?.text.toString().toFloat() + inInch
                    val weight : Float = binding?.etUsMetricUnitWeight?.text.toString().toFloat()
                    val bmi = (weight * 703) / (height * height)
                    displayBmiResult(bmi)
                }else{
                    Toast.makeText(this, "Please Enter the Valid Values.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

    private fun validateMetricCountInt(): Boolean {
        var isValid = true

        if (binding?.etMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }else if (binding?.etMetricUnitHeight?.text.toString().isEmpty()){
            isValid = false
        }

        return  isValid
    }

    private fun validateUsMetricCount():Boolean{
        var isValid = true
        if (binding?.etUsMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }else if(binding?.etUsMetricUnitHeight?.text.toString().isEmpty()){
            isValid = false
        }else if (binding?.etUsMetricUnitHeightInch?.text.toString().isEmpty()){
            isValid =false
        }
        return isValid
    }

    private fun displayBmiResult(bmi: Float) {
        binding?.resultLayout?.visibility = View.VISIBLE
        val bmiLabel : String
        val bmiDescription : String

        if(bmi.compareTo(15f) <=0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "Ooos! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(15f) >0 && bmi.compareTo(16f) <=0 ){
            bmiLabel = "Severely underweight"
            bmiDescription = "Ooos! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(16f) >0 && bmi.compareTo(18.5f) <=0){
            bmiLabel = "underweight"
            bmiDescription ="Ooos! You really need to take better care of yourself! Eat more!"
        }else if (bmi.compareTo(18.5f) >0 && bmi.compareTo(25f) <=0){
            bmiLabel = "Normal"
            bmiDescription ="Congratulations! You are in a good shape!"
        }else if (bmi.compareTo(25f) >0 && bmi.compareTo(30f) <=0 ){
            bmiLabel ="Overweight"
            bmiDescription ="Oops! You really need to take care of your yourself! Workout maybe!"
        }else if (bmi.compareTo(30f) >0 && bmi.compareTo(35f) <=0){
            bmiLabel = "Obese Class || (Moderately) obese"
            bmiDescription ="Oops! You really need to take care of your yourself! Workout maybe!"
        }else if(bmi.compareTo(35f)>0 && bmi.compareTo(40f) <=0){
            bmiLabel ="Obese Class || (Severely Obese)"
            bmiDescription ="Oops! You really need to take care of your yourself! Workout maybe!"
        }else{
            bmiLabel = "Very Severely Obese"
            bmiDescription ="Oops! You really need to take care of your yourself! Workout maybe!"
        }

        binding?.tvyourbmi?.visibility = View.VISIBLE
        val result = BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()
        binding?.tvBMIValue?.text = result
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.INVISIBLE
        binding?.tilUsMetricUnitHeightFeet?.visibility = View.INVISIBLE
        binding?.tilUsMetricUnitHeightInch?.visibility = View.INVISIBLE
        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()
        binding?.resultLayout?.visibility = View.INVISIBLE
    }

    private fun makeVisibleUsUnitsView(){
        currentVisibleView = US_UNIT_VIEW
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilUsMetricUnitHeightFeet?.visibility = View.VISIBLE
        binding?.tilUsMetricUnitHeightInch?.visibility = View.VISIBLE

        binding?.etUsMetricUnitWeight?.text!!.clear()
        binding?.etUsMetricUnitHeight?.text!!.clear()
        binding?.etUsMetricUnitHeightInch?.text!!.clear()
        binding?.resultLayout?.visibility = View.INVISIBLE
    }
}


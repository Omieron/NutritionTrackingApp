package com.group20.nutritiontrackingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.group20.nutritiontrackingapp.databinding.ActivityReportBinding

class ReportActivity : AppCompatActivity() {

    // Variables
    lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding section
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Listeners
        binding.reportBackBtn.setOnClickListener {
            finish()
        }
    }
}
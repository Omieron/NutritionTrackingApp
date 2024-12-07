package com.group20.nutritiontrackingapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.group20.nutritiontrackingapp.databinding.ActivityMainBinding
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
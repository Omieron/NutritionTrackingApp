package com.group20.nutritiontrackingapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.group20.nutritiontrackingapp.databinding.ActivityPersonBinding

class PersonActivity : AppCompatActivity() {

    lateinit var binding: ActivityPersonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Binding section
        binding = ActivityPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Listeners
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}
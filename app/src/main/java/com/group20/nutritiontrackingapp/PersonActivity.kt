package com.group20.nutritiontrackingapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.group20.nutritiontrackingapp.databinding.ActivityMainBinding
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
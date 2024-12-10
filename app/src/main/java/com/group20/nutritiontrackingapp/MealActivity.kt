package com.group20.nutritiontrackingapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.group20.nutritiontrackingapp.databinding.ActivityMainBinding
import com.group20.nutritiontrackingapp.databinding.ActivityMealBinding
import com.group20.nutritiontrackingapp.databinding.ActivityPersonBinding

class MealActivity : AppCompatActivity() {

    lateinit var binding: ActivityMealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Binding section
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setting title
        var mealTitle: String = intent.getStringExtra("MEAL_TYPE").toString()
        binding.tvTitle.text = mealTitle

        //Listeners
        binding.backButton.setOnClickListener {

            finish()
        }
    }
}
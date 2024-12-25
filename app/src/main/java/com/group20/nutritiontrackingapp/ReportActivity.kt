package com.group20.nutritiontrackingapp

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.group20.nutritiontrackingapp.chart.DonutChart
import com.group20.nutritiontrackingapp.databinding.ActivityReportBinding
import com.group20.nutritiontrackingapp.db.AppDatabase
import com.group20.nutritiontrackingapp.db.Meal
import com.group20.nutritiontrackingapp.util.Constants

class ReportActivity : AppCompatActivity() {
    // Variables
    private lateinit var binding: ActivityReportBinding
    private lateinit var donutChart: DonutChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding section
        binding = ActivityReportBinding.inflate(layoutInflater)
        //donutChart = findViewById(R.id.donutChart)
        setContentView(binding.root)
        enableEdgeToEdge()

        val db: AppDatabase by lazy {
            Room.databaseBuilder(
                this,
                AppDatabase::class.java,
                Constants.DATABASE_NAME
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }

        val meals = db.mealDao().getAllMeals()

        if(meals.isNotEmpty())
            getMealsGiveMacros(meals)

        //Listeners
        binding.reportBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun getMealsGiveMacros(meals : MutableList<Meal>) {
        var totalProtein = 0.0
        var totalCarbs = 0.0
        var totalFat = 0.0

        val carbText : TextView = findViewById(R.id.carbRate)
        val proText : TextView = findViewById(R.id.proRate)
        val fatText : TextView = findViewById(R.id.fatRate)

        for (meal in meals) {
            totalProtein += meal.protein
            totalCarbs += meal.carbs
            totalFat += meal.fat
        }

        var totalMacros = totalProtein + totalCarbs + totalFat

        val proteinPercentage = (totalProtein / totalMacros * 100).toFloat()
        val carbsPercentage = (totalCarbs / totalMacros * 100).toFloat()
        val fatPercentage = (totalFat / totalMacros * 100).toFloat()

        carbText.text = String.format("%.2f", totalCarbs) + "g"
        proText.text = String.format("%.2f", totalProtein) + "g"
        fatText.text = String.format("%.2f", totalFat) + "g"
        DonutChart.updateData(carbsPercentage,proteinPercentage,fatPercentage)
    }
}
package com.group20.nutritiontrackingapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.group20.nutritiontrackingapp.adapter.MealCustomRecyclerViewAdapter
import com.group20.nutritiontrackingapp.databinding.ActivityMealBinding
import com.group20.nutritiontrackingapp.db.AppDatabase
import com.group20.nutritiontrackingapp.db.Meal
import com.group20.nutritiontrackingapp.retrofit.ApiClient
import com.group20.nutritiontrackingapp.retrofit.MealService
import com.group20.nutritiontrackingapp.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealActivity : AppCompatActivity() {

    lateinit var binding: ActivityMealBinding
    private lateinit var mealService: MealService
    private lateinit var mealAdapter: MealCustomRecyclerViewAdapter
    private var mealList: MutableList<Meal> = mutableListOf()

    private val db: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Binding section
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setting title
        var mealTitle: String = intent.getStringExtra("MEAL_TYPE").toString()
        binding.tvTitle.text = mealTitle

        // Set up recycler view
        mealAdapter = MealCustomRecyclerViewAdapter(this, mealList, db)
        binding.recyclerView.apply {
            adapter = mealAdapter
            layoutManager = LinearLayoutManager(this@MealActivity)
        }

        // Initialize Retrofit service
        mealService = ApiClient.getClient().create(MealService::class.java)
        val request = mealService.getMeals()

        request.enqueue(object : Callback<List<Meal>> {
            override fun onFailure(call: Call<List<Meal>>, t: Throwable) {
                Log.e("API_ERROR", "Failed to fetch meals", t)
                Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Meal>>, response: Response<List<Meal>>) {
                if (response.isSuccessful) {
                    response.body()?.let { meals ->
                        mealList.clear()
                        mealList.addAll(meals)
                        displayMeals(mealList)
                        Log.d("API_SUCCESS", "Fetched ${meals.size} meals")
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Error: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })

        //Listeners

        // Add button click listener
        binding.addMealButton.setOnClickListener {
            val selectedMeal = mealAdapter.getSelectedMeal()
            if (selectedMeal != null) {
                db.mealDao().insertMeal(selectedMeal)
                Toast.makeText(
                    this,
                    "${selectedMeal.name} added",
                    Toast.LENGTH_SHORT
                ).show()
                // Return to main
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Please select a meal first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    fun displayMeals(meals: MutableList<Meal>) {
        mealAdapter.setData(meals)
    }
}
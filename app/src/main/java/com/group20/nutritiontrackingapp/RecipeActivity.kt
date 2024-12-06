package com.sarazebardast.project

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.sarazebardast.project.databinding.ActivityRecipeBinding
import com.sarazebardast.project.db.AppDatabase
import com.sarazebardast.project.db.Recipe
import com.sarazebardast.project.util.Constants

class RecipeActivity : AppCompatActivity() {

    // Variables
    private lateinit var binding: ActivityRecipeBinding

    // Database
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

        // View binding
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the recipe ID from intent
        val recipeId = intent.getIntExtra("recipe_id", -1)

        // Display
        if (recipeId != -1) {
            // Get recipe from database
            val recipe = db.recipeDao().getRecipeById(recipeId)

            // Display recipe details
            displayRecipeDetails(recipe)
        } else {
            // Handle error - invalid recipe ID
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Listeners
        binding.btnBackRecipe.setOnClickListener {
            finish()
        }
    }

    // Functions
    private fun displayRecipeDetails(recipe: Recipe) {
        binding.apply {
            recipeImage.setImageResource(recipe.imgId)
            recipeTitle.text = recipe.title
            recipeInstructions.text = recipe.instructions
            recipeCalories.text = "Calories: ${recipe.totalCalories}"
            recipeProtein.text = "Protein: ${recipe.totalProtein}g"
            recipeCarbs.text = "Carbs: ${recipe.totalCarbs}g"
            recipeFat.text = "Fat: ${recipe.totalFat}g"
            recipePrepTime.text = "Prep Time: ${recipe.prepTime} min"
        }
    }
}
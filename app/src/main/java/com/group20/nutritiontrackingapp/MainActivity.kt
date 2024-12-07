package com.group20.nutritiontrackingapp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.group20.nutritiontrackingapp.adapter.ExerciseCustomRecyclerViewAdapter
import com.group20.nutritiontrackingapp.adapter.RecipeCustomRecyclerViewAdapter
import com.group20.nutritiontrackingapp.databinding.ActivityMainBinding
import com.group20.nutritiontrackingapp.databinding.ExerciseDialogBinding
import com.group20.nutritiontrackingapp.db.AppDatabase
import com.group20.nutritiontrackingapp.db.Exercise
import com.group20.nutritiontrackingapp.db.Recipe
import com.group20.nutritiontrackingapp.util.Constants
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Collections
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity(),ExerciseCustomRecyclerViewAdapter.ExerciseAdapterInterface {

    // Variables
    lateinit var customDialog: Dialog
    lateinit var binding: ActivityMainBinding
    lateinit var bindingForDialog: ExerciseDialogBinding
    var adapter: RecipeCustomRecyclerViewAdapter?=null
    var exerciseAdapter: ExerciseCustomRecyclerViewAdapter? = null
    private var selectedExercise: Exercise? = null
    private var exerciseTimeMinutes: Int = 0
    private var totalCaloriesBurned: Int = 0
    private var activeMinutes: Int = 0
    private var waterCount = 0
    private lateinit var waterGlasses: List<ImageView>

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
        enableEdgeToEdge()

        // Binding section
        bindingForDialog = ExerciseDialogBinding.inflate(layoutInflater)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recipeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        //Listeners
        binding.profileButton.setOnClickListener {
            val intent = Intent(this, PersonActivity::class.java)
            startActivity(intent)
        }

        binding.reportButton.setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }

        binding.addWaterButton.setOnClickListener {
            if (waterCount < 8) {
                waterCount++
                updateWaterDisplay()
                saveWaterCount()
            }
        }

        binding.addWaterButton.setOnLongClickListener { // Long click to reset it just in case
            waterCount = 0
            updateWaterDisplay()
            saveWaterCount()
            true
        }

        //data
        prepareData()
        getData()

        //Dialog
        binding.addExerciseButton.setOnClickListener{
            customDialog.show()
        }
        createDailog()

        // Water
        // shared pref - >  like a mini database for simple values -> used to save
        val prefs = getSharedPreferences("WaterPrefs", Context.MODE_PRIVATE)
        val today = LocalDate.now().toString()
        val lastDate = prefs.getString("lastDate", "")

        // Reset count if it's a new day
        waterCount = if (today == lastDate) {
            prefs.getInt("waterCount", 0)
        } else {
            0
        }

        // Initialize water glass ImageViews
        waterGlasses = listOf(
            findViewById(R.id.glass1),
            findViewById(R.id.glass2),
            findViewById(R.id.glass3),
            findViewById(R.id.glass4),
            findViewById(R.id.glass5),
            findViewById(R.id.glass6),
            findViewById(R.id.glass7),
            findViewById(R.id.glass8)
        )
        updateWaterDisplay()

        // Title
        // Today's date formatted
        val sdf = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
        val currentDate = sdf.format(Date())

        // Set the title
        binding.currentDate.text = currentDate
    }

    // Functions
    fun displayRecipes(recipes: MutableList<Recipe>) {
        adapter = RecipeCustomRecyclerViewAdapter(this,recipes)
        binding.recipeRecyclerView.adapter = adapter
        adapter?.notifyDataSetChanged()
    }

    fun getData(){
        if(db.recipeDao().getAllRecipes().isNotEmpty()){
            displayRecipes(db.recipeDao().getAllRecipes())
        }
        else{
            binding.recipeRecyclerView.adapter = null
        }
    }

    // Temporary data until we learn retrofit
    fun prepareData() {
        val recipes = ArrayList<Recipe>()
        Collections.addAll(recipes,
            Recipe(
                id = 1,
                title = "Greek Yogurt Parfait",
                instructions = "1. Start with a base of 1 cup Greek yogurt in a tall glass or mason jar. Add a layer of mixed fresh berries (strawberries, blueberries, raspberries). Sprinkle 1/4 cup of your favorite granola. Add another layer of Greek yogurt. Top with more berries. Drizzle with 1 tablespoon of honey. Finish with a handful of chopped almonds or walnuts. Best served immediately while granola is still crunchy",
                totalCalories = 320,
                totalProtein = 15.0,
                totalCarbs = 45.0,
                totalFat = 12.0,
                prepTime = 5,
                imgId = R.drawable.food1
            ),

            Recipe(
                id = 2,
                title = "Quinoa Buddha Bowl",
                instructions = "1. Rinse 1 cup quinoa thoroughly and cook in 2 cups water for 15-20 minutes until fluffy 2. Meanwhile, preheat oven to 400°F 3. Drain and pat dry 1 can chickpeas, toss with olive oil and seasonings 4. Cut sweet potato into 1-inch cubes, toss with olive oil and seasonings 5. Roast chickpeas and sweet potatoes for 25 minutes, stirring halfway 6. Wash and chop 2 cups kale, massage with olive oil 7. Make tahini dressing: mix 2 tbsp tahini, lemon juice, garlic, and water\\n8. Assemble bowl: quinoa base, topped with roasted vegetables, kale\\n9. Drizzle with tahini dressing and serve warm",
                totalCalories = 450,
                totalProtein = 18.0,
                totalCarbs = 65.0,
                totalFat = 16.0,
                prepTime = 25,
                imgId = R.drawable.food2
            ),

            Recipe(
                id = 3,
                title = "Grilled Chicken Salad",
                instructions = "1. Marinate chicken breast in olive oil, lemon juice, garlic, and herbs for 30 minutes 2. Preheat grill or grill pan to medium-high heat 3. Grill chicken for 6-7 minutes per side until internal temperature reaches 165°F 4. Let chicken rest for 5 minutes, then slice against the grain\\n5. Wash and dry 4 cups mixed salad greens 6. Halve 1 cup cherry tomatoes 7. Make balsamic vinaigrette: whisk together balsamic vinegar, olive oil, Dijon mustard, honey 8. Assemble salad: bed of greens, sliced chicken, tomatoes 9. Drizzle with vinaigrette just before serving 10. Optional: add cucumber, red onion, or avocado",
                totalCalories = 380,
                totalProtein = 35.0,
                totalCarbs = 12.0,
                totalFat = 22.0,
                prepTime = 20,
                imgId = R.drawable.food3
            ),

            Recipe(
                id = 4,
                title = "Baked Salmon",
                instructions = "1. Preheat oven to 400°F and line a baking sheet with parchment paper 2. Pat salmon fillet dry with paper towels 3. Drizzle with 1 tablespoon olive oil 4. Season generously with salt, pepper, and dried herbs 5. Slice one lemon thinly and arrange on top of salmon 6. Prepare vegetables: cut broccoli, carrots, and Brussels sprouts into similar sizes 7. Toss vegetables with olive oil, salt, pepper, and garlic powder 8. Arrange vegetables around salmon on baking sheet 9. Bake for 12-15 minutes until salmon flakes easily 10. Let rest for 5 minutes before serving 11. Garnish with fresh herbs and additional lemon wedges",
                totalCalories = 420,
                totalProtein = 46.0,
                totalCarbs = 8.0,
                totalFat = 25.0,
                prepTime = 25,
                imgId = R.drawable.food4
            ),

            Recipe(
                id = 5,
                title = "Overnight Oats",
                instructions = "1. In a mason jar or container, combine 1/2 cup old-fashioned oats 2. Add 1 cup of your preferred milk (dairy, almond, oat) 3. Stir in 1 tablespoon chia seeds 4. Add 1-2 teaspoons maple syrup or honey to taste 5. Optional: add 1/4 teaspoon vanilla extract 6. Optional: add 1/2 scoop protein powder 7. Stir mixture well to combine all ingredients 8. Seal container and refrigerate overnight (at least 6 hours) 9. In the morning, stir oats and add more milk if needed10. Top with fresh fruit, nuts, seeds, or additional maple syrup 11. Can be stored in refrigerator for up to 3 days",
                totalCalories = 350,
                totalProtein = 12.0,
                totalCarbs = 56.0,
                totalFat = 9.0,
                prepTime = 5,
                imgId = R.drawable.food5
            ),

            Recipe(
                id = 6,
                title = "Turkey Wrap",
                instructions = "1. Lay out one large whole wheat tortilla 2. Spread 1 tablespoon mustard evenly on tortilla 3. Layer 3-4 slices of turkey breast in the center 4. Add 2-3 fresh lettuce leaves, washed and dried 5. Layer 3-4 thin slices of tomato 6. Add 1/4 sliced avocado, fanned out 7. Optional: add red onion, cucumber, or bell peppers 8. Season with salt, pepper, and any preferred herbs 9. Fold in sides of tortilla 10. Roll tightly from bottom up, keeping filling in place 11. Cut diagonally and serve immediately 12. Can be wrapped in foil for lunch on-the-go",
                totalCalories = 340,
                totalProtein = 28.0,
                totalCarbs = 32.0,
                totalFat = 14.0,
                prepTime = 10,
                imgId = R.drawable.food6
            ),

            Recipe(
                id = 7,
                title = "Vegetarian Stir-Fry",
                instructions = "1. Press one block of firm tofu for 15 minutes to remove excess moisture 2. Cut tofu into 1-inch cubes 3. Cook 1 cup brown rice according to package instructions 4. Heat 2 tablespoons oil in a large wok or skillet over medium-high heat 5. Add tofu cubes and cook until golden brown on all sides 6. Remove tofu and set aside 7. In the same pan, add sliced carrots, broccoli, snap peas 8. Stir-fry vegetables for 3-4 minutes until crisp-tender 9. Make sauce: combine soy sauce, ginger, garlic, cornstarch, and water 10. Return tofu to pan, add sauce and stir until thickened 11. Serve hot over brown rice 12. Garnish with sesame seeds and green onions",
                totalCalories = 380,
                totalProtein = 20.0,
                totalCarbs = 48.0,
                totalFat = 16.0,
                prepTime = 20,
                imgId = R.drawable.food7
            ),

            Recipe(
                id = 8,
                title = "Protein Smoothie",
                instructions = "1. Gather all ingredients: protein powder, banana, mixed berries, almond milk, spinach 2. Peel and slice one ripe banana (can use frozen for thicker texture) 3. Add 1 cup mixed frozen berries to blender 4. Add 1 scoop (30g) protein powder of choice 5. Add 1 cup fresh spinach leaves 6. Pour in 1 cup unsweetened almond milk 7. Optional: add 1 tablespoon chia seeds or flax seeds 8. Optional: add 1 tablespoon nut butter for healthy fats 9. Blend on high speed until completely smooth 10. Add more almond milk if needed for desired consistency 11. Taste and adjust sweetness if needed 12. Serve immediately for best taste and texture",
                totalCalories = 280,
                totalProtein = 24.0,
                totalCarbs = 38.0,
                totalFat = 6.0,
                prepTime = 5,
                imgId = R.drawable.food8
            ),

            Recipe(
                id = 9,
                title = "Mediterranean Bowl",
                instructions = "1. Prepare falafel according to package instructions or recipe 2. Warm pita bread in oven or toaster 3. Dice 1 cucumber into small cubes 4. Halve 1 cup cherry tomatoes 5. Slice 1/4 cup Kalamata olives 6. Prepare hummus: blend chickpeas, tahini, lemon, garlic, olive oil 7. Optional: make tzatziki sauce with yogurt, cucumber, dill 8. Arrange in bowl: start with 2-3 falafels 9. Add a generous scoop of hummus 10. Add diced vegetables and olives 11. Drizzle with olive oil and lemon juice 12. Sprinkle with za'atar seasoning if available 13. Serve with warm pita bread on the side 14. Garnish with fresh parsley or mint",
                totalCalories = 440,
                totalProtein = 16.0,
                totalCarbs = 52.0,
                totalFat = 22.0,
                prepTime = 15,
                imgId = R.drawable.food9
            ),

            Recipe(
                id = 10,
                title = "Egg White Omelette",
                instructions = "1. Separate 4-5 eggs, reserving whites only 2. Wash and chop 1 cup fresh spinach 3. Slice 4-5 mushrooms thinly 4. Grate 1/4 cup low-fat cheese 5. Heat non-stick pan over medium heat 6. Lightly coat pan with cooking spray 7. Sauté mushrooms until golden brown 8. Add spinach and cook until wilted 9. Remove vegetables and set aside 10. Whisk egg whites until slightly frothy 11. Season with salt, pepper, and herbs of choice 12. Pour egg whites into pan, tilting to spread evenly 13. When edges start to set, add vegetables and cheese to one half 14. Fold empty half over filling 15. Cook until cheese melts and omelette is set 16. Serve immediately with fresh herbs garnish.",
                totalCalories = 220,
                totalProtein = 24.0,
                totalCarbs = 6.0,
                totalFat = 12.0,
                prepTime = 15,
                imgId = R.drawable.food10
            )
        )
        db.recipeDao().insertAllRecipes(recipes)
    }

    fun createDailog() {
        customDialog = Dialog(this)
        customDialog.setContentView(bindingForDialog.root)

        // Setup RecyclerView with adapter
        val recyclerView = bindingForDialog.exerciseRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Setup search functionality
        bindingForDialog.searchExercise.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                exerciseAdapter?.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Display exercises from database
        if(db.exerciseDao().getAllExercises().isNotEmpty()) {
            displayExercises(db.exerciseDao().getAllExercises())
        } else {
            prepareExerciseData()
            displayExercises(db.exerciseDao().getAllExercises())
        }

        bindingForDialog.addButton.setOnClickListener {
            selectedExercise?.let { exercise ->
                exerciseTimeMinutes = 30
                val caloriesBurned = (exercise.caloriesPerHour * exerciseTimeMinutes) / 60

                // Update totals
                totalCaloriesBurned += caloriesBurned
                activeMinutes += exerciseTimeMinutes

                // Update UI
                updateExerciseStats()

                Toast.makeText(this, "Added ${exercise.name}", Toast.LENGTH_SHORT).show()
                // Clear selection
                selectedExercise = null
                customDialog.dismiss()
            }
        }

        bindingForDialog.cancelButton.setOnClickListener {
            // Clear selection
            selectedExercise = null
            customDialog.dismiss()
        }
    }

    private fun prepareExerciseData() {
        val exercises = ArrayList<Exercise>()
        exercises.add(Exercise(
            name = "Running",
            caloriesPerHour = 600,
            category = "Intense",
            description = "High-impact cardio exercise"
        ))
        exercises.add(Exercise(
            name = "Walking",
            caloriesPerHour = 200,
            category = "Light",
            description = "Low-impact cardio exercise"
        ))
        exercises.add(Exercise(
            name = "Swimming",
            caloriesPerHour = 400,
            category = "Moderate",
            description = "Full-body workout in water"
        ))
        exercises.add(Exercise(
            name = "Cycling",
            caloriesPerHour = 450,
            category = "Moderate",
            description = "Low-impact cardio on bike"
        ))
        exercises.add(Exercise(
            name = "HIIT",
            caloriesPerHour = 700,
            category = "Intense",
            description = "High-intensity interval training"
        ))
        exercises.add(Exercise(
            name = "Yoga",
            caloriesPerHour = 250,
            category = "Light",
            description = "Mind-body exercise for flexibility"
        ))

        db.exerciseDao().insertAllExercises(exercises)
    }

    override fun displayExercise(exercise: Exercise) {
        selectedExercise = exercise
        bindingForDialog.addButton.isEnabled = true
    }

    override fun displayExercises(exercises: MutableList<Exercise>) {
        exerciseAdapter = ExerciseCustomRecyclerViewAdapter(this, exercises)
        bindingForDialog.exerciseRecyclerView.adapter = exerciseAdapter
        exerciseAdapter?.notifyDataSetChanged()
    }

    private fun updateExerciseStats() {
        // Update the exercise section texts
        binding.caloriesBurnedText.text = "Calories Burned: $totalCaloriesBurned kcal"
        binding.activeMinutesText.text = "Active Minutes: $activeMinutes min"
    }

    private fun updateWaterDisplay() {
        waterGlasses.forEachIndexed { index, glass ->
            glass.setImageResource(
                if (index < waterCount) R.drawable.water_full
                else R.drawable.ic_drop_empty
            )
        }
    }

    private fun saveWaterCount() {
        getSharedPreferences("WaterPrefs", Context.MODE_PRIVATE).edit().apply {
            putInt("waterCount", waterCount)
            putString("lastDate", LocalDate.now().toString())
            apply()
        }
    }
}

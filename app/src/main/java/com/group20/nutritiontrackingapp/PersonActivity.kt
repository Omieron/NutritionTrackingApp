package com.group20.nutritiontrackingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.group20.nutritiontrackingapp.databinding.ActivityPersonBinding
import com.group20.nutritiontrackingapp.db.AppDatabase
import com.group20.nutritiontrackingapp.db.Person
import com.group20.nutritiontrackingapp.db.PersonDAO
import com.group20.nutritiontrackingapp.util.Constants

class PersonActivity : AppCompatActivity() {

    // Variables
    lateinit var binding: ActivityPersonBinding
    private lateinit var personDao: PersonDAO
    private var isFirstTime = true

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
        binding = ActivityPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //DB
        personDao = db.personDao()

        checkExistingData()

        // Listeners
        binding.backBtn.setOnClickListener {
            if (validateFields()) {
                saveData()
            }
            finish()
        }
    }

    private fun checkExistingData() {
        val person = personDao.getPersonById(1000)
        Log.d("PersonActivity", "Checking existing data: ${person?.toString() ?: "No person found"}")
        person?.let {
            // Fill in the fields with existing data
            binding.apply {
                nameTv.setText(it.name)
                AgeTv.setText(it.age.toString())
                WeightTv.setText(it.weight.toString())
                HeightTv.setText(it.height.toString())
                calGoalTv.setText(it.calorieGoal.toString())
                proteinGoalTv.setText(it.proteinGoal.toString())
                if (it.sex == 'F') {
                    femaleRb.isChecked = true
                    binding.imageView3.setImageResource(R.drawable.baseline_person_female_24)
                }
                else {
                    MaleRb.isChecked = true
                    binding.imageView3.setImageResource(R.drawable.baseline_person_male_24)
                }
            }
        }
    }

    private fun validateFields(): Boolean {
        binding.apply {
            if (nameTv.text.isBlank()) {
                nameTv.error = "Name required"
                return false
            }
            if (AgeTv.text.isBlank()) {
                AgeTv.error = "Age required"
                return false
            }
            if (WeightTv.text.isBlank()) {
                WeightTv.error = "Weight required"
                return false
            }
            if (HeightTv.text.isBlank()) {
                HeightTv.error = "Height required"
                return false
            }
            if (calGoalTv.text.isBlank()) {
                calGoalTv.error = "Calorie goal required"
                return false
            }
            if (proteinGoalTv.text.isBlank()) {
                proteinGoalTv.error = "Protein goal required"
                return false
            }
            if (!femaleRb.isChecked && !MaleRb.isChecked) {
                Toast.makeText(this@PersonActivity, "Please select gender", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun saveData() {
        val person = Person(
            id = 1000,
            name = binding.nameTv.text.toString(),
            age = binding.AgeTv.text.toString().toInt(),
            weight = binding.WeightTv.text.toString().toDouble(),
            height = binding.HeightTv.text.toString().toInt(),
            sex = if (binding.femaleRb.isChecked) 'F' else 'M',
            calorieGoal = binding.calGoalTv.text.toString().toInt(),
            proteinGoal = binding.proteinGoalTv.text.toString().toDouble()
        )

        // Save to database
        val existingPerson = personDao.getPersonById(1000)
        if (existingPerson == null) {
            personDao.insertPerson(person)
            Log.d("PersonActivity", "Inserted new person")
        } else {
            personDao.updatePerson(person)
            Log.d("PersonActivity", "Updated existing person")
        }

        finish()
    }

}
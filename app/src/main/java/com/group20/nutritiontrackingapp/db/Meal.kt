package com.group20.nutritiontrackingapp.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.group20.nutritiontrackingapp.util.Constants

@Entity(
    tableName = Constants.MEAL_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = Person::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class Meal(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    // Foreign key
    var personId: Int,   // Links to the person who ate the meal

    var name: String,
    var servingSize: Double = 1.0,  // Multiplier for recipe portions
    var calories: Int,
    var protein: Double,
    var carbs: Double,
    var fat: Double,

    var mealType: String, // e.g., "Breakfast", "Lunch", "Dinner", "Snack"
)
package com.sarazebardast.project.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sarazebardast.project.util.Constants

@Entity(tableName = Constants.RECIPE_TABLE)
class Recipe(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var instructions: String,
    var totalCalories: Int,
    var totalProtein: Double,
    var totalCarbs: Double,
    var totalFat: Double,
    var prepTime: Int,
    var imgId: Int
)
package com.group20.nutritiontrackingapp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Recipe::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDAO
    //abstract fun customerDao(): CustomerDAO
}

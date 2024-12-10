package com.group20.nutritiontrackingapp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Recipe::class, Exercise::class, Person::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDAO
    abstract fun exerciseDao(): ExerciseDAO
    abstract fun personDao(): PersonDAO
}

package com.group20.nutritiontrackingapp.retrofitRecipe

import com.group20.nutritiontrackingapp.db.Recipe
import retrofit2.Call
import retrofit2.http.GET

interface RecipeService {
    @GET("MQMP") // The endpoint for your JSON
    fun getRecipes(): Call<List<Recipe>>
}
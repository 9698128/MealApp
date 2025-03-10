package com.example.mealapp.network

import com.example.mealapp.model.CategoryResponse
import com.example.mealapp.model.IngredientResponse
import com.example.mealapp.model.MealDetailResponse
import com.example.mealapp.model.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {
    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): MealResponse

    @GET("lookup.php")
    suspend fun getMealDetails(@Query("i") mealId: String): MealDetailResponse

    @GET("list.php?i=list")
    suspend fun getIngredients(): IngredientResponse

    @GET("filter.php")
    suspend fun getMealsByIngredient(@Query("i") ingredient: String): MealResponse
}
package com.example.mealapp.model

data class IngredientResponse(
    val meals: List<Ingredient>
)

data class Ingredient(
    val idIngredient: String,
    val strIngredient: String,
    val strDescription: String?,
    val strType: String?
)
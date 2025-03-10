package com.example.mealapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealapp.model.Meal
import com.example.mealapp.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MealViewModel : ViewModel() {
    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchMealsByCategory(category: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                val response = RetrofitInstance.api.getMealsByCategory(category)
                _meals.value = response.meals

                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                _error.value = e.message ?: "Errore durante il caricamento dei piatti"
                e.printStackTrace()
            }
        }
    }
}
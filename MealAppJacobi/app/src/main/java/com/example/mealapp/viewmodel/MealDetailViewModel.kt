package com.example.mealapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealapp.model.MealDetail
import com.example.mealapp.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MealDetailViewModel : ViewModel() {
    private val _mealDetails = MutableStateFlow<MealDetail?>(null)
    val mealDetails: StateFlow<MealDetail?> = _mealDetails.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchMealDetails(mealId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                val response = RetrofitInstance.api.getMealDetails(mealId)
                _mealDetails.value = response.meals?.firstOrNull()

                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                _error.value = e.message ?: "Unknown error occurred"
                e.printStackTrace()
            }
        }
    }
}
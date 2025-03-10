package com.example.mealapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealapp.model.Category
import com.example.mealapp.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                val response = RetrofitInstance.api.getCategories()
                _categories.value = response.categories

                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                _error.value = e.message ?: "Errore durante il caricamento delle categorie"
                e.printStackTrace()
            }
        }
    }
}
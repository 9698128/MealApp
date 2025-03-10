package com.example.mealapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealapp.model.Ingredient
import com.example.mealapp.model.Meal
import com.example.mealapp.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class IngredientViewModel : ViewModel() {
    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val ingredients: StateFlow<List<Ingredient>> = _ingredients.asStateFlow()

    private val _filteredIngredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val filteredIngredients: StateFlow<List<Ingredient>> = _filteredIngredients.asStateFlow()

    private val _selectedIngredients = MutableStateFlow<Set<Ingredient>>(emptySet())
    val selectedIngredients: StateFlow<Set<Ingredient>> = _selectedIngredients.asStateFlow()

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Flag per indicare se i risultati della ricerca sono pronti
    private val _searchResultsReady = MutableStateFlow(false)
    val searchResultsReady: StateFlow<Boolean> = _searchResultsReady.asStateFlow()

    init {
        fetchIngredients()
    }

    private fun fetchIngredients() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                val response = RetrofitInstance.api.getIngredients()
                val sortedIngredients = response.meals.sortedBy { it.strIngredient }
                _ingredients.value = sortedIngredients
                _filteredIngredients.value = sortedIngredients

                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                _error.value = e.message ?: "Errore durante il caricamento degli ingredienti"
                e.printStackTrace()
            }
        }
    }

    fun searchIngredients(query: String) {
        if (query.isBlank()) {
            _filteredIngredients.value = _ingredients.value
        } else {
            _filteredIngredients.value = _ingredients.value.filter {
                it.strIngredient.contains(query, ignoreCase = true)
            }
        }
    }

    fun toggleIngredientSelection(ingredient: Ingredient) {
        val currentSelection = _selectedIngredients.value.toMutableSet()

        if (currentSelection.contains(ingredient)) {
            currentSelection.remove(ingredient)
        } else {
            currentSelection.add(ingredient)
        }

        _selectedIngredients.value = currentSelection
        _searchResultsReady.value = false // Resetta i risultati di ricerca quando cambia la selezione
    }

    fun isIngredientSelected(ingredient: Ingredient): Boolean {
        return _selectedIngredients.value.contains(ingredient)
    }

    fun searchMealsByIngredients() {
        if (_selectedIngredients.value.isEmpty()) {
            _meals.value = emptyList()
            _searchResultsReady.value = true
            return
        }

        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                // Utilizzare il primo ingrediente per ottenere una lista iniziale di piatti
                val firstIngredient = _selectedIngredients.value.first()
                val response = RetrofitInstance.api.getMealsByIngredient(firstIngredient.strIngredient)
                var filteredMeals = response.meals

                // Per ogni ingrediente aggiuntivo, filtra ulteriormente i risultati
                for (ingredient in _selectedIngredients.value.drop(1)) {
                    if (filteredMeals.isEmpty()) break

                    val ingredientResponse = RetrofitInstance.api.getMealsByIngredient(ingredient.strIngredient)
                    val ingredientMealIds = ingredientResponse.meals.map { it.idMeal }.toSet()

                    // Mantieni solo i piatti che contengono anche questo ingrediente
                    filteredMeals = filteredMeals.filter { ingredientMealIds.contains(it.idMeal) }
                }

                _meals.value = filteredMeals
                _searchResultsReady.value = true
                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                _error.value = e.message ?: "Errore durante il caricamento dei piatti"
                e.printStackTrace()
                _searchResultsReady.value = true
            }
        }
    }

    fun clearSelections() {
        _selectedIngredients.value = emptySet()
        _meals.value = emptyList()
        _searchResultsReady.value = false
    }
}
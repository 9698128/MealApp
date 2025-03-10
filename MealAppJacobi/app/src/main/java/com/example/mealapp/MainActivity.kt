package com.example.mealapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mealapp.ui.HomeScreen
import com.example.mealapp.ui.IngredientSearchScreen
import com.example.mealapp.ui.MealDetailScreen
import com.example.mealapp.ui.MealListScreen
import com.example.mealapp.ui.theme.MealAppTheme
import com.example.mealapp.viewmodel.HomeViewModel
import com.example.mealapp.viewmodel.IngredientViewModel
import com.example.mealapp.viewmodel.MealViewModel
import com.example.mealapp.viewmodel.MealDetailViewModel

/**
 * ISTRUZIONI: Sostituisci il contenuto del tuo file MainActivity.kt con questo codice.
 * Assicurati di NON avere altro codice di navigazione nel progetto (come Navigation.kt)
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MealAppTheme {
                MealApp()
            }
        }
    }
}

@Composable
fun MealApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            val homeViewModel: HomeViewModel = viewModel()
            HomeScreen(
                viewModel = homeViewModel,
                onCategoryClick = { category ->
                    navController.navigate("category/$category")
                },
                onSearchByIngredientClick = {
                    navController.navigate("search_ingredients")
                }
            )
        }

        composable("search_ingredients") {
            val ingredientViewModel: IngredientViewModel = viewModel()
            IngredientSearchScreen(
                viewModel = ingredientViewModel,
                onMealClick = { mealId ->
                    navController.navigate("mealDetail/$mealId")
                },
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "category/{categoryName}",
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val mealViewModel: MealViewModel = viewModel()

            MealListScreen(
                categoryName = categoryName,
                viewModel = mealViewModel,
                onMealClick = { mealId ->
                    navController.navigate("mealDetail/$mealId")
                },
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "mealDetail/{mealId}",
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId") ?: ""

            MealDetailScreen(
                mealId = mealId,
                onBackPressed = { navController.popBackStack() }
            )
        }
    }
}
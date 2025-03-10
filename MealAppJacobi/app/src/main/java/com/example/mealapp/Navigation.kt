package com.example.mealapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mealapp.ui.HomeScreen
import com.example.mealapp.ui.MealDetailScreen
import com.example.mealapp.ui.MealListScreen
import com.example.mealapp.viewmodel.HomeViewModel
import com.example.mealapp.viewmodel.MealViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

object Destinations {
    const val HOME = "home"
    const val CATEGORY_LIST = "category/{categoryName}"
    const val MEAL_DETAIL = "meal_detail/{mealId}"

    fun categoryRoute(categoryName: String): String {
        return "category/$categoryName"
    }

    fun mealDetailRoute(mealId: String): String {
        return "meal_detail/$mealId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()

    NavHost(navController = navController, startDestination = Destinations.HOME) {
        composable(Destinations.HOME) {
            HomeScreen(
                viewModel = homeViewModel,
                onCategoryClick = { category ->
                    navController.navigate(Destinations.categoryRoute(category))
                },
                onSearchByIngredientClick = {
                    navController.navigate("search_ingredients")
                }
            )
        }

        composable(
            route = Destinations.CATEGORY_LIST,
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val mealViewModel: MealViewModel = viewModel()

            MealListScreen(
                categoryName = categoryName,
                viewModel = mealViewModel,
                onMealClick = { mealId ->
                    navController.navigate(Destinations.mealDetailRoute(mealId))
                },
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Destinations.MEAL_DETAIL,
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
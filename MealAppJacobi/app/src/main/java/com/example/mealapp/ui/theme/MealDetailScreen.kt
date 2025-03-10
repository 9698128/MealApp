package com.example.mealapp.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.mealapp.model.MealDetail
import com.example.mealapp.viewmodel.MealDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(
    mealId: String,
    viewModel: MealDetailViewModel = viewModel(),
    onBackPressed: () -> Unit
) {
    // Trigger the fetch when the screen is created
    LaunchedEffect(mealId) {
        viewModel.fetchMealDetails(mealId)
    }

    val mealDetails by viewModel.mealDetails.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        mealDetails?.strMeal ?: "Loading...",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (error != null) {
                Text(
                    text = "Error: ${error}",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                mealDetails?.let { meal ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(meal.strMealThumb),
                            contentDescription = meal.strMeal,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = meal.strMeal,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Category: ${meal.strCategory ?: "Not specified"}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = "Area: ${meal.strArea ?: "Not specified"}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Ingredients",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Display ingredients
                        Column {
                            listOfNotNull(
                                pair(meal.strIngredient1, meal.strMeasure1),
                                pair(meal.strIngredient2, meal.strMeasure2),
                                pair(meal.strIngredient3, meal.strMeasure3),
                                pair(meal.strIngredient4, meal.strMeasure4),
                                pair(meal.strIngredient5, meal.strMeasure5),
                                pair(meal.strIngredient6, meal.strMeasure6),
                                pair(meal.strIngredient7, meal.strMeasure7),
                                pair(meal.strIngredient8, meal.strMeasure8),
                                pair(meal.strIngredient9, meal.strMeasure9),
                                pair(meal.strIngredient10, meal.strMeasure10),
                                pair(meal.strIngredient11, meal.strMeasure11),
                                pair(meal.strIngredient12, meal.strMeasure12),
                                pair(meal.strIngredient13, meal.strMeasure13),
                                pair(meal.strIngredient14, meal.strMeasure14),
                                pair(meal.strIngredient15, meal.strMeasure15),
                                pair(meal.strIngredient16, meal.strMeasure16),
                                pair(meal.strIngredient17, meal.strMeasure17),
                                pair(meal.strIngredient18, meal.strMeasure18),
                                pair(meal.strIngredient19, meal.strMeasure19),
                                pair(meal.strIngredient20, meal.strMeasure20)
                            ).forEach { (ingredient, measure) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "$ingredient:",
                                        modifier = Modifier.weight(1f),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = measure ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Instructions",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = meal.strInstructions ?: "No instructions available.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // YouTube Tutorial Button
                        meal.strYoutube?.takeIf { it.isNotBlank() }?.let { youtubeLink ->
                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                    contentColor = MaterialTheme.colorScheme.onTertiary
                                )
                            ) {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = "Watch Tutorial",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("See tutorial")
                            }
                        }
                    }
                }
            }
        }
    }
}

// Helper function to create pairs of ingredients and measurements
// Only returns the pair if the ingredient is not null or blank
private fun pair(ingredient: String?, measure: String?): Pair<String, String?>? {
    return if (!ingredient.isNullOrBlank()) {
        Pair(ingredient, measure)
    } else {
        null
    }
}
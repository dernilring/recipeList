package com.example.recipelist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipelist.ui.theme.RecipeListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeListTheme {
                RecipeScreen()
            }
        }
    }
}

@Composable
fun RecipeScreen(viewModel: RecipeViewModel = viewModel()) {
    val context = LocalContext.current
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                viewModel.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                viewModel.errorMessage != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = viewModel.errorMessage.orEmpty(),
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(onClick = { viewModel.fetchRecipes() }) {
                            Text(text = "retry")
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(viewModel.recipes, key = { it.id }) { recipe ->
                            RecipeCard(
                                recipe = recipe, // 👈 исправлено
                                onItemClick = {
                                    Toast.makeText(
                                        context,
                                        recipe.name,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeCard(
    recipe: MyRecipe,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onItemClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Раскомментируйте то, что захотите показывать:
            // Text(
            //     text = recipe.cuisine,
            //     style = MaterialTheme.typography.bodyMedium
            // )
            // Spacer(modifier = Modifier.height(12.dp))
            // Row(
            //     modifier = Modifier.fillMaxWidth(),
            //     horizontalArrangement = Arrangement.spacedBy(10.dp),
            //     verticalAlignment = Alignment.CenterVertically
            // ) {
            //     Text("prep: ${recipe.prepTimeMinutes} min", style = MaterialTheme.typography.bodySmall)
            //     Text("cook: ${recipe.cookTimeMinutes} min", style = MaterialTheme.typography.bodySmall)
            //     Text("servings: ${recipe.servings}", style = MaterialTheme.typography.bodySmall)
            // }
            //
            // Spacer(modifier = Modifier.height(8.dp))
            // Text(
            //     text = "Теги: ${recipe.tags.joinToString(", ")}",
            //     style = MaterialTheme.typography.labelSmall,
            //     color = MaterialTheme.colorScheme.primary
            // )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipePreview() {
    val sampleRecipe = MyRecipe(
        id = 1,
        name = "Classic Margherita Pizza",
        ingredients = listOf(
            "Pizza dough",
            "Tomato sauce",
            "Fresh mozzarella cheese",
            "Fresh basil leaves",
            "Olive oil",
            "Salt and pepper to taste"
        ),
        instructions = listOf(
            "Preheat the oven to 475°F (245°C).",
            "Roll out the pizza dough and spread tomato sauce evenly.",
            "Top with slices of fresh mozzarella and fresh basil leaves.",
            "Drizzle with olive oil and season with salt and pepper.",
            "Bake in the preheated oven for 12-15 minutes or until the crust is golden brown.",
            "Slice and serve hot."
        ),
        prepTimeMinutes = 20,
        cookTimeMinutes = 15,
        servings = 4,
        difficulty = Difficulty.Easy,
        cuisine = "Italian",
        caloriesPerServing = 300,
        tags = listOf("Pizza", "Italian"),
        userId = 166,
        image = "https://cdn.dummyjson.com/recipe-images/1.webp",
        rating = 4.6,
        reviewCount = 98,
        mealType = listOf("Dinner")
    )
    RecipeCard(recipe = sampleRecipe, onItemClick = {} )
}
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
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
                                recipe = recipe,
                                onItemClick = {
                                    Toast.makeText(
                                        context,
                                        recipe.name,
                                        Toast.LENGTH_LONG
                                    ).show()
                                },
                                onItemLongClick ={  viewModel.removeRecipe(recipe) },
                                onFavoriteItemClick ={ viewModel.toggleFavorite(recipe)}
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeCard(
    recipe: MyRecipe,
    onItemClick: () -> Unit,
    onItemLongClick: () -> Unit,
    onFavoriteItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onItemClick,
                onLongClick = onItemLongClick,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        AsyncImage(
            model = recipe.image,
            contentDescription = recipe.name,
            modifier = Modifier.fillMaxWidth().height(220.dp),
            contentScale = ContentScale.Crop

        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = recipe.name,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
             Text(
                 text = recipe.cuisine,
                 fontSize = 16.sp,
             )
             Spacer(modifier = Modifier.height(12.dp))
             Row(
                 modifier = Modifier.fillMaxWidth(),
                 horizontalArrangement = Arrangement.spacedBy(12.dp),
                 verticalAlignment = Alignment.CenterVertically
             ) {
                 Text("prep: ${recipe.prepTimeMinutes} min", fontSize = 14.sp)
                 Text("cook: ${recipe.cookTimeMinutes} min", fontSize = 14.sp)
                 Text("servings: ${recipe.servings}", fontSize = 14.sp)
                 IconButton(onClick = onFavoriteItemClick) {
                     Icon(
                         painter = painterResource(
                             if (recipe.isLiked) R.drawable.baseline_favorite_24
                             else R.drawable.outline_favorite_24
                         ),
                         contentDescription = if (recipe.isLiked) "remove from liked" else "add to liked",
                         tint = if (recipe.isLiked) Color.Red
                         else MaterialTheme.colorScheme.onSurfaceVariant
                     )
                 }
             }

             Spacer(modifier = Modifier.height(8.dp))
             Text(
                 text = "Теги: ${recipe.tags.joinToString(", ")}",
                 fontSize = 14.sp,
                 color = MaterialTheme.colorScheme.primary
             )

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
        mealType = listOf("Dinner"),
        isLiked = false
    )
    RecipeCard(recipe = sampleRecipe, onItemClick = {} , onItemLongClick = {}, onFavoriteItemClick = {})
}
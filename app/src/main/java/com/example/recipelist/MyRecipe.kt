package com.example.recipelist


data class RecipeResponse(
    val recipes: List<MyRecipe>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

data class MyRecipe(
    val id: Int,
    val name: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val servings: Int,
    val difficulty: Difficulty,
    val cuisine: String,
    val caloriesPerServing: Int,
    val tags: List<String>,
    val userId: Int,
    val image: String,
    val rating: Double,
    val reviewCount: Int,
    val mealType: List<String>,
    val isLiked : Boolean = false
)

enum class Difficulty {
    Easy, Medium, Hard
}
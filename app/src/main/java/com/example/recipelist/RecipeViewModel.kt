package com.example.recipelist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RecipeViewModel: ViewModel(){

    var recipes by mutableStateOf<List<MyRecipe>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init{
         fetchRecipes()
    }
    fun fetchRecipes(){
        viewModelScope.launch{
            isLoading = true
            errorMessage = null
            try {
                val response = RetrofitInstance.apiService.getRecipes()
                recipes = response.recipes
            } catch ( e: Exception){
                errorMessage = e.localizedMessage ?: "error of loading data"
                Log.e("RecipeViewModel", "loading data error", e)
            } finally {
                isLoading = false
            }
        }
    }
    fun removeRecipe(recipe: MyRecipe){
        recipes = recipes.filterNot { it.id == recipe.id }
    }
    fun toggleFavorite(recipe: MyRecipe){
        recipes =recipes.map { r->
                if ( r.id == recipe.id) r.copy (isLiked = !r.isLiked) else r
        }
    }
}
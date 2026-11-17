package com.example.recipelist

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("recipes")
    suspend fun getRecipes() : RecipeResponse

    @GET("recipes/{id}")
    suspend fun getRecipeById(@Path("id") id: Int) : MyRecipe

}
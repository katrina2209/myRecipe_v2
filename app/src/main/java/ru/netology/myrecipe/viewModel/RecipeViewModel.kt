package ru.netology.myrecipe.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.myrecipe.adapter.RecipeInteractionListener
import ru.netology.myrecipe.data.*
import ru.netology.myrecipe.db.AppDb
import ru.netology.myrecipe.util.SingleLiveEvent

class RecipeViewModel(
    application: Application
) : AndroidViewModel(application), RecipeInteractionListener {

    private val repository: RecipeRepository =
        RecipeRepositoryImpl(
            dao = AppDb.getInstance(
                context = application
            ).recipeDao
        )

    val data get() = repository.data

    val currentRecipe = MutableLiveData<Recipe?>(null)

    val navigateToRecipeContentScreenEvent = SingleLiveEvent<Recipe>()
    val navigateToRecipeCardFragmentEvent = SingleLiveEvent<Int>()



    fun onSaveButtonClicked(
        title: String, author: String, category: String, steps: String
    ) {

        if (title.isBlank()) return
        //if (category.isBlank()) return
        //if (steps.isEmpty()) return
        val recipe =
            currentRecipe.value?.copy(
                title = title,
                category = category,
                steps = steps
//                steps = steps?.map {
//                    Step(RecipeRepository.NEW_STEP_ID, it, currentRecipe.value!!.id)
//                },

                ) ?: Recipe(
                id = RecipeRepository.NEW_RECIPE_ID,
                author = author,
                title = title,
                category = category,
                steps = steps
//                steps = steps?.map {
//                    Step(RecipeRepository.NEW_STEP_ID, it, currentRecipe.value!!.id)
//                }
            )
        repository.save(recipe)
        currentRecipe.value = null
    }



    fun searchDatabase(searchQuery: String) = repository.searchDatabase(searchQuery)

    fun filterByCategory(categories: ArrayList<String>) {
        repository.searchByCategory(categories)

    }

    fun clearFilters() {
        repository.clearFilters()
        //println(repository.data.value)
        //  repository.data.value
        // repository.searchByCategory(ArrayList())
    }

// region RecipeInteractionListener

    override fun onFavoriteClicked(recipe: Recipe) {
        repository.favorite(recipe.id)

    }

    override fun onRemoveClicked(recipe: Recipe) {
        currentRecipe.value = null
        repository.delete(recipe.id)
    }

    override fun onEditClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToRecipeContentScreenEvent.value = recipe

    }

    override fun onRecipeClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        navigateToRecipeCardFragmentEvent.value = recipe.id

    }

    // endregion RecipeInteractionListener

 }


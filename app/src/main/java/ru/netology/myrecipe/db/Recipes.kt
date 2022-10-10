package ru.netology.myrecipe.db

import ru.netology.myrecipe.data.Recipe



internal fun RecipeEntity.toModel() = Recipe(
    id = id,
    title = title,
    author = author,
    category = category,
    steps = steps,//converter.toList(steps),
    favoriteForMe = favoriteForMe,

    )

internal fun Recipe.toEntity() =
    RecipeEntity(
        id = id,
        title = title,
        author = author,
        category = category,
        steps = steps,
        favoriteForMe = favoriteForMe
    )

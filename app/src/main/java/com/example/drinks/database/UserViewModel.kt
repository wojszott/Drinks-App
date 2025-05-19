package com.example.drinks.database;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(application:Application) : AndroidViewModel(application) {
    private val drinkDao = AppDatabase.getInstance(application).drinkDao()
    //Pobiera singletonową instancję bazy i DAO do obsługi tabeli User.
    val drinks: LiveData<List<Drink>> = drinkDao.getAllDrinks()
    //Udostępnia obserwowalną listę użytkowników z bazy.
    fun addDrink(
        name: String,
        description: String,
        ingredients: String,
        preparation: String,
        imageResId: Int,
        type: String
    ) {
        viewModelScope.launch {
            drinkDao.insertDrink(
                Drink(
                    name = name,
                    description = description,
                    ingredients = ingredients,
                    preparation = preparation,
                    imageResId = imageResId,
                    type = type
                )
            )
        }
    }
}

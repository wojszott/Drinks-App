package com.example.drinks.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DrinkDao {
    @Query("SELECT * FROM Drink")
    fun getAllDrinks(): LiveData<List<Drink>>
    @Insert
    suspend fun insertDrink(drink: Drink)
    @Insert
    suspend fun insertAll(drinks: List<Drink>)
    @Update
    suspend fun updateDrink(drink: Drink)
    @Delete
    suspend fun deleteDrink(drink: Drink)
}
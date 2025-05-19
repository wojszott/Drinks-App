package com.example.drinks.database
import androidx.room.*

@Entity
data class Drink(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0, // unikalne ID (auto-generowane)
    val name: String,
    val description: String,
    val ingredients: String,
    val preparation: String,
    val imageResId: Int,
    val type: String
)

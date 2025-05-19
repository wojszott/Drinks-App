package com.example.drinks

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.drinks.database.Drink
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.example.drinks.database.UserViewModel

@Composable
fun Screen1(navController: NavController,viewModel: MainViewModel,viewModel2: UserViewModel) {

    val context = LocalContext.current
    val swipeThreshold = 100f
    var offsetX by remember { mutableStateOf(0f) }
    var swipe by remember { mutableIntStateOf(0) }
    val drinkList by viewModel2.drinks.observeAsState(emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        offsetX += dragAmount
                    },
                    onDragEnd = {
                        if (offsetX > swipeThreshold) {
                            swipe = -1
                        }
                        else if (offsetX < -swipeThreshold){
                            swipe = 1
                        }
                        offsetX = 0f
                    }
                )
            }
    ){
        DrinkListScreen(
            drinks = drinkList,
            onDrinkClick = { drink ->
                viewModel.selectDrink(drink)
                navController.navigate("screen2")
                Toast.makeText(context, "Wybrałeś: ${drink.name}", Toast.LENGTH_SHORT).show()
            },
            viewModel,
            swipe,
            onSwipeConsumed = { swipe = 0 }
        )
    }
}

@Composable
fun DrinkListScreen(
    drinks: List<Drink>,
    onDrinkClick: (Drink) -> Unit,
    viewModel: MainViewModel,
    swipe: Int,
    onSwipeConsumed: () -> Unit
) {

    val configuration = LocalConfiguration.current
    val isTablet = configuration.smallestScreenWidthDp >= 600
    val screenWidth = configuration.screenWidthDp

    val columns = when {
        screenWidth >= 840 -> 4
        screenWidth >= 600 -> 3
        else -> 2
    }

    val dPadding = if (isTablet) 24.dp else 16.dp
    val titleFontSize = if (isTablet) 24.sp else 20.sp



    var selectedCategoryIndex by remember { mutableStateOf(0) }
    val categories = listOf("All") + drinks.map { it.type }.distinct()
    val selectedCategory = categories[selectedCategoryIndex]

    // Filtrujemy drinki na podstawie wybranego typu
    val filteredDrinks = if (selectedCategory == "All") {
        drinks
    } else {
        drinks.filter { it.type == selectedCategory }
    }

    if(swipe<0){
        if (selectedCategoryIndex > 0){
            selectedCategoryIndex--
            onSwipeConsumed()
        }
    } else if (swipe>0){
        if (selectedCategoryIndex < categories.lastIndex) {
            selectedCategoryIndex++
            onSwipeConsumed()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pasek z kategoriami
        Box(
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .align(Alignment.Center)
                    .wrapContentWidth()
            ) {
                items(categories.size) { index ->
                    val category = categories[index]
                    Button(
                        onClick = { selectedCategoryIndex = index },
                        enabled = selectedCategoryIndex != index,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text(category,fontSize = titleFontSize)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(dPadding))
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(dPadding),
            horizontalArrangement = Arrangement.spacedBy(dPadding)
        ) {
            items(filteredDrinks) { drink ->
                DrinkCard(drink = drink, onClick = { onDrinkClick(drink) },dPadding,titleFontSize)
            }
        }
    }
}

@Composable
fun DrinkCard(drink: Drink, onClick: () -> Unit, dPadding: Dp, titleFontSize: TextUnit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),

        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageRes = drink.imageResId.takeIf { it != 0 } ?: R.drawable.drinkfill // Fallback
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = drink.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
            ){
                Text(
                    text = drink.name,
                    color = MaterialTheme.colorScheme.background,
                    fontSize = titleFontSize,
                    modifier = Modifier.padding(dPadding),
                )
            }

        }
    }
}

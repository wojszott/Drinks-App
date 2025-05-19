package com.example.drinks

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen2(navController: NavController, viewModel: MainViewModel) {
    val swipeThreshold = 100f
    var offsetX by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        offsetX += dragAmount
                    },
                    onDragEnd = {
                        if (offsetX > swipeThreshold || offsetX < -swipeThreshold) {
                            navController.popBackStack()
                        }
                        offsetX = 0f
                    }
                )
            }
    ) {
        DrinkDetails(navController, viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinkDetails(navController: NavController, viewModel: MainViewModel) {
    val drink = viewModel.selectedDrink
    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val scrollState = rememberScrollState()

    val configuration = LocalConfiguration.current
    val isTablet = configuration.smallestScreenWidthDp >= 600

    val dPadding = if (isTablet) 24.dp else 16.dp
    val titleFontSize = if (isTablet) 42.sp else 20.sp
    val contentFontSize = if (isTablet) 32.sp else 16.sp
    val imageHeight = if (isTablet) 360.dp else 240.dp
    val bigFontSize = if (isTablet) 48.sp else 40.sp

    val imageRes = drink?.imageResId?.takeIf { it != 0 } ?: R.drawable.drinkfill
    //Log.d("DrinkCard", "Drink: ${drink?.name}, imageResId: ${drink?.imageResId}")

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = drink?.name ?: "Szczegóły", fontSize = titleFontSize)
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            if (drink != null) {
                FloatingActionButton(
                    onClick = {
                        val ingredientsText = drink.ingredients
                        Toast.makeText(
                            context,
                            "Wysłano SMS: $ingredientsText",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Text("SMS", fontSize = contentFontSize)
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = drink?.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                )

                if (drink != null) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Składniki:", fontSize = titleFontSize, modifier = Modifier.padding(dPadding))
                    Text(text = drink.ingredients, fontSize = contentFontSize, modifier = Modifier.padding(horizontal = dPadding))

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Sposób przygotowania:", fontSize = titleFontSize, modifier = Modifier.padding(dPadding))
                    Text(text = drink.preparation, fontSize = contentFontSize, modifier = Modifier.padding(horizontal = dPadding))

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = viewModel.current.value.toString(),
                        fontSize = bigFontSize,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(dPadding)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dPadding),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { viewModel.stoperHandler() }) {
                            Text(if (viewModel.hasStarted.value) "Reset" else "Start", fontSize = contentFontSize)
                        }

                        Button(onClick = { viewModel.pauseOrResumeCountdown() }) {
                            Text(if (viewModel.isPaused.value) "Wznów" else "Pauza", fontSize = contentFontSize)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Back to list", fontSize = contentFontSize)
                    }

                    Spacer(modifier = Modifier.height(300.dp))
                } else {
                    Text("Brak danych o drinku", fontSize = contentFontSize, modifier = Modifier.padding(dPadding))
                }
            }
        }
    }
}

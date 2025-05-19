package com.example.drinks

import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.drinks.ui.theme.DrinkTheme
import kotlinx.coroutines.delay
import com.example.drinks.database.UserViewModel
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import kotlinx.coroutines.launch
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.getInsetsController(window,window.decorView).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
        }
        setContent {
            DrinkTheme {
                Main()
            }
        }
    }
}

@Composable
fun Main() {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    val viewModel: MainViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showSplash by remember { mutableStateOf(true) }
    val drinkList by userViewModel.drinks.observeAsState(emptyList())

    val titleFontSize = if (isTablet) 64.sp else 28.sp
    val shelfFontSize = if (isTablet) 42.sp else 22.sp
    val buttonFontSize = if (isTablet) 42.sp else 16.sp
    val dPadding = if (isTablet) 12.dp else 8.dp

    val screenWidth = configuration.screenWidthDp.dp
    val drawerWidth = if (screenWidth > 600.dp) screenWidth * 0.5f else 280.dp

    // Animacja startowa - wyświetlanie splash screen
    LaunchedEffect(Unit) {
        delay(3000)
        showSplash = false
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(drawerWidth),
                drawerContainerColor = MaterialTheme.colorScheme.primary
            ) {
                Card (
                    modifier = Modifier
                        .padding(dPadding),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                    ),
                ) {
                    Text(
                        "Nawigacja", modifier = Modifier.padding(dPadding),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = titleFontSize
                    )
                }
                Spacer(Modifier.height(8.dp))

                DrawerItem("Strona główna",dPadding,shelfFontSize) {
                    navController.navigate("screen1")
                    scope.launch { drawerState.close() }
                }
                DrawerItem("Losowy Drink",dPadding,shelfFontSize) {
                    if (drinkList.isNotEmpty()) {
                        val randomDrink = drinkList[Random.nextInt(drinkList.size)]
                        viewModel.selectDrink(randomDrink)
                        navController.navigate("screen2")
                    }
                    scope.launch { drawerState.close() }
                }
                DrawerItem("Ustawienia",dPadding,shelfFontSize) {
                    navController.navigate("settings")
                    scope.launch { drawerState.close() }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(dPadding),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "☰", // Ikona menu
                        modifier = Modifier
                            .clickable {
                                scope.launch { drawerState.open() }
                            }
                            .padding(horizontal = dPadding),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = titleFontSize
                    )
                    Text(
                        text = "Drink App 🍹",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = titleFontSize
                    )
                    Button(
                        onClick = { navController.navigate("settings") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Settings", fontSize = buttonFontSize)
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    NavHost(navController = navController, startDestination = "splash") {
                        composable("splash") { SplashScreen(navController) }
                        composable("screen1") { Screen1(navController, viewModel, userViewModel) }
                        composable("screen2") { Screen2(navController, viewModel) }
                        composable("settings") { Settings(navController) }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerItem(text: String,dPadding: Dp,fontSize: TextUnit, onClick: () -> Unit) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(dPadding),
            colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        ),
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(dPadding),
            fontSize = fontSize,
            color = MaterialTheme.colorScheme.background,
        )
    }
}


@Composable
fun Settings(navController: NavController){

    val configuration = LocalConfiguration.current
    val isTablet = configuration.smallestScreenWidthDp >= 600


    val titleFontSize = if (isTablet) 64.sp else 28.sp
    val buttonFontSize = if (isTablet) 32.sp else 16.sp
    val dFontSize = if (isTablet) 32.sp else 22.sp
    val dPadding = if (isTablet) 12.dp else 8.dp

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
        }){
        Column {
            Spacer(modifier = Modifier.height(dPadding))
            Text(
                text = "Ustawienia",
                color = Color.DarkGray,
                fontSize = titleFontSize,
                modifier = Modifier.padding(dPadding)
            )
            Spacer(modifier = Modifier.height(dPadding))
            Text(
                text = "Projekt Drink Maker własności Spitree 2025",
                color = Color.Black,
                fontSize = dFontSize,
                modifier = Modifier.padding(dPadding)
            )
            Spacer(modifier = Modifier.height(dPadding))
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = dPadding)
            ) {
                Text("Back to list",fontSize = buttonFontSize)
            }
        }
    }
}

@Composable
fun DrinkSplashAnimation() {
    AndroidView(
        modifier = Modifier.size(150.dp),
        factory = { ctx ->
            ImageView(ctx).apply {
                setImageResource(R.drawable.drinkfill)
                ObjectAnimator.ofFloat(this, "rotation", 0f, 360f).apply {
                    duration = 2000
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.RESTART
                    start()
                }
            }
        }
    )
}

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(3000) // Czas trwania splash screenu
        navController.navigate("screen1") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        DrinkSplashAnimation()
    }
}


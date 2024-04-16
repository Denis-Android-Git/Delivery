package com.example.delivery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.delivery.screens.AnimationScreen
import com.example.delivery.screens.BasketScreen
import com.example.delivery.screens.MainScreen
import com.example.delivery.screens.SearchScreen
import com.example.delivery.ui.theme.DeliveryTheme
import com.example.delivery.viewmodel.ProductsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeliveryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KoinContext {

                        val navController = rememberNavController()

                        val productsViewModel = koinViewModel<ProductsViewModel>()

                        NavHost(navController = navController, startDestination = "mainScreen") {
                            composable(route = "mainScreen") {
                                MainScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    navController = navController,
                                    productsViewModel = productsViewModel
                                )
                            }
                            composable(route = "basketScreen") {
                                BasketScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    navController = navController
                                )
                            }
                            composable(route = "animation") {
                                AnimationScreen(
                                    modifier = Modifier.padding(innerPadding),
                                )
                            }
                            composable(route = "search") {
                                SearchScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    productsViewModel = productsViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
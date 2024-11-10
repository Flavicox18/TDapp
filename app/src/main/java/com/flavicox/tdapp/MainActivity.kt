package com.flavicox.tdapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.flavicox.tdapp.ui.theme.TDappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TDappTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "search") {
        composable("search") { SearchPackageScreen(navController) }

        composable(
            "details/{id_encomienda}",
            arguments = listOf(navArgument("id_encomienda") { type = NavType.IntType })
        ) { backStackEntry ->
            val id_encomienda = backStackEntry.arguments?.getInt("id_encomienda") ?: 0
            ShipmentDetailsScreen(id_encomienda = id_encomienda)
            }
        }
}

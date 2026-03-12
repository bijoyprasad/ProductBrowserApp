package com.bijoy.productbrowser.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.bijoy.productbrowser.presentation.navigation.ProductDetailsRoute
import com.bijoy.productbrowser.presentation.navigation.ProductListRoute
import com.bijoy.productbrowser.presentation.productdetails.ProductDetailScreen
import com.bijoy.productbrowser.presentation.productlist.ProductListScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ProductListRoute
    ) {
        composable<ProductListRoute> {
            ProductListScreen(
                onItemClick = { id ->
                    navController.navigate(ProductDetailsRoute(id))
                }
            )
        }
        composable<ProductDetailsRoute> {
            val route = it.toRoute<ProductDetailsRoute>()
            ProductDetailScreen(
                onBack = {
                    navController.popBackStack()
                },
                productId = route.id,
            )
        }
    }
}
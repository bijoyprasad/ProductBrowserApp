package com.bijoy.productbrowser.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object ProductListRoute

@Serializable
data class ProductDetailsRoute(val id: Int)
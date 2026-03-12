package com.bijoy.productbrowser.presentation.util

fun Double.toPrice(): String {
    val rounded = kotlin.math.round(this * 100) / 100
    val parts = rounded.toString().split(".")

    val decimal = if (parts.size > 1) parts[1].padEnd(2, '0').take(2) else "00"

    return "$${parts[0]}.$decimal"
}
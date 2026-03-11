package com.bijoy.productbrowser.domain.repository

import com.bijoy.productbrowser.domain.model.Product

interface
ProductRepository {

    /*
    * You can pass limit and skip params to limit and skip the results for pagination
    * use limit=0 to get all items.
    * From api docs
    * */
    suspend fun getAllProducts(limit: Int = 30, skip: Int = 0): List<Product>

    suspend fun getProductById(id: Int): Product

    suspend fun searchProducts(query: String): List<Product>

}
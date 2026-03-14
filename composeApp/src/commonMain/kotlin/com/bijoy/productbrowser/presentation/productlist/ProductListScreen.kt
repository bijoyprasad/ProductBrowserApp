package com.bijoy.productbrowser.presentation.productlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bijoy.productbrowser.presentation.ui.EmptyView
import com.bijoy.productbrowser.presentation.ui.ErrorView
import com.bijoy.productbrowser.presentation.ui.LoadingView
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onItemClick: (Int) -> Unit = {},
    viewModel: ProductListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = "Products",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = state.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                onClear = viewModel::clearSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            AnimatedVisibility(
                visible = !state.isLoading && state.availableCategories.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CategoryFilterRow(
                    categories = state.availableCategories,
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = { category ->
                        if (category == null) viewModel.clearCategory()
                        else viewModel.selectCategory(category)
                    }
                )
            }

            AnimatedVisibility(
                visible = state.isSearchActive && state.displayedProducts.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                val count = state.displayedProducts.size
                val categoryNote = state.selectedCategory?.let { " in \"$it\"" } ?: ""
                Text(
                    text = "$count result${if (count != 1) "s" else ""} for \"${state.searchQuery}\"$categoryNote",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            when {
                state.isLoading -> LoadingView()

                state.error != null && !state.isSearchActive -> ErrorView(
                    message = state.error!!,
                    onRetry = viewModel::loadProducts
                )

                state.isSearchLoading -> LoadingView()

                state.displayedProducts.isEmpty() && state.isSearchActive -> {
                    val msg = buildString {
                        append("No results for \"${state.searchQuery}\"")
                        state.selectedCategory?.let { append(" in category \"$it\"") }
                    }
                    EmptyView(message = msg)
                }

                state.displayedProducts.isEmpty() && state.selectedCategory != null ->
                    EmptyView(message = "No products in \"${state.selectedCategory}\"")

                state.displayedProducts.isEmpty() -> EmptyView()

                else -> {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Adaptive(160.dp),
                        contentPadding = PaddingValues(8.dp),
                        verticalItemSpacing = 8.dp,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.displayedProducts, key = { it.id }) { product ->
                            ProductCard(
                                product = product,
                                onClick = { onItemClick(product.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

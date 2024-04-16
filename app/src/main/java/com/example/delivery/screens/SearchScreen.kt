package com.example.delivery.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.delivery.R
import com.example.delivery.viewmodel.DbViewModel
import com.example.delivery.viewmodel.ProductsViewModel
import com.example.domain.model.Product
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier,
    productsViewModel: ProductsViewModel,
    dbViewModel: DbViewModel = koinViewModel()
) {
    val searchText by productsViewModel.searchText.collectAsState()
    val isSearching by productsViewModel.isSearching.collectAsState()
    val productList by productsViewModel.searchList.collectAsState()
    var showList by remember {
        mutableStateOf(true)
    }

    var showDetails by remember {
        mutableStateOf(true)
    }
    var productToHandle by remember {
        mutableStateOf<Product?>(null)
    }
    val dbList = dbViewModel.list.collectAsState()

    Scaffold(
        modifier = Modifier,
        topBar = {
            SearchBar(
                query = searchText,
                onQueryChange = productsViewModel::onSearchTextChange,
                onSearch = productsViewModel::onSearchTextChange,
                active = isSearching,
                onActiveChange = { productsViewModel.onToogleSearch() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (isSearching) 0.dp else 16.dp),
                colors = SearchBarDefaults.colors(
                    containerColor = Color.White,
                    dividerColor = Color.Gray
                ),
                leadingIcon = {
                    AnimatedVisibility(isSearching) {
                        IconButton(onClick = {
                            productsViewModel.onToogleSearch()
                        }) {
                            Image(
                                painter = painterResource(id = R.drawable.arrowback),
                                contentDescription = null
                            )
                        }
                    }
                },
                trailingIcon = {
                    AnimatedVisibility(isSearching) {

                        IconButton(onClick = {
                            productsViewModel.onSearchTextChange("")
                        }) {
                            Image(
                                painter = painterResource(id = R.drawable.cancel),
                                contentDescription = null
                            )
                        }
                    }
                },
                placeholder = {
                    Text(text = stringResource(R.string.find_food))
                }
            ) {
                AnimatedVisibility(visible = showList && productList.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = modifier
                            .fillMaxSize()
                    ) {
                        items(productList,
                            key = {
                                it.id
                            }) { product ->
                            ProductItem(
                                item = product,
                                modifier = Modifier.animateItemPlacement(
                                    spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                ),
                                dbViewModel = dbViewModel,
                                onClick = {
                                    productToHandle = product
                                    showList = false
                                    showDetails = true

                                }
                            )
                        }
                    }
                }
                AnimatedVisibility(visible = productList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = stringResource(id = R.string.nothing),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                AnimatedVisibility(visible = showDetails) {
                    productToHandle?.let {
                        val foundItem = dbList.value.find { productFromDb ->
                            productFromDb.id == productToHandle!!.id
                        }

                        DetailScreen(
                            modifier = Modifier.navigationBarsPadding(),
                            item = it,
                            onButtonClick = {
                                if (foundItem == null) {
                                    dbViewModel.upsert(
                                        count = 1,
                                        id = productToHandle!!.id,
                                        price = productToHandle!!.price_current,
                                        oldPrice = productToHandle!!.price_old,
                                        name = productToHandle!!.name,
                                        image = productToHandle!!.image
                                    )
                                } else {
                                    dbViewModel.plusCount(foundItem)
                                }
                                showDetails = false
                                showList = true
                            }
                        ) {
                            showList = true
                            showDetails = false
                        }
                    }
                }
            }
        }
    ) {
    }
}

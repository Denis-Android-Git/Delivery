package com.example.delivery.screens

import android.app.Application
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.data.states.States
import com.example.delivery.R
import com.example.delivery.viewmodel.DbViewModel
import com.example.delivery.viewmodel.ProductsViewModel
import com.example.domain.model.Product
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    modifier: Modifier,
    navController: NavController,
    productsViewModel: ProductsViewModel = koinViewModel(),
    dbViewModel: DbViewModel = koinViewModel()
) {
    val dbList = dbViewModel.list.collectAsState()

    val sum = dbList.value.sumOf {
        it.price * it.count
    }

    var showList by remember {
        mutableStateOf(true)
    }

    var showDetails by remember {
        mutableStateOf(true)
    }

    var productToHandle by remember {
        mutableStateOf<Product?>(null)
    }
    val categories = productsViewModel.categories.collectAsState()
    val tags = productsViewModel.tags.collectAsState()
    val state = productsViewModel.states.collectAsState()
    var tabIndex by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val selectedOptions = remember { mutableStateOf(listOf<Int>()) }
    val context = LocalContext.current
    var showSearchDialog by remember {
        mutableStateOf(false)
    }
    var search by remember {
        mutableStateOf("")
    }

    if (categories.value.isNotEmpty()) {
        LaunchedEffect(Unit) {
            productsViewModel.selectCategory(categories.value, tabIndex)
        }
    }

    AnimatedVisibility(visible = showList) {
        if (showSearchDialog) {
            BasicAlertDialog(onDismissRequest = { showSearchDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        TextField(value = search, onValueChange = {
                            search = it
                        })
                        Button(
                            onClick = {
                                productsViewModel.search(
                                    search,
                                    context.applicationContext as Application
                                )
                                showSearchDialog = false
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .padding(16.dp)
                                .width(343.dp)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF15412)
                            )
                        ) {
                            Text(text = stringResource(R.string.search))
                        }
                    }
                }
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.choose),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(
                        Font(R.font.roboto)
                    ),
                    fontWeight = FontWeight.Bold
                )

                tags.value.forEach { tag ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = tag.name,
                            modifier = Modifier
                                .padding(start = 16.dp, top = 3.dp, bottom = 6.dp)
                                .align(Alignment.CenterStart),
                            fontSize = 16.sp,
                            fontFamily = FontFamily(
                                Font(R.font.roboto)
                            )
                        )
                        Checkbox(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            checked = selectedOptions.value.contains(tag.id),
                            onCheckedChange = { selected ->
                                val currentSelected = selectedOptions.value.toMutableList()
                                if (selected) {
                                    currentSelected.add(tag.id)
                                } else {
                                    currentSelected.remove(tag.id)
                                }
                                selectedOptions.value = currentSelected
                            },
                            colors = CheckboxColors(
                                checkedBorderColor = Color(0xFFF15412),
                                checkedBoxColor = Color(0xFFF15412),
                                checkedCheckmarkColor = Color.White,
                                disabledBorderColor = Color.Transparent,
                                disabledCheckedBoxColor = Color.Transparent,
                                disabledIndeterminateBorderColor = Color.Transparent,
                                disabledUncheckedBoxColor = Color.Transparent,
                                disabledUncheckedBorderColor = Color.Transparent,
                                disabledIndeterminateBoxColor = Color.Transparent,
                                uncheckedBorderColor = Color.Gray,
                                uncheckedBoxColor = Color.Transparent,
                                uncheckedCheckmarkColor = Color.Transparent
                            )
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                }
                Button(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(343.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF15412)
                    ),
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                        productsViewModel.selectTag(
                            categories.value,
                            tabIndex,
                            selectedOptions.value,
                            context.applicationContext as Application
                        )
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                    title = {
                        Row {
                            Box {
                                if (selectedOptions.value.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .width(22.dp)
                                            .height(22.dp)
                                            .background(
                                                color = Color(0xFFF15412),
                                                shape = RoundedCornerShape(50.dp)
                                            ),
                                    ) {
                                        Text(
                                            lineHeight = 22.sp,
                                            text = selectedOptions.value.size.toString(),
                                            fontSize = 10.sp,
                                            color = Color.White,
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        showBottomSheet = true
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.indicator_off),
                                        contentDescription = null
                                    )
                                }
                            }
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = null,
                            )
                            IconButton(
                                onClick = {
                                    showSearchDialog = true
                                },
                                Modifier
                                    .padding(end = 16.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Search, contentDescription = null
                                )
                            }
                        }
                    })

                ScrollableTabRow(
                    modifier = Modifier.padding(top = 6.dp),
                    edgePadding = 1.dp,
                    selectedTabIndex = tabIndex,
                    divider = {},
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            color = Color.Transparent
                        )
                    },
                ) {
                    categories.value.forEachIndexed { index, category ->
                        val color = remember {
                            Animatable(Color(0xFFF15412))
                        }
                        scope.launch {
                            color.animateTo(
                                if (tabIndex == index) {
                                    Color(0xFFF15412)
                                } else {
                                    Color.White
                                }
                            )
                        }
                        val tabModifier = Modifier
                            .background(color = color.value, shape = RoundedCornerShape(8.dp))
                            .height(40.dp)
                        Tab(
                            text = {
                                Text(
                                    category.name, style = if (tabIndex == index) {
                                        TextStyle(
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontFamily = FontFamily(
                                                Font(R.font.roboto)
                                            )
                                        )
                                    } else {
                                        TextStyle(
                                            color = Color.Black,
                                            fontSize = 16.sp,
                                            fontFamily = FontFamily(
                                                Font(R.font.roboto)
                                            )
                                        )
                                    }
                                )
                            },
                            selected = tabIndex == index,
                            onClick = {
                                tabIndex = index
                                productsViewModel.selectCategory(categories.value, index)
                                selectedOptions.value = emptyList()
                            },
                            modifier = tabModifier
                        )
                    }
                }

                when (val currentState = state.value) {
                    is States.Error -> {
                        Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            currentState.error?.let {
                                Text(
                                    text = it,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(16.dp),
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(
                                        Font(R.font.roboto)
                                    )
                                )
                            }
                        }
                    }

                    States.Loading -> {
                        Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is States.Success -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = if (dbList.value.isEmpty()) 0.dp else 65.dp)
                        ) {
                            items(currentState.list,
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
                                    })
                            }
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = dbList.value.isNotEmpty(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Button(
                    onClick = {
                        navController.navigate("basketScreen")
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .width(343.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF15412)
                    )
                ) {

                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.cart),
                            contentDescription = null,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                        Text(
                            text = stringResource(R.string.sum, sum / 100),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto))
                        )
                    }
                }
            }
        }
    }

    AnimatedVisibility(visible = showDetails) {
        productToHandle?.let {
            val foundItem = dbList.value.find { productFromDb ->
                productFromDb.id == productToHandle!!.id
            }

            DetailScreen(
                modifier = modifier,
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
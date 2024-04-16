package com.example.delivery.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.states.States
import com.example.delivery.R
import com.example.domain.model.Category
import com.example.domain.model.Product
import com.example.domain.usecase.CategoryListUseCase
import com.example.domain.usecase.GetTagsUseCase
import com.example.domain.usecase.ProductListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val productListUseCase: ProductListUseCase,
    private val categoryListUseCase: CategoryListUseCase,
    private val tagsUseCase: GetTagsUseCase
) : ViewModel() {

    private var _states = MutableStateFlow<States>(States.Loading)
    val states = _states.asStateFlow()

    private var _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    private var _products = MutableStateFlow<List<Product>>(emptyList())
    private val products = _products.asStateFlow()

    private var _tags = MutableStateFlow<List<Category>>(emptyList())
    val tags = _tags.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    val searchList = searchText
        .combine(_products) { text, products ->
            products.filter { product ->
                product.description.contains(text)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _products.value
        )

    init {
        viewModelScope.launch {
            try {
                _categories.value = categoryListUseCase.execute()
                _tags.value = tagsUseCase.execute()
                _products.value = productListUseCase.execute()
            } catch (e: Exception) {
                _states.value = States.Error(e.message)
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToogleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    fun selectCategory(list: List<Category>, index: Int) {
        _states.value = States.Loading
        viewModelScope.launch {
            try {
                val tab = list[index]
                products.collect { list ->
                    val filteredList = list.filter {
                        it.category_id == tab.id
                    }
                    _states.value = States.Success(filteredList)
                }
            } catch (e: Exception) {
                _states.value = States.Error(e.message)
            }
        }
    }

    fun selectTag(list: List<Category>, index: Int, tags: List<Int>, application: Application) {
        _states.value = States.Loading
        viewModelScope.launch {
            try {
                val tab = list[index]
                products.collect { list ->
                    val tabList = list.filter {
                        it.category_id == tab.id
                    }
                    val tagList = tabList.filter {
                        it.tag_ids.containsAll(tags)
                    }
                    if (tagList.isEmpty()) {
                        _states.value = States.Error(application.getString(R.string.no_filter))
                    } else {
                        _states.value = States.Success(tagList)
                    }
                }
            } catch (e: Exception) {
                _states.value = States.Error(e.message)
            }
        }
    }
}
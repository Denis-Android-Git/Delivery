package com.example.delivery.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.states.States
import com.example.delivery.R
import com.example.domain.model.Category
import com.example.domain.usecase.CategoryListUseCase
import com.example.domain.usecase.GetTagsUseCase
import com.example.domain.usecase.ProductListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private var _tags = MutableStateFlow<List<Category>>(emptyList())
    val tags = _tags.asStateFlow()

    init {
        viewModelScope.launch {
            _categories.value = categoryListUseCase.execute()
            _tags.value = tagsUseCase.execute()
        }
    }

    fun selectCategory(list: List<Category>, index: Int) {
        _states.value = States.Loading
        viewModelScope.launch {
            try {
                val tab = list[index]
                val filteredList = productListUseCase.execute().filter {
                    it.category_id == tab.id
                }
                _states.value = States.Success(filteredList)
            } catch (e: Exception) {
                _states.value = States.Error(e.message)
            }
        }
    }

    fun search(query: String, application: Application) {
        _states.value = States.Loading
        viewModelScope.launch {
            try {
                val list = productListUseCase.execute()
                val result = list.filter {
                    it.description.contains(query)
                }
                if (result.isEmpty()) {
                    _states.value = States.Error(application.getString(R.string.nothing))
                } else {
                    _states.value = States.Success(result)
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
                val tabList = productListUseCase.execute().filter {
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
            } catch (e: Exception) {
                _states.value = States.Error(e.message)
            }
        }
    }
}
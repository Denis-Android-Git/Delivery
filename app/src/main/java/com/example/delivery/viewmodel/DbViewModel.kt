package com.example.delivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ProductFromDb
import com.example.domain.usecase.DbListUseCase
import com.example.domain.usecase.DeleteUseCase
import com.example.domain.usecase.UpsertItemUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DbViewModel(
    private val upsertItemUseCase: UpsertItemUseCase,
    private val dbListUseCase: DbListUseCase,
    private val deleteUseCase: DeleteUseCase
) : ViewModel() {
    val list = this.dbListUseCase.execute()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun upsert(
        count: Int,
        id: Int,
        price: Int,
        oldPrice: Int?,
        name: String,
        image: String
    ) {
        viewModelScope.launch {
            val item = ProductFromDb(
                count = count,
                id = id,
                name = name,
                price = price,
                oldPrice = oldPrice,
                image = image
            )
            upsertItemUseCase.execute(item)
        }
    }

    fun plusCount(item: ProductFromDb) {
        viewModelScope.launch {
            val updateItem = ProductFromDb(
                count = item.count + 1,
                id = item.id,
                name = item.name,
                price = item.price,
                oldPrice = item.oldPrice,
                image = item.image
            )
            upsertItemUseCase.execute(updateItem)
        }
    }

    fun minusCount(item: ProductFromDb) {
        viewModelScope.launch {
            val updateItem = ProductFromDb(
                count = item.count - 1,
                id = item.id,
                name = item.name,
                price = item.price,
                oldPrice = item.oldPrice,
                image = item.image
            )
            upsertItemUseCase.execute(updateItem)
            if (item.count == 1) {
                deleteUseCase.execute(item)
            }
        }
    }
}
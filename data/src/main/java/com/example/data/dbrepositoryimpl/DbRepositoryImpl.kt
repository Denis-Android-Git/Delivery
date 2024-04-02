package com.example.data.dbrepositoryimpl

import com.example.data.database.MyDataBase
import com.example.domain.model.ProductFromDb
import com.example.domain.repository.DbRepository
import kotlinx.coroutines.flow.Flow

class DbRepositoryImpl(
    private val db: MyDataBase
) : DbRepository {
    override suspend fun upsertItem(item: ProductFromDb) {
        db.itemDao().upsertItem(item)
    }

    override fun getItemList(): Flow<List<ProductFromDb>> {
        return db.itemDao().getItemList()
    }

    override suspend fun delete(item: ProductFromDb) {
        db.itemDao().delete(item)
    }
}
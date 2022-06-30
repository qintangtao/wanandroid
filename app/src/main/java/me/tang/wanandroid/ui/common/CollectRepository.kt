package me.tang.wanandroid.ui.common

import me.tang.mvvm.base.BaseModel
import me.tang.wanandroid.model.api.ApiRetrofit

class CollectRepository private constructor(
    private val remote: ApiRetrofit
// private val local: ApiRoom
) : BaseModel() {

    suspend fun collect(id: Int) = remote.service.collect(id)
    suspend fun uncollect(id: Int) = remote.service.uncollect(id)

    companion object {
        @Volatile
        private var INSTANCE: CollectRepository? = null

        fun getInstance(remote: ApiRetrofit) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: CollectRepository(remote).also { INSTANCE = it }
            }
    }
}
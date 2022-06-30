package me.tang.wanandroid.ui.main.navigation

import me.tang.mvvm.base.BaseModel
import me.tang.wanandroid.model.api.ApiRetrofit

class NavigationRepository private constructor(
    private val remote: ApiRetrofit
// private val local: ApiRoom
) : BaseModel() {

    suspend fun getNavigations() = remote.service.getNavigations()


    companion object {

        @Volatile
        private var INSTANCE: NavigationRepository? = null

        fun getInstance(remote: ApiRetrofit) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NavigationRepository(remote).also { INSTANCE = it }
            }
    }
}
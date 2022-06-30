package me.tang.wanandroid.model.api

import me.tang.mvvm.utils.RetrofitClient


class ApiRetrofit {

    val service by lazy { RetrofitClient.getInstance().create(ApiService.BASE_URL, ApiService::class.java) }

    companion object {
        @Volatile
        private var INSTANCE: ApiRetrofit? = null

        fun getInstance() = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ApiRetrofit().also { INSTANCE = it }
        }
    }
}
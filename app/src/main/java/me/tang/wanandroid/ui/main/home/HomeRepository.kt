package me.tang.wanandroid.ui.main.home

import me.tang.mvvm.base.BaseModel
import me.tang.wanandroid.model.api.ApiRetrofit

class HomeRepository private constructor(
    private val remote: ApiRetrofit
// private val local: ApiRoom
) : BaseModel() {

    suspend fun getTopArticleList() = remote.service.getTopArticleList()
    suspend fun getArticleList(page: Int) = remote.service.getArticleList(page)
    suspend fun getProjectList(page: Int) = remote.service.getProjectList(page)
    suspend fun getUserArticleList(page: Int) = remote.service.getUserArticleList(page)
    suspend fun getProjectCategories() = remote.service.getProjectCategories()
    suspend fun getProjectListByCid(page: Int, cid: Int) = remote.service.getProjectListByCid(page, cid)
    suspend fun getWechatCategories() = remote.service.getWechatCategories()
    suspend fun getWechatArticleList(page: Int, id: Int) = remote.service.getWechatArticleList(page, id)

    companion object {

        @Volatile
        private var INSTANCE: HomeRepository? = null

        fun getInstance(remote: ApiRetrofit) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeRepository(remote).also { INSTANCE = it }
            }
    }
}
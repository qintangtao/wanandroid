package me.tang.wanandroid.model.api

import me.tang.wanandroid.model.bean.*
import me.tang.mvvm.base.BaseResult
import retrofit2.http.*


interface ApiService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }

    @GET("/article/listproject/{page}/json")
    suspend fun getProjectList(@Path("page") page: Int): BaseResult<Pagination<Article>>

    @GET("/article/top/json")
    suspend fun getTopArticleList(): BaseResult<MutableList<Article>>

    @GET("/article/list/{page}/json")
    suspend fun getArticleList(@Path("page") page: Int): BaseResult<Pagination<Article>>

    @GET("/user_article/list/{page}/json")
    suspend fun getUserArticleList(@Path("page") page: Int): BaseResult<Pagination<Article>>

    @GET("tree/json")
    suspend fun getArticleCategories(): BaseResult<MutableList<Category>>

    @GET("article/list/{page}/json")
    suspend fun getArticleListByCid(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): BaseResult<Pagination<Article>>

    @GET("project/tree/json")
    suspend fun getProjectCategories(): BaseResult<MutableList<Category>>

    @GET("project/list/{page}/json")
    suspend fun getProjectListByCid(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): BaseResult<Pagination<Article>>

    @GET("wxarticle/chapters/json")
    suspend fun getWechatCategories(): BaseResult<MutableList<Category>>

    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getWechatArticleList(
        @Path("page") page: Int,
        @Path("id") id: Int
    ): BaseResult<Pagination<Article>>

    @GET("navi/json")
    suspend fun getNavigations(): BaseResult<MutableList<Navigation>>

    @GET("banner/json")
    suspend fun getBanners(): BaseResult<MutableList<Banner>>

    @GET("hotkey/json")
    suspend fun getHotWords(): BaseResult<MutableList<HotWord>>

    @GET("friend/json")
    suspend fun getFrequentlyWebsites(): BaseResult<MutableList<Frequently>>

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): BaseResult<UserInfo>

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): BaseResult<UserInfo>

    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int): BaseResult<Any?>

    @POST("lg/uncollect_originId/{id}/json")
    suspend fun uncollect(@Path("id") id: Int): BaseResult<Any?>

    @FormUrlEncoded
    @POST("article/query/{page}/json")
    suspend fun search(
        @Field("k") keywords: String,
        @Path("page") page: Int
    ): BaseResult<Pagination<Article>>

    @FormUrlEncoded
    @POST("lg/user_article/add/json")
    suspend fun shareArticle(
        @Field("title") title: String,
        @Field("link") link: String
    ): BaseResult<Any>

    @GET("lg/coin/userinfo/json")
    suspend fun getPoints(): BaseResult<PointRank>

    @GET("lg/coin/list/{page}/json")
    suspend fun getPointsRecord(@Path("page") page: Int): BaseResult<Pagination<PointRecord>>

    @GET("coin/rank/{page}/json")
    suspend fun getPointsRank(@Path("page") page: Int): BaseResult<Pagination<PointRank>>

    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectionList(@Path("page") page: Int): BaseResult<Pagination<Article>>

    @GET("user/lg/private_articles/{page}/json")
    suspend fun getSharedArticleList(@Path("page") page: Int): BaseResult<Shared>

    @POST("lg/user_article/delete/{id}/json")
    suspend fun deleteShare(@Path("id") id: Int): BaseResult<Any>
}
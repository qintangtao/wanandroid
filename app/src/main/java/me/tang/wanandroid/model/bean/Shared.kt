package me.tang.wanandroid.model.bean

data class Shared(
    val coinInfo: PointRank,
    val shareArticles: Pagination<Article>
)
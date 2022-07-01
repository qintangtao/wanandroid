package me.tang.wanandroid.ui.main.home.popular

import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tang.mvvm.base.BaseViewModel
import me.tang.mvvm.base.OnItemClickListener
import me.tang.mvvm.network.RESULT
import me.tang.mvvm.BR
import me.tang.wanandroid.R
import me.tang.wanandroid.model.api.ApiRetrofit
import me.tang.wanandroid.model.bean.Article
import me.tang.wanandroid.model.bean.Pagination
import me.tang.wanandroid.ui.detail.DetailActivity
import me.tang.wanandroid.ui.main.home.HomeRepository
import me.tatarka.bindingcollectionadapter2.ItemBinding

class PopularViewModel : BaseViewModel() {
    private val repository by lazy { HomeRepository.getInstance(ApiRetrofit.getInstance()) }

    private val itemOnClickListener = object : OnItemClickListener<Article> {
        override fun onClick(view: View, item: Article) {
            when(view.id) {
                R.id.iv_collect -> {
                    //item.collect = !item.collect
                    //_items.value = _items.value!!.toMutableList()

                    //check(index > -1) { "not found $item from list" }
                    //存在还没有把最新的item更新到binding view上去 此时点击的还是旧item 已在上次移除
                    val index = _items.value!!.indexOf(item)
                    if (index >= 0) {
                        _items.value  = _items.value!!.toMutableList().apply {
                            removeAt(index)
                            add(index,  item.copy(collect = !item.collect))
                        }
                    }
                }
                else -> {
                    view.context.startActivity(Intent().apply {
                        setClass(view.context, DetailActivity::class.java)
                        putExtra(DetailActivity.PARAM_ARTICLE, item)
                    })
                }
            }
        }
    }

    private var page = 0

    private val _items = MutableLiveData<MutableList<Article>>()

    val items: LiveData<MutableList<Article>> = _items
    val itemBinding = ItemBinding.of<Article>(BR.itemBean, R.layout.item_article)
        .bindExtra(BR.listenner, itemOnClickListener)

    val diff = Article.diffCallback

    fun refreshArticleList(isNotify: Boolean = false) {
        launchFlowzipResult({
            repository.getTopArticleList()
        }, {
            repository.getArticleList(0)
        }, {
            val a = it[0] as List<Article>
            val b = it[1] as Pagination<Article>
            if (a.isNullOrEmpty()) RESULT.EMPTY.code
            else {
                _items.value = mutableListOf<Article>().apply {
                    addAll(a.apply { forEach{it.top = true} })
                    addAll(b.datas)
                }
                RESULT.SUCCESS.code
            }
        }, isNotify = isNotify)
    }

    fun loadMoreArticleList() {
        launchOnlyResult({
            repository.getArticleList(page)
        }, {
            page = it.curPage
            val list = _items.value ?: mutableListOf<Article>()
            list.addAll(it.datas)
            if (it.offset >= it.total) RESULT.END.code else RESULT.SUCCESS.code
        }, isNotify = false)
    }
}
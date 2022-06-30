package me.tang.wanandroid.ui.main.home.latest

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
import me.tang.wanandroid.ui.main.home.HomeRepository
import me.tang.wanandroid.ui.detail.DetailActivity
import me.tatarka.bindingcollectionadapter2.ItemBinding

class LatestViewModel : BaseViewModel() {

    private val repository by lazy { HomeRepository.getInstance(ApiRetrofit.getInstance()) }

    private val onItemClickListener = object : OnItemClickListener<Article> {
        override fun onClick(view: View, item: Article) {
            when(view.id) {
                R.id.iv_collect -> {
                    item.collect = !item.collect
                    _items.value = _items.value!!.toMutableList()
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
        .bindExtra(BR.listenner, onItemClickListener)

    fun refreshProjectList(isNotify: Boolean = false) {
        launchOnlyResult({
            repository.getProjectList(0)
        }, {
            if (it.datas.isEmpty()) RESULT.EMPTY.code
            else {
                page = it.curPage
                _items.value = it.datas.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = isNotify)
    }

    fun loadMoreProjectList() {
        launchOnlyResult({
            repository.getProjectList(page)
        }, {
            page = it.curPage
            val list = _items.value ?: mutableListOf<Article>()
            list.addAll(it.datas)
            if (it.offset >= it.total) RESULT.END.code else RESULT.SUCCESS.code
        }, isNotify = false)
    }
}
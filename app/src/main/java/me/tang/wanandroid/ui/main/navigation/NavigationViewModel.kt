package me.tang.wanandroid.ui.main.navigation

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tang.wanandroid.model.api.ApiRetrofit
import me.tang.wanandroid.model.bean.Article
import me.tang.wanandroid.model.bean.Navigation
import me.tang.wanandroid.ui.detail.DetailActivity
import com.zhy.view.flowlayout.TagFlowLayout
import me.tang.mvvm.BR
import me.tang.mvvm.base.BaseViewModel
import me.tang.mvvm.network.RESULT
import me.tang.wanandroid.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

class NavigationViewModel : BaseViewModel() {

    private val repository by lazy { NavigationRepository.getInstance(ApiRetrofit.getInstance()) }

    private val _items = MutableLiveData<MutableList<Navigation>>()

    val items: LiveData<MutableList<Navigation>> = _items
    val itemBinding = ItemBinding.of<Navigation>(BR.itemBean, R.layout.item_navigation)
        .bindExtra(BR.viewModel, this)

    val itemBindingTag =  me.tang.bindingcollectionadapter
        .ItemBinding.of<Article>(BR.itemBean, R.layout.item_nav_tag)

    val onTagClickListener = TagFlowLayout.OnTagClickListener {  view, position, parent ->
        val cid = parent.getTag() as Int
        val list = items.value
        val item = list?.find { it.cid == cid } ?: return@OnTagClickListener false
        view.context.startActivity(Intent().apply {
            setClass(view.context, DetailActivity::class.java)
            putExtra(DetailActivity.PARAM_ARTICLE, item.articles.get(position))
        })
        true
    }

    fun getNavigation(isNotify: Boolean = false) {
        launchOnlyResult({
            repository.getNavigations()
        },{
            if (it.isNullOrEmpty()) RESULT.EMPTY.code
            else {
                _items.value = it.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = isNotify)
    }

}
package me.tang.wanandroid.ui.main.system

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tang.mvvm.base.BaseViewModel
import me.tang.mvvm.network.RESULT
import me.tang.wanandroid.model.api.ApiRetrofit
import me.tang.wanandroid.model.bean.Category

class SystemViewModel : BaseViewModel() {
    private val repository by lazy { SystemRepository.getInstance(ApiRetrofit.getInstance()) }

    private val _itemsCategory = MutableLiveData<MutableList<Category>>()

    val itemsCategory : LiveData<MutableList<Category>> = _itemsCategory

    fun getArticleCategories() {
        launchOnlyResult({
            repository.getArticleCategories()
        }, {
            if (it.isEmpty()) RESULT.EMPTY.code
            else {
                _itemsCategory.value = it
                RESULT.SUCCESS.code
            }
        })
    }
}
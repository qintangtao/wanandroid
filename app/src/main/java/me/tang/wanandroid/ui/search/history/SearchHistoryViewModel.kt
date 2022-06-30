package me.tang.wanandroid.ui.search.history

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tang.wanandroid.model.api.ApiRetrofit
import me.tang.wanandroid.model.bean.HotWord
import me.tang.wanandroid.ui.search.SearchRepository
import com.zhy.view.flowlayout.TagFlowLayout
import me.tang.mvvm.BR
import me.tang.mvvm.base.BaseViewModel
import me.tang.mvvm.base.OnItemClickListener
import me.tang.mvvm.bus.Bus
import me.tang.mvvm.network.RESULT
import me.tang.wanandroid.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

class SearchHistoryViewModel : BaseViewModel() {

    private val repository by lazy { SearchRepository.getInstance(ApiRetrofit.getInstance()) }

    private val onItemClickListener = object : OnItemClickListener<String> {
        override fun onClick(view: View, item: String) {
            when(view.id) {
                R.id.ivDelete -> {
                    deleteSearchHistory(item)
                }
                else -> {
                    Bus.post(SearchRepository.SELECTED_KEYWORDS, item)
                }
            }
        }
    }

    private val _itemsHotWord = MutableLiveData<MutableList<HotWord>>()
    private val _itemsHistory = MutableLiveData<MutableList<String>>()

    val itemsHotWord : LiveData<MutableList<HotWord>> = _itemsHotWord
    val itemBindingHotWord = me.tang.bindingcollectionadapter
        .ItemBinding.of<HotWord>(BR.itemBean, R.layout.item_hot_search)
    val onTagClickListener = TagFlowLayout.OnTagClickListener { _, position, _ ->
        Bus.post(SearchRepository.SELECTED_KEYWORDS, itemsHotWord.value!!.get(position).name)
        true
    }

    val itemsHistory : LiveData<MutableList<String>> = _itemsHistory
    val itemBindingHistory = ItemBinding.of<String>(BR.itemBean, R.layout.item_search_history)
        .bindExtra(BR.listenner, onItemClickListener)

    fun initData() {
        launchOnlyResult({
            repository.getHotSearch()
        }, {
            if (it.isNullOrEmpty()) RESULT.EMPTY.code
            else {
                _itemsHotWord.value = it.toMutableList()
                RESULT.SUCCESS.code
            }
        })

        _itemsHistory.value = repository.getSearchHisory()
    }

    fun addSearchHistory(searchWords: String) {
        val history = itemsHistory.value ?: mutableListOf()
        if (history.contains(searchWords)) {
            history.remove(searchWords)
        }
        history.add(0, searchWords)
        _itemsHistory.value = history.toMutableList()
        repository.saveSearchHistory(searchWords)
    }

    fun deleteSearchHistory(searchWords: String) {
        val history = itemsHistory.value ?: mutableListOf()
        if (history.contains(searchWords)) {
            history.remove(searchWords)
            _itemsHistory.value = history.toMutableList()
            repository.deleteSearchHistory(searchWords)
        }
    }
}
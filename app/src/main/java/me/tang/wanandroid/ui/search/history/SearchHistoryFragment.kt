package me.tang.wanandroid.ui.search.history

import android.os.Bundle
import me.tang.mvvm.bus.Bus
import me.tang.mvvm.state.base.BaseStateFragment
import me.tang.wanandroid.databinding.FragmentSearchHistoryBinding
import me.tang.wanandroid.ui.search.SearchActivity
import me.tang.wanandroid.ui.search.SearchRepository

class SearchHistoryFragment : BaseStateFragment<SearchHistoryViewModel, FragmentSearchHistoryBinding>() {

    companion object {
        fun newInstance() = SearchHistoryFragment()
    }

    override fun stateLayout() = mBinding.stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.viewModel = viewModel

        Bus.observe<String>(SearchRepository.SELECTED_KEYWORDS, this) {
            (activity as? SearchActivity)?.fillSearchInput(it)
        }
    }

    override fun lazyLoadData() {
        viewModel.initData()
    }

    fun addSearchHistory(keywords: String) {
        viewModel.addSearchHistory(keywords)
    }
}
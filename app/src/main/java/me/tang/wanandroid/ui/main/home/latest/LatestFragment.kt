package me.tang.wanandroid.ui.main.home.latest

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import me.tang.xrecyclerview.ProgressStyle
import me.tang.xrecyclerview.XRecyclerView
import me.tang.mvvm.network.RESULT
import me.tang.mvvm.state.base.BaseStateFragment
import me.tang.wanandroid.R
import me.tang.wanandroid.databinding.FragmentLatestBinding

class LatestFragment : BaseStateFragment<LatestViewModel, FragmentLatestBinding>() {

    companion object {
        fun newInstance() = LatestFragment()
    }

    override fun stateLayout() = mBinding.stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.viewModel = viewModel

        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { viewModel.refreshProjectList() }
        }

        mBinding.recyclerView.run {
            setPullRefreshEnabled(false)
            setLoadingMoreEnabled(true)
            setLoadingMoreProgressStyle(ProgressStyle.Pacman)
            setLoadingListener(object : XRecyclerView.LoadingListener {
                override fun onRefresh() {
                }

                override fun onLoadMore() {
                    viewModel.loadMoreProjectList()
                }
            })
        }

    }
    override fun lazyLoadData() {
        viewModel.refreshProjectList(true)
    }

    override fun onLoadResult(code: Int) {
        when(code) {
            RESULT.END.code -> ToastUtils.showLong(RESULT.END.msg)
            else -> super.onLoadResult(code)
        }
    }

    override fun onLoadCompleted() {
        super.onLoadCompleted()
        mBinding.run {
            if (swipeRefreshLayout.isRefreshing)
                swipeRefreshLayout.isRefreshing = false
            recyclerView.loadMoreComplete()
        }
    }
}
package me.tang.wanandroid.ui.main.home.project

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView
import me.tang.mvvm.network.RESULT
import me.tang.mvvm.state.base.BaseStateFragment
import me.tang.wanandroid.R
import me.tang.wanandroid.databinding.FragmentProjectBinding

class ProjectFragment : BaseStateFragment<ProjectViewModel, FragmentProjectBinding>() {

    companion object {
        fun newInstance() = ProjectFragment()
    }

    override fun stateLayout() = mBinding.stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.viewModel = viewModel

        mBinding.run {
            swipeRefreshLayout.run {
                setColorSchemeResources(R.color.textColorPrimary)
                setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
                setOnRefreshListener { viewModel?.refreshProjectList() }
            }

            recyclerView.run {
                setPullRefreshEnabled(false)
                setLoadingMoreEnabled(true)
                setLoadingMoreProgressStyle(ProgressStyle.Pacman)
                setLoadingListener(object : XRecyclerView.LoadingListener {
                    override fun onRefresh() { }
                    override fun onLoadMore() {
                        viewModel?.loadMoreProjectList()
                    }
                })
            }
        }
    }

    override fun lazyLoadData() {
        viewModel.getProjectCategories()
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
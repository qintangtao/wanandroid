package me.tang.wanandroid.ui.main.home.popular

import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import me.tang.xrecyclerview.ProgressStyle
import me.tang.xrecyclerview.XRecyclerView.LoadingListener
import me.tang.mvvm.event.Message
import me.tang.mvvm.network.RESULT
import me.tang.mvvm.state.base.BaseStateFragment
import me.tang.wanandroid.R
import me.tang.wanandroid.databinding.FragmentPopularBinding
import me.tang.wanandroid.model.bean.Article
import me.tang.wanandroid.ui.detail.DetailActivity

class PopularFragment : BaseStateFragment<PopularViewModel, FragmentPopularBinding>() {

    companion object {
        fun newInstance() = PopularFragment()
        const val START_DETAIL_ARTICLE = 10000
    }

    override fun stateLayout() = mBinding.stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.viewModel = viewModel


        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { viewModel.refreshArticleList() }
        }

        mBinding.recyclerView.run {
            setPullRefreshEnabled(false)
            setLoadingMoreEnabled(true)
            setLoadingMoreProgressStyle(ProgressStyle.Pacman)
            setLoadingListener(object : LoadingListener {
                override fun onRefresh() {
                }
                override fun onLoadMore() {
                    viewModel.loadMoreArticleList()
                }
            })
        }
    }

    override fun lazyLoadData() {
        viewModel.refreshArticleList(true)
    }

    override fun onLoadEvent(msg: Message) {
        when(msg.code) {
            START_DETAIL_ARTICLE -> {
                startActivity(Intent().apply {
                    setClass(requireActivity(), DetailActivity::class.java)
                    putExtra(DetailActivity.PARAM_ARTICLE, msg.obj as Article)
                })
            }
            else -> {
                super.onLoadEvent(msg)
            }
        }
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
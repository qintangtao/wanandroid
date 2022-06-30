package me.tang.wanandroid.ui.main.system.pager

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView
import me.tang.mvvm.network.RESULT
import me.tang.mvvm.state.base.BaseStateFragment
import me.tang.wanandroid.R
import me.tang.wanandroid.databinding.FragmentSystemPagerBinding
import me.tang.wanandroid.model.bean.Category

class SystemPagerFragment : BaseStateFragment<SystemPagerViewModel, FragmentSystemPagerBinding>() {

    companion object {
        private const val PARAM_CATEGORY = "param_category"
        fun newInstance(category: Category) : SystemPagerFragment {
            return SystemPagerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAM_CATEGORY, category)
                }
            }
        }
    }

    private lateinit var category: Category

    override fun stateLayout() = mBinding.stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.viewModel = viewModel

        arguments?.let {
            category = it.getParcelable(PARAM_CATEGORY)!!
        }

        mBinding.run {
            swipeRefreshLayout.run {
                setColorSchemeResources(R.color.textColorPrimary)
                setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
                setOnRefreshListener { viewModel?.refreshArticleList() }
            }

            recyclerView.run {
                setPullRefreshEnabled(false)
                setLoadingMoreEnabled(true)
                setLoadingMoreProgressStyle(ProgressStyle.Pacman)
                setLoadingListener(object : XRecyclerView.LoadingListener {
                    override fun onRefresh() { }
                    override fun onLoadMore() {
                        viewModel?.loadMoreArticleList()
                    }
                })
            }
        }

    }

    override fun lazyLoadData() {
        viewModel.getArticleList(category.children)
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

    fun getCheckedPosition() =  category.id to  viewModel.checkedCat.code

    fun check(position: Int) {
        val position2 = viewModel.itemsCategory.value?.get(position)?.id ?: return
        viewModel.check(position2)
    }
}
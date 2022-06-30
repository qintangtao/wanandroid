package me.tang.wanandroid.ui.main.navigation

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import me.tang.mvvm.state.base.BaseStateFragment
import me.tang.wanandroid.R
import me.tang.wanandroid.databinding.FragmentNavigationBinding
import me.tang.wanandroid.ui.main.MainActivity

class NavigationFragment : BaseStateFragment<NavigationViewModel, FragmentNavigationBinding>() {

    companion object {
        fun newInstance() = NavigationFragment()
    }

    private var currentPosition = 0

    override fun stateLayout() = mBinding.stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.viewModel = viewModel

        mBinding.run {
            swipeRefreshLayout.run {
                setColorSchemeResources(R.color.textColorPrimary)
                setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
                setOnRefreshListener { viewModel?.getNavigation(false)  }
            }

            recyclerView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (activity is MainActivity && scrollY != oldScrollY) {
                    (activity as MainActivity).animateBottomNavigationView(scrollY < oldScrollY)
                }
                if (scrollY < oldScrollY) {
                    tvFloatTitle.text = viewModel!!.items.value?.get(currentPosition)?.name
                }
                val lm = recyclerView.layoutManager as LinearLayoutManager
                val nextView = lm.findViewByPosition(currentPosition + 1)
                if (nextView != null) {
                    tvFloatTitle.y = if (nextView.top < tvFloatTitle.measuredHeight) {
                        (nextView.top - tvFloatTitle.measuredHeight).toFloat()
                    } else {
                        0f
                    }
                }
                currentPosition = lm.findFirstVisibleItemPosition()
                if (scrollY > oldScrollY) {
                    tvFloatTitle.text = viewModel!!.items.value?.get(currentPosition)?.name
                }
            }
        }
    }

    override fun lazyLoadData() {
        viewModel.getNavigation(true)
    }

    override fun onLoadCompleted() {
        super.onLoadCompleted()
        mBinding.run {
            if (swipeRefreshLayout.isRefreshing)
                swipeRefreshLayout.isRefreshing = false
        }
    }
}
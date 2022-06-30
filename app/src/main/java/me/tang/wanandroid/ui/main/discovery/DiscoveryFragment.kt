package me.tang.wanandroid.ui.main.discovery

import android.os.Bundle
import me.tang.mvvm.state.base.BaseStateFragment
import me.tang.wanandroid.R
import me.tang.wanandroid.databinding.FragmentDiscoveryBinding
import me.tang.wanandroid.ui.main.MainActivity

class DiscoveryFragment : BaseStateFragment<DiscoveryViewModel, FragmentDiscoveryBinding>() {

    companion object {
        fun newInstance() = DiscoveryFragment()
    }

    override fun stateLayout() = mBinding.stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.viewModel = viewModel

        mBinding.run {
            swipeRefreshLayout.run {
                setColorSchemeResources(R.color.textColorPrimary)
                setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
                setOnRefreshListener { viewModel?.getData() }
            }

            nestedScollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (activity is MainActivity && scrollY != oldScrollY) {
                    (activity as MainActivity).animateBottomNavigationView(scrollY < oldScrollY)
                }
            }
        }

    }

    override fun lazyLoadData() {
        viewModel.getData(true)
    }

    override fun onLoadCompleted() {
        super.onLoadCompleted()
        mBinding.run {
            if (swipeRefreshLayout.isRefreshing)
                swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.run {
            bannerView.startAutoPlay()
        }
    }

    override fun onPause() {
        super.onPause()
        mBinding.run {
            bannerView.stopAutoPlay()
        }
    }
}
package me.tang.wanandroid.ui.main.system

import android.os.Bundle
import android.view.View
import androidx.core.view.isEmpty
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import me.tang.mvvm.adapter.SimpleFragmentStateAdapter
import me.tang.wanandroid.ui.main.MainActivity
import me.tang.wanandroid.ui.main.system.category.SystemCategoryFragment
import me.tang.wanandroid.ui.main.system.pager.SystemPagerFragment
import me.tang.mvvm.state.base.BaseStateFragment
import me.tang.wanandroid.databinding.FragmentSystemBinding

class SystemFragment : BaseStateFragment<SystemViewModel, FragmentSystemBinding>() {

    companion object {
        fun newInstance() = SystemFragment()
    }

    private var categoryFragment: SystemCategoryFragment? = null
    private val titles = mutableListOf<String>()
    private var fragments = mutableListOf<SystemPagerFragment>()
    private var currentOffset = 0

    override fun stateLayout() = mBinding.stateLayout

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mBinding.viewModel = viewModel

        mBinding.let {
            it.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
                if (activity is MainActivity && this.currentOffset != offset) {
                    (activity as MainActivity).animateBottomNavigationView(offset > currentOffset)
                    currentOffset = offset
                }
            })
        }

        mBinding.run {

            ivFilter.setOnClickListener {
                categoryFragment?.show(childFragmentManager)
            }

            viewModel!!.itemsCategory.observe(viewLifecycleOwner, Observer {
                titles.clear()
                fragments.clear()
                it.forEach {
                    titles.add(it.name)
                    fragments.add(SystemPagerFragment.newInstance(it))
                }

                //viewPager.adapter = SimpleFragmentPagerAdapter(childFragmentManager, fragments, titles)
                //太多了会造成卡顿，通过viewmodel的生命周期，在初始化加载数据的时候，不重新加载新的数据，直接使用旧数据
                //viewPager.offscreenPageLimit = fragments.size
                //tabLayout.setupWithViewPager(viewPager)

                viewPager.adapter = SimpleFragmentStateAdapter(requireActivity(), fragments)
                TabLayoutMediator(tabLayout, viewPager, true, true) { tab, positon ->
                    tab.text = titles[positon]
                }.attach()

                categoryFragment = SystemCategoryFragment.newInstance(ArrayList(it))
            })
        }
    }

    override fun lazyLoadData() {
        viewModel.getArticleCategories();
    }

    override fun onLoadStart() {
        mBinding.viewPager.visibility = View.GONE
        super.onLoadStart()
    }

    override fun onLoadCompleted() {
        super.onLoadCompleted()
        mBinding.run {
            if (!isErrorLoaded && !isEmptyLoaded) {
                tvState.visibility = View.GONE
                viewPager.visibility = View.VISIBLE
            }
        }
    }

    fun getCurrentChecked(): Pair<Int, Int> {
        if (fragments.isEmpty()) return 0 to 0
        //val first = viewPager.currentItem
        //val second = fragments[viewPager.currentItem].getCheckedPosition()
        //return first to second
        return fragments[mBinding.viewPager.currentItem].getCheckedPosition()
    }

    fun check(position: Pair<Int, Int>) {
        if (fragments.isNullOrEmpty() || mBinding.viewPager.isEmpty()) return
        mBinding.viewPager.currentItem = position.first
        fragments[position.first].check(position.second)
    }
}
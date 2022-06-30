package me.tang.wanandroid.ui.main.mine

import android.os.Bundle
import me.tang.mvvm.base.BaseFragment
import me.tang.mvvm.bus.Bus
import me.tang.wanandroid.databinding.FragmentMineBinding
import me.tang.wanandroid.ui.common.UserRepository

class MineFragment : BaseFragment<MineViewModel, FragmentMineBinding>() {

    companion object {
        fun newInstance() = MineFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mBinding.viewModel = viewModel
        Bus.observe<Boolean>(UserRepository.USER_LOGIN_STATE_CHANGED, this) {
            viewModel.requestUserInfo()
        }
    }

    override fun lazyLoadData() {
        viewModel.requestUserInfo()
    }
}
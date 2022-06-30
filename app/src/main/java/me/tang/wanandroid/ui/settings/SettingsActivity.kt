package me.tang.wanandroid.ui.settings


import android.os.Bundle
import me.tang.mvvm.base.BaseActivity
import me.tang.wanandroid.databinding.ActivitySettingsBinding

class SettingsActivity : BaseActivity<SettingsViewModel, ActivitySettingsBinding>() {

    companion object {
        fun newInstance() = SettingsActivity()
    }


    override fun initView(savedInstanceState: Bundle?) {
        mBinding.viewModel = viewModel
        mBinding.run {
            ivBack.setOnClickListener { finish() }
        }
    }

    override fun initData() {
        viewModel.initData()
    }


}
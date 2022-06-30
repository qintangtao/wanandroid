package me.tang.wanandroid.ui.register

import android.os.Bundle
import me.tang.mvvm.base.BaseActivity
import me.tang.mvvm.event.Message
import me.tang.mvvm.network.RESULT
import me.tang.wanandroid.R
import me.tang.wanandroid.databinding.ActivityRegisterBinding

class RegisterActivity : BaseActivity<RegisterViewModel, ActivityRegisterBinding>(){

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.viewModel = viewModel
        mBinding.ivBack.setOnClickListener { finish() }
    }

    override fun initData() {

    }

    override fun onLoadStart() {
        showProgressDialog(R.string.registerring)
    }

    override fun onLoadResult(code: Int) {
        when(code) {
            RESULT.END.code -> finish()
            else -> super.onLoadResult(code)
        }
    }

    override fun onLoadEvent(msg: Message) {
        viewModel.confirmPasswordError.value = msg.msg
    }
}
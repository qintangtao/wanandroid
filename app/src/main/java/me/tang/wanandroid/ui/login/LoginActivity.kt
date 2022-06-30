package me.tang.wanandroid.ui.login

import android.os.Bundle
import me.tang.mvvm.base.BaseActivity
import me.tang.mvvm.event.Message
import me.tang.mvvm.network.RESULT
import me.tang.wanandroid.R
import me.tang.wanandroid.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>(){

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.viewModel = viewModel
        mBinding.ivClose.setOnClickListener { finish() }
    }

    override fun initData() {
        viewModel.account.value = "tangtao"
        viewModel.password.value = "123456"
    }

    override fun onLoadStart() {
        showProgressDialog(R.string.logging_in)
    }

    override fun onLoadResult(code: Int) {
        when(code) {
            RESULT.END.code -> finish()
            else -> super.onLoadResult(code)
        }
    }

    override fun onLoadEvent(msg: Message) {
        viewModel.passwordError.value = msg.msg
    }
}
package me.tang.wanandroid.ui.base

import com.blankj.utilcode.util.ActivityUtils
import me.tang.wanandroid.ui.common.UserRepository
import me.tang.wanandroid.model.api.ApiRetrofit
import me.tang.mvvm.base.BaseViewModel
import me.tang.wanandroid.ui.login.LoginActivity

open class BaseUserViewModel : BaseViewModel() {

    protected val repository by lazy { UserRepository.getInstance(ApiRetrofit.getInstance()) }

    fun checkLogin(block: (() -> Unit)? = null): Boolean {
        return if (repository.isLogin()) {
            block?.invoke()
            true
        } else {
            ActivityUtils.startActivity(LoginActivity::class.java)
            false
        }
    }
}
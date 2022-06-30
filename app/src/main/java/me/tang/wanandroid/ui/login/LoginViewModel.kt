package me.tang.wanandroid.ui.login

import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import me.tang.wanandroid.model.api.ApiRetrofit
import me.tang.wanandroid.ui.common.UserRepository
import me.tang.wanandroid.ui.register.RegisterActivity
import me.tang.mvvm.base.BaseViewModel
import me.tang.mvvm.bus.Bus
import me.tang.mvvm.network.RESULT
import me.tang.wanandroid.R

class LoginViewModel : BaseViewModel() {

    private val repository by lazy { UserRepository.getInstance(ApiRetrofit.getInstance()) }

    val account = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val accountError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()

    fun click() {
        accountError.value = ""
        passwordError.value = ""
        when {
            account.value.isNullOrEmpty() -> accountError.value = getString(R.string.account_can_not_be_empty)
            password.value.isNullOrEmpty() -> passwordError.value = getString(R.string.password_can_not_be_empty)
            else -> {
                launchOnlyResult({
                    repository.login(account.value!!, password.value!!)
                }, {
                    //save userinfo
                    repository.updateUserInfo(it)
                    Bus.post(UserRepository.USER_LOGIN_STATE_CHANGED, true)
                    RESULT.END.code
                })
            }
        }
    }

    fun clickRegister(view: View) {
        view.context.startActivity(Intent(view.context, RegisterActivity::class.java))
    }
}
package me.tang.wanandroid.ui.register

import androidx.lifecycle.MutableLiveData
import me.tang.mvvm.base.BaseViewModel
import me.tang.mvvm.network.RESULT
import me.tang.wanandroid.R
import me.tang.wanandroid.model.api.ApiRetrofit
import me.tang.wanandroid.ui.common.UserRepository

class RegisterViewModel : BaseViewModel() {
    private val repository by lazy { UserRepository.getInstance(ApiRetrofit.getInstance()) }

    val account = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    val accountError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()
    val confirmPasswordError = MutableLiveData<String>()

    fun click() {
        accountError.value = ""
        passwordError.value = ""
        confirmPasswordError.value = ""
        when {
            account.value.isNullOrEmpty() -> accountError.value = getString(R.string.account_can_not_be_empty)
            account.value!!.length < 3 ->  accountError.value = getString(R.string.account_length_over_three)
            password.value.isNullOrEmpty() -> passwordError.value = getString(R.string.password_can_not_be_empty)
            password.value!!.length < 6 ->  passwordError.value = getString(R.string.password_length_over_six)
            confirmPassword.value.isNullOrEmpty() -> confirmPasswordError.value = getString(R.string.confirm_password_can_not_be_empty)
            !password.value.equals(confirmPassword.value) -> confirmPasswordError.value = getString(R.string.two_password_are_inconsistent)
            else -> {
                launchOnlyResult({
                    repository.register(account.value!!, password.value!!, confirmPassword.value!!)
                }, {
                    RESULT.END.code
                })
            }
        }
    }
}
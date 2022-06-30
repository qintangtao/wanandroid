package me.tang.wanandroid.ui.main.mine

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ActivityUtils
import me.tang.wanandroid.R
import me.tang.wanandroid.model.bean.UserInfo
import me.tang.wanandroid.ui.base.BaseUserViewModel
import me.tang.wanandroid.ui.settings.SettingsActivity

class MineViewModel : BaseUserViewModel() {

    private val _userInfo = MutableLiveData<UserInfo?>()
    private val _isLogin = MutableLiveData<Boolean>()

    val userInfo: LiveData<UserInfo?> = _userInfo
    val isLogin: LiveData<Boolean> = _isLogin

    fun requestUserInfo() {
        _isLogin.value = repository.isLogin()
        _userInfo.value = repository.getUserInfo()
    }

    fun click(view: View) {
        when(view.id) {
            R.id.clHeader -> checkLogin {

            }
            R.id.llMyPoints -> checkLogin {

            }
            R.id.llPointsRank -> checkLogin {

            }
            R.id.llMyShare -> checkLogin {

            }
            R.id.llMyCollect -> checkLogin {

            }
            R.id.llHistory -> checkLogin {

            }
            R.id.llAboutAuthor -> checkLogin {

            }
            R.id.llOpenSource -> checkLogin {

            }
            R.id.llSetting -> checkLogin {
                ActivityUtils.startActivity(SettingsActivity::class.java)
            }
        }
    }
}
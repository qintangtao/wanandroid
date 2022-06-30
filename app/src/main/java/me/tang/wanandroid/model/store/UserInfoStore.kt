package me.tang.wanandroid.model.store

import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import me.tang.mvvm.utils.clearSpValue
import me.tang.mvvm.utils.getSpValue
import me.tang.mvvm.utils.putSpValue
import me.tang.wanandroid.model.bean.UserInfo

object UserInfoStore {

    private const val SP_USER_INFO = "sp_user_info"
    private const val KEY_USER_INFO = "userInfo"
    private val mGson by lazy { Gson() }

    fun isLogin(): Boolean {
        val userInfoStr = getSpValue(SP_USER_INFO, Utils.getApp(), KEY_USER_INFO, "")
        return userInfoStr.isNotEmpty()
    }

    fun getUserInfo(): UserInfo? {
        val userInfoStr = getSpValue(SP_USER_INFO, Utils.getApp(), KEY_USER_INFO, "")
        return if (userInfoStr.isNotEmpty()) {
            mGson.fromJson(userInfoStr, UserInfo::class.java)
        } else {
            null
        }
    }

    fun setUserInfo(userInfo: UserInfo) =
        putSpValue(SP_USER_INFO, Utils.getApp(), KEY_USER_INFO, mGson.toJson(userInfo))

    fun clearUserInfo() {
        clearSpValue(SP_USER_INFO, Utils.getApp())
    }

}
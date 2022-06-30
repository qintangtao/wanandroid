package me.tang.wanandroid

import com.blankj.utilcode.util.ProcessUtils
import me.tang.wanandroid.model.store.SettingsStore
import me.tang.mvvm.base.BaseApplication
import me.tang.mvvm.utils.setNightMode

class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        if (ProcessUtils.isMainProcess()) {
            setNightMode(SettingsStore.getNightMode())
        }
    }
}
package me.tang.wanandroid.model.store

import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.tang.mvvm.utils.getSpValue
import me.tang.mvvm.utils.putSpValue

object SearchHistoryStore {
    private const val SP_SEARCH_HISTORY = "sp_search_history"
    private const val KEY_SEARCH_HISTORY = "searchHistory"
    private val mGson by lazy { Gson() }

    fun saveSearchHistory(words: String) {
        val history = getSearchHistory()
        if (history.contains(words)) {
            history.remove(words)
        }
        history.add(0, words)
        val listStr = mGson.toJson(history)
        putSpValue(SP_SEARCH_HISTORY, Utils.getApp(), KEY_SEARCH_HISTORY, listStr)
    }

    fun deleteSearchHistory(words: String) {
        val history = getSearchHistory()
        history.remove(words)
        val listStr = mGson.toJson(history)
        putSpValue(SP_SEARCH_HISTORY, Utils.getApp(), KEY_SEARCH_HISTORY, listStr)
    }

    fun getSearchHistory(): MutableList<String> {
        val listStr = getSpValue(SP_SEARCH_HISTORY, Utils.getApp(), KEY_SEARCH_HISTORY, "")
        return if (listStr.isEmpty()) {
            mutableListOf()
        } else {
            mGson.fromJson<MutableList<String>>(
                listStr,
                object : TypeToken<MutableList<String>>() {}.type
            )
        }
    }
}
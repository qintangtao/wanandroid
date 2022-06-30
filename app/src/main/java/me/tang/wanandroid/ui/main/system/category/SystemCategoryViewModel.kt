package me.tang.wanandroid.ui.main.system.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tang.wanandroid.model.bean.Category
import com.zhy.view.flowlayout.TagFlowLayout
import me.tang.mvvm.BR
import me.tang.mvvm.base.BaseViewModel
import me.tang.mvvm.bus.Bus
import me.tang.wanandroid.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

class SystemCategoryViewModel : BaseViewModel() {

    private val _checked = MutableLiveData<Pair<Int, Int>>()
    private val _items = MutableLiveData<MutableList<Category>>()

    val checked: LiveData<Pair<Int, Int>> = _checked
    val items: LiveData<MutableList<Category>> = _items

    val itemBinding = ItemBinding.of<Category>(BR.itemBean, R.layout.item_system_category)
        .bindExtra(BR.viewModel, this)

    val itemBindingTag =  me.tang.bindingcollectionadapter
        .ItemBinding.of<Category>(BR.itemBean, R.layout.item_system_category_tag)

    val onTagClickListener = TagFlowLayout.OnTagClickListener { _, position, parent ->
        var id = parent.getTag() as Int
        val item = items.value!!.find { it.id == id } ?: return@OnTagClickListener false
        id = items.value!!.indexOf(item)
        _checked.value = id to position
        Bus.post("CHECK",id to position)
        true
    }

    fun initData(chacked: Pair<Int, Int>, categorys: List<Category>) {
        _checked.value = chacked
        _items.value = categorys.toMutableList()
    }
}
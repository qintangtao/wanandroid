package me.tang.wanandroid.ui.main.system.category


import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.ScreenUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.tang.mvvm.base.BaseBottomSheetDialogFragment
import me.tang.mvvm.bus.Bus
import me.tang.wanandroid.databinding.FragmentSystemCategoryBinding
import me.tang.wanandroid.model.bean.Category
import me.tang.wanandroid.ui.main.system.SystemFragment

class SystemCategoryFragment() : BaseBottomSheetDialogFragment<SystemCategoryViewModel, FragmentSystemCategoryBinding>() {

    companion object {
        private const val PARAM_CATEGORY_LIST = "param_category_list"

        fun newInstance(categorys: ArrayList<Category>): SystemCategoryFragment {
            return  SystemCategoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(PARAM_CATEGORY_LIST, categorys)
                }
            }
        }
    }

    private var height: Int? = null
    private var behavior: BottomSheetBehavior<View>? = null
    private lateinit var categorys: ArrayList<Category>

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mBinding.viewModel = viewModel
        categorys = arguments?.getParcelableArrayList<Category>(PARAM_CATEGORY_LIST)!!

        Bus.observe<Pair<Int, Int>>("CHECK", this) {
            behavior?.state = BottomSheetBehavior.STATE_HIDDEN
            (parentFragment as SystemFragment).check(it)
        }
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet: View = (dialog as BottomSheetDialog).delegate
            .findViewById(com.google.android.material.R.id.design_bottom_sheet)
            ?: return
        behavior = BottomSheetBehavior.from(bottomSheet)
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        height?.let { behavior?.peekHeight = it }
        dialog?.window?.let {
            it.setGravity(Gravity.BOTTOM)
            it.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, height ?: ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun lazyLoadData() {
        var checked = (parentFragment as SystemFragment).getCurrentChecked()
        val item = categorys.find { it.id == checked.first}
        val second = if (item != null) { val item2 = item.children.find { it.id == checked.second }
            if (item2 != null) item.children.indexOf(item2) else -1
        } else -1
        viewModel.initData(checked.first to second, categorys)
    }

    fun show(manager: FragmentManager, height: Int? = null) {
        this.height = height ?: (ScreenUtils.getScreenHeight() * 0.75f).toInt()
        super.show(manager, SystemCategoryFragment::class.java.name)
    }
}
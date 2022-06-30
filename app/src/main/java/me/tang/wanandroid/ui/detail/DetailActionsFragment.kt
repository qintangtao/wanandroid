package me.tang.wanandroid.ui.detail

import android.os.Bundle
import android.view.View
import me.tang.wanandroid.model.bean.Article
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.tang.mvvm.base.BaseBottomSheetDialogFragment
import me.tang.mvvm.base.NoViewModel
import me.tang.mvvm.ext.copyTextIntoClipboard
import me.tang.mvvm.ext.openInExplorer
import me.tang.mvvm.ext.showToast
import me.tang.mvvm.utils.share
import me.tang.wanandroid.R
import me.tang.wanandroid.databinding.FragmentDetailAcitonsBinding

class DetailActionsFragment : BaseBottomSheetDialogFragment<NoViewModel, FragmentDetailAcitonsBinding>() {

    companion object {
        private const val PARAM_ARTICLE = "param_article"
        fun newInstance(article: Article): DetailActionsFragment {
            return DetailActionsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAM_ARTICLE, article)
                }
            }
        }
    }

    private var behavior: BottomSheetBehavior<View>? = null
    private lateinit var article: Article

    override fun onStart() {
        super.onStart()
        val bottomSheet: View = (dialog as BottomSheetDialog).delegate
            .findViewById(com.google.android.material.R.id.design_bottom_sheet)
            ?: return
        behavior = BottomSheetBehavior.from(bottomSheet)
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun lazyLoadData() {
        arguments?.run {
            article  = getParcelable<Article>(PARAM_ARTICLE) ?: return@run
            mBinding.article = article
            mBinding.run {
                llCollect.setOnClickListener {
                    (activity as? DetailActivity)?.changeCollect()
                    behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                }
                llShare.setOnClickListener {
                    behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                    share(
                        requireActivity(),
                        requireActivity().getString(R.string.app_name),
                        article!!.title + article!!.link
                    )
                }
                llExplorer.setOnClickListener {
                    openInExplorer(article!!.link)
                    behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                }
                llCopy.setOnClickListener {
                    context?.copyTextIntoClipboard(article!!.link, article!!.title)
                    context?.showToast(R.string.copy_success)
                    behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                }
                llRefresh.setOnClickListener {
                    (activity as? DetailActivity)?.refreshPage()
                    behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }
    }


    fun show(manager: FragmentManager) {
        super.show(manager, DetailActionsFragment::class.java.name)
    }
}
package me.tang.wanandroid.ui.detail

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.WebView
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import me.tang.mvvm.base.BaseActivity
import me.tang.wanandroid.R
import me.tang.wanandroid.databinding.ActivityDetailBinding
import me.tang.wanandroid.model.bean.Article

class DetailActivity : BaseActivity<DetailViewModel, ActivityDetailBinding>() {

    companion object {
        const val PARAM_ARTICLE = "param_article"
    }

    private lateinit var article: Article
    private var agentWeb: AgentWeb? = null


    override fun initView(savedInstanceState: Bundle?) {
        mBinding.viewModel = viewModel
        article  = intent?.getParcelableExtra(PARAM_ARTICLE) ?: return

        mBinding.run {
            ivBack.setOnClickListener {
                finish()
            }
            ivMore.setOnClickListener {
                DetailActionsFragment.newInstance(article).show(supportFragmentManager)
            }
        }
    }

    override fun initData() {
        viewModel.setArtivle(article)

        agentWeb = AgentWeb.with(this)
            .setAgentWebParent(mBinding.webContainer, ViewGroup.LayoutParams(-1,-1))
            .useDefaultIndicator(getColor(R.color.textColorPrimary), 2)
            .interceptUnkownUrl()
            //.setMainFrameErrorView(R.layout.default_error_view, R.id.text_error)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)
            .setWebChromeClient(object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    title?.let { viewModel.setTitle(it) }
                    super.onReceivedTitle(view, title)
                }
            })
            .setWebViewClient(object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    view?.loadUrl(customJs(url))
                }
            })
            .createAgentWeb()
            .get()

        agentWeb?.webCreator?.webView?.run {
            overScrollMode = WebView.OVER_SCROLL_NEVER
            settings.run {
                //是否允许JS弹窗
                javaScriptCanOpenWindowsAutomatically = false
                //自动加载image
                loadsImagesAutomatically = true
                //支持<meta>标签的viewport属性,使html界面自适应屏幕
                useWideViewPort = true
                //缩放至屏幕的大小
                loadWithOverviewMode = true

                //setSupportZoom(true)
                //textZoom = SettingsStore.getWebTextZoom()
            }
        }

        agentWeb?.urlLoader?.loadUrl(article.link)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (agentWeb?.handleKeyEvent(keyCode, event) == true) {
            return true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onPause() {
        agentWeb?.webLifeCycle?.onPause()
        super.onPause()

    }

    override fun onResume() {
        agentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        agentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }

    fun changeCollect() {
        if (article.collect) {
            //viewModel.uncollect(article.id)
        } else {
            //viewModel.collect(article.id)
        }
    }

    fun refreshPage() {
        agentWeb?.urlLoader?.reload()
    }

    private fun customJs(url: String? = article.link): String {
        val js = StringBuilder()
        js.append("javascript:(function(){")
        when (Uri.parse(url).host) {
            "juejin.im" -> {
                js.append("var headerList = document.getElementsByClassName('main-header-box');")
                js.append("if(headerList&&headerList.length){headerList[0].parentNode.removeChild(headerList[0])}")
                js.append("var openAppList = document.getElementsByClassName('open-in-app');")
                js.append("if(openAppList&&openAppList.length){openAppList[0].parentNode.removeChild(openAppList[0])}")
                js.append("var actionBox = document.getElementsByClassName('action-box');")
                js.append("if(actionBox&&actionBox.length){actionBox[0].parentNode.removeChild(actionBox[0])}")
                js.append("var actionBarList = document.getElementsByClassName('action-bar');")
                js.append("if(actionBarList&&actionBarList.length){actionBarList[0].parentNode.removeChild(actionBarList[0])}")
                js.append("var columnViewList = document.getElementsByClassName('column-view');")
                js.append("if(columnViewList&&columnViewList.length){columnViewList[0].style.margin = '0px'}")
            }
            "www.jianshu.com" -> {
                js.append("var jianshuHeader = document.getElementById('jianshu-header');")
                js.append("if(jianshuHeader){jianshuHeader.parentNode.removeChild(jianshuHeader)}")
                js.append("var headerShimList = document.getElementsByClassName('header-shim');")
                js.append("if(headerShimList&&headerShimList.length){headerShimList[0].parentNode.removeChild(headerShimList[0])}")
                js.append("var fubiaoList = document.getElementsByClassName('fubiao-dialog');")
                js.append("if(fubiaoList&&fubiaoList.length){fubiaoList[0].parentNode.removeChild(fubiaoList[0])}")
                js.append("var ads = document.getElementsByClassName('note-comment-above-ad-wrap');")
                js.append("if(ads&&ads.length){ads[0].parentNode.removeChild(ads[0])}")

                js.append("var lazyShimList = document.getElementsByClassName('v-lazy-shim');")
                js.append("if(lazyShimList&&lazyShimList.length&&lazyShimList[0]){lazyShimList[0].parentNode.removeChild(lazyShimList[0])}")
                js.append("if(lazyShimList&&lazyShimList.length&&lazyShimList[1]){lazyShimList[1].parentNode.removeChild(lazyShimList[1])}")
            }
            "blog.csdn.net" -> {
                js.append("var csdnToolBar = document.getElementById('csdn-toolbar');")
                js.append("if(csdnToolBar){csdnToolBar.parentNode.removeChild(csdnToolBar)}")
                js.append("var csdnMain = document.getElementById('main');")
                js.append("if(csdnMain){csdnMain.style.margin='0px'}")
                js.append("var operate = document.getElementById('operate');")
                js.append("if(operate){operate.parentNode.removeChild(operate)}")
            }
        }
        js.append("})()")
        return js.toString()
    }
}
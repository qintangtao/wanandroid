package me.tang.wanandroid.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.ViewPropertyAnimator
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.animation.AnimationUtils
import me.tang.wanandroid.R
import me.tang.wanandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var bottomNavigationViewAnimtor: ViewPropertyAnimator? = null
    private var currentBottomNavagtionState = true

    private lateinit var mBinding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        mBinding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(mBinding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
         */
        mBinding.run {
            navView.setupWithNavController(navController)
        }
    }

    fun animateBottomNavigationView(show: Boolean) {
        mBinding.run {
            if (currentBottomNavagtionState == show) {
                return
            }
            if (bottomNavigationViewAnimtor != null) {
                bottomNavigationViewAnimtor?.cancel()
                navView.clearAnimation()
            }
            currentBottomNavagtionState = show
            val targetY = if (show) 0F else navView.measuredHeight.toFloat()
            val duration = if (show) 225L else 175L
            bottomNavigationViewAnimtor = navView.animate()
                .translationY(targetY)
                .setDuration(duration)
                .setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        bottomNavigationViewAnimtor = null
                    }
                })
        }

    }
}

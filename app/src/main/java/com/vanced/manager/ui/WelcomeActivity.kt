package com.vanced.manager.ui

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.vanced.manager.adapter.WelcomePageAdapter
import com.vanced.manager.databinding.ActivityWelcomeBinding
import kotlin.math.abs

class WelcomeActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager2 = binding.welcomeViewpager
        viewPager2.apply {
            adapter = WelcomePageAdapter(this@WelcomeActivity)
            isUserInputEnabled = false
            setPageTransformer { page, position ->
                page.apply {
                    val pageWidth = width
                    //Thank you, fragula dev!
                    when {
                        position > 0 && position < 1 -> {
                            alpha = 1f
                            translationX = 0f
                        }
                        position > -1 && position <= 0 -> {
                            alpha = 1.0f - abs(position * 0.7f)
                            translationX = -pageWidth * position / 1.3F
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (viewPager2.currentItem == 0) {
            super.onBackPressed()
        } else {
            navigateTo(viewPager2.currentItem - 1)
        }
    }

    fun navigateTo(position: Int) {
        viewPager2.currentPosition = position
    }

    //Shit way to implement animation duration, but at least it works
    private var ViewPager2.currentPosition: Int
        get() = currentItem
        set(value)  {
            val pixelsToDrag: Int = width * (value - currentItem)
            val animator = ValueAnimator.ofInt(0, pixelsToDrag)
            var previousValue = 0
            animator.apply {
                addUpdateListener { valueAnimator ->
                    val currentValue = valueAnimator.animatedValue as Int
                    val currentPxToDrag = (currentValue - previousValue).toFloat()
                    fakeDragBy(-currentPxToDrag)
                    previousValue = currentValue
                }
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) { beginFakeDrag() }
                    override fun onAnimationEnd(animation: Animator?) { endFakeDrag() }
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationRepeat(animation: Animator?) {}
                })
                interpolator = AccelerateDecelerateInterpolator()
                duration = 500
                start()
            }
        }

}
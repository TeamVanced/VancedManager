package com.vanced.manager.ui

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.LayoutDirection
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.viewpager2.widget.ViewPager2
import com.vanced.manager.adapter.WelcomePageAdapter
import com.vanced.manager.databinding.ActivityWelcomeBinding
import kotlin.math.abs

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private var isRtl = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isRtl = resources.configuration.layoutDirection == LayoutDirection.RTL

        binding.welcomeViewpager.apply {
            adapter = WelcomePageAdapter(this@WelcomeActivity)
            isUserInputEnabled = false
            setPageTransformer { page, position ->
                page.apply {
                    val pageWidth = width.toFloat()
                    //Thank you, fragula dev!
                    when {
                        position > 0 && position < 1 -> {
                            alpha = 1f
                            translationX = 0f
                        }
                        position > -1 && position <= 0 -> {
                            alpha = 1.0f - abs(position * 0.7f)
                            translationX = pageWidth.rtlCompat * position / 1.3F
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        with(binding) {
            if (welcomeViewpager.currentItem == 0) {
                super.onBackPressed()
            } else {
                navigateTo(welcomeViewpager.currentItem - 1)
            }
        }
    }

    fun navigateTo(position: Int) {
        binding.welcomeViewpager.currentPosition = position
    }

    private val Float.rtlCompat get() = if (isRtl) this else -this

    //Shit way to implement animation duration, but at least it works
    private var ViewPager2.currentPosition: Int
        get() = currentItem
        set(value) {
            val pixelsToDrag: Int = width * (value - currentItem)
            val animator = ValueAnimator.ofInt(0, pixelsToDrag)
            var previousValue = 0
            animator.apply {
                addUpdateListener { valueAnimator ->
                    val currentValue = valueAnimator.animatedValue as Int
                    val currentPxToDrag = (currentValue - previousValue).toFloat()
                    fakeDragBy(currentPxToDrag.rtlCompat)
                    previousValue = currentValue
                }
                addListener(
                    onStart = { beginFakeDrag() },
                    onEnd = { endFakeDrag() }
                )
                duration = 500
                start()
            }
        }

}
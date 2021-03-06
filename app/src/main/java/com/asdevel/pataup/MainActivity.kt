package com.asdevel.pataup

import android.arch.lifecycle.Observer
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.asdevel.pataup.arch.PataUpManager
import com.asdevel.pataup.databinding.ActivityMainBinding
import com.common.binding.BindingActivity
import com.common.utils.AnimationUtils
import com.common.utils.animateWithDrawable
import com.common.utils.gone
import com.common.utils.setTextHtml

class MainActivity : BindingActivity<ActivityMainBinding>() {

    companion object {
        var alreadyAnimated = false
    }

    override fun onCreate() {

        setNavigationBarColor(resources.getColor(R.color.dinosaurColor))

        PataUpManager.pataUpStatus.observe(this, Observer {
            onPataStatusChange(it ?: false)
        })

        PataUpManager.elapsedTimesStatus.observe(this, Observer {
            val statusRes = when (it) {
                in 0L..1_000L -> R.string.buena
                in 1_000L..6_000L -> R.string.regular
                else -> R.string.mala
            }
            BINDING_VIEWS.elapsedTimeTextView.setTextHtml(if (it ?: 0L == 0L) "" else "${getString(statusRes)}<br><small>$it ms</small>")
        })

        BINDING_VIEWS.scanButton.setOnClickListener {
            PataUpManager.scanning = !BINDING_VIEWS.scanButton.isSelected
        }

        animIn()
    }

    fun animIn() {
        if (alreadyAnimated)
            return
        alreadyAnimated = true
        AnimationUtils.newAnimatorOfFloat(BINDING_VIEWS.backgroundView, "TranslationY", -500f, 0f, 500, 200, DecelerateInterpolator()).start()
        AnimationUtils.newAnimatorOfFloat(BINDING_VIEWS.dinoImageView, "TranslationY", -300f, 0f, 500, 200, DecelerateInterpolator()).start()
        AnimationUtils.newAnimatorOfFloat(BINDING_VIEWS.cloudImageView, "TranslationY", -200f, 0f, 500, 200, DecelerateInterpolator()).start()
        AnimationUtils.newAnimatorOfFloat(BINDING_VIEWS.elapsedTimeTextView, "TranslationY", -200f, 0f, 500, 200, DecelerateInterpolator()).start()
        AnimationUtils.newAnimatorOfFloat(BINDING_VIEWS.settingsButton, "TranslationY", -200f, 0f, 500, 200, DecelerateInterpolator()).start()
        AnimationUtils.newAnimatorOfFloat(BINDING_VIEWS.statusTextView, "TranslationY", -400f, 0f, 500, 200, DecelerateInterpolator()).start()
        AnimationUtils.newAnimatorOfFloat(BINDING_VIEWS.scanButton, "TranslationY", -800f, 0f, 500, 200, DecelerateInterpolator()).start()
        AnimationUtils.newAnimatorOfFloat(BINDING_VIEWS.scanButtonText, "TranslationY", -900f, 0f, 500, 200, DecelerateInterpolator()).start()
    }

    fun onPataStatusChange(pataUp: Boolean) {
        val scanOn = PataUpManager.scanning
        logRed("onPataStatusChange scanOn:$scanOn pataUp:$pataUp")

        BINDING_VIEWS.scanButton.isSelected = scanOn
        BINDING_VIEWS.scanButton.text = getString(if (scanOn) R.string.on else R.string.off)

        if (!scanOn) {
            BINDING_VIEWS.dinoImageView.setImageResource(R.drawable.dino_lupa_white)
            BINDING_VIEWS.statusTextView.text = getString(R.string.inspector_off)
            BINDING_VIEWS.statusTextView.textSize = 16f
            BINDING_VIEWS.pathImageView.gone()
            BINDING_VIEWS.cloudImageView.gone()
            BINDING_VIEWS.elapsedTimeTextView.gone()
        } else {
            BINDING_VIEWS.statusTextView.textSize = 25f
            if (pataUp)
                BINDING_VIEWS.dinoImageView.setImageResource(R.drawable.pata_up)
            else
                BINDING_VIEWS.dinoImageView.animateWithDrawable(R.drawable.dino_lupa_white_running)

            BINDING_VIEWS.pathImageView.visibility = if (pataUp) View.GONE else View.VISIBLE
            BINDING_VIEWS.cloudImageView.visibility = if (pataUp) View.GONE else View.VISIBLE
            BINDING_VIEWS.elapsedTimeTextView.visibility = if (pataUp) View.VISIBLE else View.GONE

            BINDING_VIEWS.statusTextView.text = getString(if (pataUp) R.string.pata_up else R.string.buscando_la_pata)
        }
    }

    override fun getLayoutResource(): Int = R.layout.activity_main
}

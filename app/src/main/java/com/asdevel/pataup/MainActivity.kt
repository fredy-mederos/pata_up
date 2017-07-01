package com.asdevel.pataup

import android.arch.lifecycle.Observer
import android.view.View
import com.asdevel.pataup.arch.PataUpManager
import com.asdevel.pataup.databinding.ActivityMainBinding
import com.common.binding.BindingActivity
import com.common.utils.animateWithDrawable
import com.common.utils.gone

class MainActivity : BindingActivity<ActivityMainBinding>() {

    override fun onCreate() {

        PataUpManager.pataUpStatus.observe(this, Observer {
            onPataStatusChange(it ?: false)
        })

        BINDING_VIEWS.scanButton.setOnClickListener {
            PataUpManager.scanning = !BINDING_VIEWS.scanButton.isSelected
        }

    }

    fun onPataStatusChange(pataUp: Boolean) {
        val scanOn = PataUpManager.scanning

        BINDING_VIEWS.scanButton.isSelected = scanOn
        BINDING_VIEWS.scanButton.text = getString(if (scanOn) R.string.on else R.string.off)

        if (!scanOn) {
            BINDING_VIEWS.dinoImageView.setImageResource(R.drawable.dino_lupa_white)
            BINDING_VIEWS.statusTextView.text = ""
            BINDING_VIEWS.pathImageView.gone()
        } else {
            if (pataUp)
                BINDING_VIEWS.dinoImageView.setImageResource(R.drawable.pata_up)
            else
                BINDING_VIEWS.dinoImageView.animateWithDrawable(R.drawable.dino_lupa_white_running)

            BINDING_VIEWS.pathImageView.visibility = if (pataUp) View.GONE else View.VISIBLE
            BINDING_VIEWS.statusTextView.text = getString(if (pataUp) R.string.pata_up else R.string.buscando_la_pata)
        }
    }

    override fun getLayoutResource(): Int = R.layout.activity_main
}

package com.asdevel.pataup

import android.graphics.drawable.AnimationDrawable
import android.view.View
import com.asdevel.pataup.databinding.ActivityMainBinding
import com.common.binding.BindingActivity

class MainActivity : BindingActivity<ActivityMainBinding>() {

    var scanning = false

    override fun onCreate() {

        (BINDING_VIEWS.pathImageView.drawable as AnimationDrawable).start()

        BINDING_VIEWS.scanButton.setOnClickListener {
            scanning = !scanning
            updateScanButtonFromState()
        }
    }

    fun updateScanButtonFromState() {
        BINDING_VIEWS.scanButton.isSelected = scanning
        BINDING_VIEWS.pathImageView.visibility = if(scanning) View.GONE else View.VISIBLE
        BINDING_VIEWS.scanButton.text = if (scanning) "on" else "off"
        BINDING_VIEWS.dinoImageView.setImageResource(if (scanning) R.drawable.pata_up else R.drawable.dino_lupa_white_running)
        BINDING_VIEWS.statusTextView.text = if (scanning) "Pata Up!" else ""

        if (!scanning && BINDING_VIEWS.dinoImageView.drawable is AnimationDrawable)
            (BINDING_VIEWS.dinoImageView.drawable as AnimationDrawable).start()
    }

    override fun getLayoutResource(): Int = R.layout.activity_main
}

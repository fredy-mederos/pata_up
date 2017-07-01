package com.common.binding

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Build
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.v7.app.AppCompatActivity
import com.common.utils.MyLogger

/**
 * Created by @Fredy.
 */

abstract class BindingActivity<T : ViewDataBinding> : AppCompatActivity(), MyLogger, LifecycleRegistryOwner {

    /**
     * Binding views.
     */
    protected lateinit var BINDING_VIEWS: T

    protected abstract fun onCreate()

    protected abstract fun getLayoutResource(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BINDING_VIEWS = DataBindingUtil.setContentView<T>(this, getLayoutResource())
        onCreate()
    }

    protected fun setStatusBarColor(@ColorInt colorInt: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = colorInt
        }
    }

    protected fun setNavigationBarColor(@ColorInt colorInt: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = colorInt
        }
    }

    @Suppress("LeakingThis")
    private val mRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = mRegistry
}

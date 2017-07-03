package com.asdevel.pataup.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.asdevel.pataup.R
import com.asdevel.pataup.arch.PataUpManager
import org.jetbrains.anko.dip

/**
 * Created by @Fredy.
 */
class QualityGraph(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var pathOfLinePoints: Path = Path()

    private var containerRect: Rect = Rect()

    var elapsedTimes: LongArray = longArrayOf()
        set(value) {
            field = value
            invalidate()
        }

    private val linePaint: Paint by lazy {
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.style = Paint.Style.STROKE
        p.color = resources.getColor(R.color.whiteColor)
        p.strokeWidth = dip(2).toFloat()
        p
    }

    private val circlePaint: Paint by lazy {
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.style = Paint.Style.FILL
        p.color = resources.getColor(R.color.whiteColor)
        p
    }

    private val MAX: Int = 15_000 //15 secs

    private fun getPointY(v: Long): Float {
        val value : Float = if (MAX - v < 0) MAX.toFloat() else (MAX - v).toFloat()
        var y: Float = containerRect.bottom - value / MAX * (containerRect.bottom - containerRect.top)
        y = Math.max(y, containerRect.top.toFloat())
        y = Math.min(y, containerRect.bottom.toFloat())
        return y
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateRect()
    }

    fun itemSpaceWidth(): Int = width / PataUpManager.MAX_ELAPSED_TIMES_COUNT

    private fun updateRect() {
        containerRect.left = 0
        containerRect.top = 0
        containerRect.right = width
        containerRect.bottom = height
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        pathOfLinePoints.reset()
        if (elapsedTimes.isNotEmpty())
            pathOfLinePoints.moveTo(0f, getPointY(elapsedTimes[0]))

        elapsedTimes.forEachIndexed { index, item ->
            pathOfLinePoints.lineTo((itemSpaceWidth() * index).toFloat(), getPointY(item))
        }

        canvas?.drawPath(pathOfLinePoints, linePaint)
    }

}
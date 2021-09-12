package com.example.paint

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

class SurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paints = List(5) {
        Paint().apply {
            strokeWidth = 5f
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
    }
    private val paths = mutableListOf<Pair<Path, Paint>>()

    private var index = 0
    private var currentPaintIndex = 0

    fun undo() {
        if (index - 1 < 0) return
        index--
        postInvalidate()
    }

    fun redo() {
        if (index + 1 > paths.size) return
        index++
        postInvalidate()
    }

    init {
        paints.forEachIndexed { index, paint ->
            paint.color = ContextCompat.getColor(context, PaintView.colors[index])
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        val pointX = event.x
        val pointY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val path = Path().apply { moveTo(pointX, pointY) }
                if (paths.size > index) {
                    paths.removeAll(paths.subList(index, paths.size))
                }
                paths.add(path to paints[currentPaintIndex])
                index = paths.size
            }
            MotionEvent.ACTION_MOVE -> {
                paths.last().first.lineTo(pointX, pointY)
            }
            else -> {
                return false
            }
        }

        postInvalidate()

        return true

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in 0 until index) {
            val (path, paint) = paths[i]
            canvas?.drawPath(path, paint)
        }
    }

    fun setColor(index: Int) {
        currentPaintIndex = index
    }

}
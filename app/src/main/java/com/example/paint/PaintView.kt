package com.example.paint

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat

class PaintView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val buttonSize by lazy {
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BUTTON_SIZE, resources.displayMetrics).toInt()
    }

    private val margin by lazy {
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN, resources.displayMetrics).toInt()
    }

    private val paintButtons = mutableListOf<Button>()
    private val btnUndo: ImageView
    private val btnRedo: ImageView

    private val surface = SurfaceView(context, attrs, defStyleAttr).apply {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
            topMargin = margin
        }
    }

    init {
        orientation = VERTICAL

        val linearLayout = LinearLayoutCompat(context, attrs, defStyleAttr).apply {
            orientation = HORIZONTAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }

        colors.forEachIndexed { index, color ->
            val button = createButton(context, attrs, defStyleAttr).apply {
                setBackgroundResource(color)
                setOnClickListener {
                    surface.setColor(index)
                }
            }
            paintButtons.add(button)
            linearLayout.addView(button)
        }

        btnUndo = createImageButton(context, attrs, defStyleAttr).apply {
            layoutParams = (layoutParams as LayoutParams).apply {
                leftMargin = margin
            }
            setImageResource(android.R.drawable.ic_menu_rotate)
            rotationY = ROTATION
            setOnClickListener {
                surface.undo()
            }
        }
        linearLayout.addView(btnUndo)
        btnRedo = createImageButton(context, attrs, defStyleAttr).apply {
            layoutParams = (layoutParams as LayoutParams).apply {
                leftMargin = margin
            }
            setImageResource(android.R.drawable.ic_menu_rotate)
            setOnClickListener {
                surface.redo()
            }
        }

        linearLayout.addView(btnRedo)
        addView(linearLayout)
        addView(surface)
    }

    private fun createButton(context: Context, attrs: AttributeSet?, defStyleAttr: Int) =
        Button(context, attrs, defStyleAttr).apply {
            layoutParams = LayoutParams(buttonSize, buttonSize)
        }

    private fun createImageButton(context: Context, attrs: AttributeSet?, defStyleAttr: Int) =
        ImageView(context, attrs, defStyleAttr).apply {
            layoutParams = LayoutParams(buttonSize, buttonSize)
        }

    companion object {
        private const val BUTTON_SIZE = 48f
        private const val MARGIN = 16f
        private const val ROTATION = 180f
        val colors = listOf(
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.color4,
            R.color.color5
        )
    }
}
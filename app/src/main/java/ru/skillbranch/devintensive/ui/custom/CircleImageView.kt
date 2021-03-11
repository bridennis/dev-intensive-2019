package ru.skillbranch.devintensive.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.graphics.drawable.toBitmap
import ru.skillbranch.devintensive.R


@SuppressLint("AppCompatCustomView")
class CircleImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.YELLOW
        private const val DEFAULT_BORDER_WIDTH = 2
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH
    private lateinit var bitmap: Bitmap

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        if (attrs != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderWidth = attributes.getDimensionPixelSize(
                    R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH
            )
            borderColor = attributes.getColor(
                    R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR
            )
            attributes.recycle()
        }

        borderPaint.color = borderColor
    }

    override fun onDraw(canvas: Canvas?) {
        if (drawable == null || width == 0 || height == 0 || canvas == null) {
            return
        }

        bitmap = getCenterCroppedBitmap(
            drawable.toBitmap(width - borderWidth * 2, height - borderWidth * 2)
        )

        val bitmapCenter = bitmap.width.toFloat() / 2

        if (borderWidth > 0) {
            canvas.drawCircle(
                    (width / 2).toFloat(),
                    (width / 2).toFloat(),
                    bitmapCenter + borderWidth / 2,
                    borderPaint
            )
        }

        canvas.drawBitmap(
                bitmap,
                ((width - bitmap.width) / 2).toFloat(),
                ((height - bitmap.height) / 2).toFloat(),
                circlePaint
        )
    }

    private fun getCenterCroppedBitmap(bitmap: Bitmap): Bitmap {
        return if (bitmap.width >= bitmap.height) {
            Bitmap.createBitmap(
                    bitmap,
                    bitmap.width / 2 - bitmap.height / 2,
                    0,
                    bitmap.height,
                    bitmap.height
            )
        } else {
            Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.height / 2 - bitmap.width / 2,
                    bitmap.width,
                    bitmap.width
            )
        }
    }

    @Dimension
    fun getBorderWidth(): Int {
        return borderWidth
    }

    fun setBorderWidth(@Dimension dp: Int) {
        borderWidth = dp
    }

    fun getBorderColor(): Int {
        return borderColor
    }

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = colorId
    }
}

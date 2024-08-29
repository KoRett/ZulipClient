package com.korett.zulip_client.core.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.korett.zulip_client.R
import com.korett.zulip_client.core.ui.extension.sp

/**
 * @author r.s.kashentsev
 */
class EmojiView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = R.attr.emojiViewStyle,
    defTheme: Int = 0
) : View(context, attributeSet, defStyle, defTheme) {

    var emoji: String = DEFAULT_EMOJI
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var count: Int = DEFAULT_COUNT
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    private val textPaint = TextPaint().apply {
        color = DEFAULT_TEXT_COLOR
        textSize = DEFAULT_TEXT_SIZE
    }

    var textColor
        get() = textPaint.color
        set(value) {
            if (textPaint.color != value) {
                textPaint.color = value
                invalidate()
            }
        }

    var textSize
        get() = textPaint.textSize
        set(value) {
            if (textPaint.textSize != value) {
                textPaint.textSize = value
                requestLayout()
            }
        }

    private val textToDraw
        get() = "${this.emoji} ${this.count}"

    private val textRect = Rect()

    init {
        context.withStyledAttributes(attributeSet, R.styleable.EmojiView, defStyle) {
            count = getInt(R.styleable.EmojiView_count, DEFAULT_COUNT)
            emoji = getString(R.styleable.EmojiView_emoji) ?: DEFAULT_EMOJI
            textPaint.color = getColor(R.styleable.EmojiView_android_textColor, DEFAULT_TEXT_COLOR)
            textPaint.textSize =
                getDimension(R.styleable.EmojiView_android_textSize, DEFAULT_TEXT_SIZE.sp(context))
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(textToDraw, 0, textToDraw.length, textRect)

        val textWidth = textRect.left + textRect.right

        val actualWidth =
            resolveSize(paddingStart + paddingEnd + textWidth, widthMeasureSpec)
        val actualHeight =
            resolveSize(paddingTop + paddingBottom + textRect.height(), heightMeasureSpec)

        setMeasuredDimension(actualWidth, actualHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val topOffset = (height + paddingTop - paddingBottom) / 2 - textRect.exactCenterY()

        canvas.drawText(textToDraw, paddingStart.toFloat(), topOffset, textPaint)
    }

    companion object {
        private const val DEFAULT_EMOJI = "\uD83D\uDE01"
        private const val DEFAULT_COUNT = 0
        private const val DEFAULT_TEXT_COLOR = Color.BLACK
        private const val DEFAULT_TEXT_SIZE = 16f
    }
}
package com.korett.zulip_client.core.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import com.korett.zulip_client.R
import com.korett.zulip_client.core.ui.extension.dp


/**
 * @author r.s.kashentsev
 */
class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0
) : ViewGroup(context, attributeSet, defStyle, defTheme) {

    var horizontalSpacing = DEFAULT_SPACING.dp(context)
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var verticalSpacing = DEFAULT_SPACING.dp(context)
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    init {
        context.withStyledAttributes(attributeSet, R.styleable.FlexBoxLayout) {
            horizontalSpacing = getDimension(
                R.styleable.FlexBoxLayout_horizontal_spacing,
                DEFAULT_SPACING.dp(context).toFloat()
            ).toInt()
            verticalSpacing = getDimension(
                R.styleable.FlexBoxLayout_vertical_spacing,
                DEFAULT_SPACING.dp(context).toFloat()
            ).toInt()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val maxWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingStart - paddingEnd

        var desiredWidth = 0
        var desiredHeight = 0
        var currentWidth = 0
        var maxViewHeight = 0

        children.forEach {
            if (currentWidth != 0) {
                currentWidth += horizontalSpacing
            }

            if (currentWidth + it.measuredWidth > maxWidth) {
                desiredHeight += maxViewHeight
                if (currentWidth != 0) {
                    desiredHeight += verticalSpacing
                }

                currentWidth -= horizontalSpacing
                if (currentWidth > desiredWidth) {
                    desiredWidth = currentWidth
                }

                maxViewHeight = 0
                currentWidth = 0
            }

            if (it.measuredHeight > maxViewHeight) {
                maxViewHeight = it.measuredHeight
            }

            currentWidth += it.measuredWidth
        }

        if (childCount != 0) {
            val lastView = getChildAt(childCount - 1)
            desiredHeight += maxOf(lastView.measuredHeight, maxViewHeight)
            desiredWidth = maxOf(currentWidth, minOf(desiredWidth, maxWidth))
        }

        val actualWidth =
            resolveSize(paddingStart + paddingEnd + desiredWidth, widthMeasureSpec)

        val actualHeight =
            resolveSize(paddingTop + paddingBottom + desiredHeight, heightMeasureSpec)

        setMeasuredDimension(actualWidth, actualHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var width = paddingStart
        var height = paddingTop
        var maxHeightViewInRow = 0

        children.forEach {
            if (width != paddingStart) {
                width += horizontalSpacing
            }

            if (width + it.measuredWidth > measuredWidth) {
                height += maxHeightViewInRow
                if (width != paddingStart) {
                    height += verticalSpacing
                }

                width = paddingStart
                maxHeightViewInRow = 0
            }

            if (it.measuredHeight > maxHeightViewInRow) {
                maxHeightViewInRow = it.measuredHeight
            }

            val leftLayout = width
            val rightLayout = leftLayout + it.measuredWidth
            val topLayout = height
            val bottomLayout = topLayout + it.measuredHeight

            it.layout(leftLayout, topLayout, rightLayout, bottomLayout)

            width = rightLayout
        }
    }

    companion object {
        private const val DEFAULT_SPACING = 10
    }
}
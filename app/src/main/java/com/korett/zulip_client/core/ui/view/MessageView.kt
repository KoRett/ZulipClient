package com.korett.zulip_client.core.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.google.android.material.imageview.ShapeableImageView
import com.korett.zulip_client.R


/**
 * @author r.s.kashentsev
 */
class MessageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle, defTheme) {

    val imageAvatar: ShapeableImageView
        get() = getChildAt(0) as ShapeableImageView

    private val textContainer: LinearLayout
        get() = getChildAt(1) as LinearLayout

    val textUsername: TextView
        get() = textContainer.getChildAt(0) as TextView

    val textContent: TextView
        get() = textContainer.getChildAt(1) as TextView

    val reactions: FlexBoxLayout
        get() = getChildAt(2) as FlexBoxLayout

    init {
        inflate(context, R.layout.layout_another_message, this)

        context.withStyledAttributes(attributeSet, R.styleable.MessageLayout) {
            imageAvatar.setImageResource(
                getResourceId(
                    R.styleable.MessageLayout_avatar_image,
                    DEFAULT_AVATAR
                )
            )
            textUsername.text = getString(R.styleable.MessageLayout_username) ?: DEFAULT_USERNAME
            textContent.text = getString(R.styleable.MessageLayout_message_text) ?: DEFAULT_MESSAGE
        }
    }

    companion object {
        private const val DEFAULT_USERNAME = "Username"
        private const val DEFAULT_MESSAGE = "Some message"
        private val DEFAULT_AVATAR = R.drawable.person_ic
    }
}
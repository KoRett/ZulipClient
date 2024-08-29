package com.korett.zulip_client.core.common.extension

import android.text.Html
import android.text.SpannableStringBuilder
import android.text.SpannedString
import androidx.core.text.toSpanned

fun SpannableStringBuilder.trimSpannable(): SpannableStringBuilder {
    var trimStart = 0
    var trimEnd = 0
    var text = this.toString()

    while (text.isNotEmpty() && text.startsWith("\n")) {
        text = text.substring(1)
        trimStart += 1
    }

    while (text.isNotEmpty() && text.endsWith("\n")) {
        text = text.substring(0, text.length - 1)
        trimEnd += 1
    }

    return this.delete(0, trimStart).delete(this.length - trimEnd, this.length)
}

fun String.fromHtmlToSpannedString(): SpannedString {
    val spanned = Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
    val spannableStringBuilder = SpannableStringBuilder(spanned).trimSpannable()
    return SpannedString(spannableStringBuilder.toSpanned())
}
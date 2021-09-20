package com.tolodev.reddit.extensions

import android.os.Build
import android.text.Html
import android.text.Html.fromHtml
import android.text.Spanned
import androidx.navigation.NavController
import androidx.navigation.NavDirections

internal inline fun <T, U, R> Pair<T?, U?>.biLet(body: (T, U) -> R): R? {
    val first = first
    val second = second
    if (first != null && second != null) {
        return body(first, second)
    }
    return null
}

internal inline fun <T, U, E, R> Triple<T?, U?, E?>.triLet(body: (T, U, E) -> R): R? {
    val first = first
    val second = second
    val third = third
    if (first != null && second != null && third != null) {
        return body(first, second, third)
    }
    return null
}

internal fun setTextFromHtml(text: String): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
    } else {
        fromHtml(text)
    }
}

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}

package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager

private const val KEYBOARD_VISIBLE_THRESHOLD_DP = 100

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.isKeyboardOpen(): Boolean {
    fun convertDpToPx(value: Int): Int =
            (value * Resources.getSystem().displayMetrics.density).toInt()

    val rootView = findViewById<View>(android.R.id.content)
    val visibleThreshold = Rect()
    rootView.getWindowVisibleDisplayFrame(visibleThreshold)
    val heightDiff = rootView.height - visibleThreshold.height()

    val accessibleValue = convertDpToPx(KEYBOARD_VISIBLE_THRESHOLD_DP)

    return heightDiff > accessibleValue
}

fun Activity.isKeyboardClosed(): Boolean {
    return isKeyboardOpen().not()
}

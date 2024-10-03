package com.kappdev.nitrixtesttask.ui.util


import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * A composable that controls the visibility of the system bars (status bar and navigation bar).
 *
 * The composable uses the [WindowInsetsControllerCompat] to show or hide the system bars based
 * on the `isSystemBarsVisible` parameter. It also ensures that the system bars are shown when the
 * composable leaves the composition.
 *
 * @param isSystemBarsVisible Determines whether the system bars are currently visible or not.
 */
@Composable
fun SystemBarsController(
    isSystemBarsVisible: Boolean
) {
    val view = LocalView.current
    val window = (view.context as Activity).window
    val insetsController = WindowCompat.getInsetsController(window, view)

    DisposableEffect(Unit) {
        onDispose {
            insetsController.showSystemBars()
        }
    }

    LaunchedEffect(isSystemBarsVisible) {
        if (!view.isInEditMode) {
            if (!isSystemBarsVisible) {
                insetsController.hideSystemBars()
            } else {
                insetsController.showSystemBars()
            }
        }
    }
}

private fun WindowInsetsControllerCompat.hideSystemBars() {
    hide(WindowInsetsCompat.Type.systemBars())
    systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
}

private fun WindowInsetsControllerCompat.showSystemBars() {
    show(WindowInsetsCompat.Type.systemBars())
}


package org.example.project

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

// Run this function to launch the application
fun main() = application {
    // Set the window size and title
    val windowState = rememberWindowState(size = DpSize(600.dp, 650.dp))
    Window(
        onCloseRequest = ::exitApplication,
        title = "Kotlin Multiplatform Compose Tic Tac Toe",
        state = windowState
    ) {
        App()  // This is the entry point for the Compose UI
    }
}
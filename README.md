# Tic-Tac-Toe

A Tic-Tac-Toe desktop app built with Kotlin and Compose Multiplatform (JVM).

## Features

- **Mode selection screen** — choose how to play at startup
- **Two Players** — local two-player hotseat mode
- **vs. Computer** — play as X against an unbeatable minimax AI
- Status bar showing whose turn it is, or "Computer is thinking..."
- Win and draw detection — winning cells highlight green, dialog appears after a 1-second delay
- Back to Menu button to return to mode selection without restarting the app

## Project Structure

All game logic lives in a single file:
`composeApp/src/jvmMain/kotlin/org/example/project/App.kt`

Key components:
- `ModeSelectionScreen` — startup screen
- `TTTGame` — game board, state, and dialogs
- `checkWinnerForBoard` / `minimaxScore` / `findBestMove` — AI logic

## Build and Run

Run from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:run
  ```

Or build a native distributable:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:runDistributable
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:runDistributable
  ```

---

Built with [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html) and [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/).
# CLAUDE.md

## Project

Tic-Tac-Toe desktop app — Kotlin + Compose Multiplatform (JVM only).

## Key File

All code is in one file:
`composeApp/src/jvmMain/kotlin/org/example/project/App.kt`

## Run Commands

```shell
./gradlew :composeApp:run              # dev run (keep process attached)
./gradlew :composeApp:runDistributable # build + launch native exe
```

On Windows use `.\gradlew.bat` instead.

## Architecture

- `App()` — owns navigation state (`gameMode: GameMode?`); renders `ModeSelectionScreen` or `TTTGame`
- `ModeSelectionScreen` — startup screen with Two Players / vs. Computer buttons
- `TTTGame(gameMode, onBackToMenu)` — full game: board state, click handler, dialogs, `LaunchedEffect` for AI turn
- `GridItem` — single cell composable (unchanged)
- `checkWinnerForBoard` / `minimaxScore` / `findBestMove` — pure top-level functions for win detection and minimax AI

## Dependencies

- `kotlinx-coroutines` (Swing dispatcher) is already in `composeApp/build.gradle.kts` — needed for `LaunchedEffect` + `delay` in the AI move
- Material3 for UI components; legacy `androidx.compose.material.Card` still used for the grid cells

## Notes

- `composeApp/src/save/` contains scratch/save files (`AppX.kt`, `AppY.kt`) — not part of the build, do not commit
- The minimax AI is unbeatable; no alpha-beta pruning needed (3×3 search space is trivially fast on JVM)
- Setting `gameMode = null` in `App()` discards all `TTTGame` `remember` state automatically

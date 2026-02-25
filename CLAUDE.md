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
- `TTTGame(gameMode, onBackToMenu)` — full game: board state, click handler, dialogs, two `LaunchedEffect`s (AI turn; win-dialog delay)
- `GridItem(isHighlighted)` — single cell composable; turns green when part of the winning line
- `findWinningLine` — returns the three winning indices (or null); `checkWinnerForBoard` delegates to it
- `minimaxScore` / `findBestMove` — pure top-level functions for minimax AI

### Win flow
1. Move placed → `findWinningLine` called → `winningLine` state set → cells highlight green immediately
2. `LaunchedEffect(winningLine.value)` waits 1 second → sets `winner` → dialog appears
3. Clicks are blocked while `winningLine != null` (covers the 1-second gap)

## Dependencies

- `kotlinx-coroutines` (Swing dispatcher) is already in `composeApp/build.gradle.kts` — needed for `LaunchedEffect` + `delay` in the AI move
- Material3 for UI components; legacy `androidx.compose.material.Card` still used for the grid cells

## Notes

- `composeApp/src/save/` contains scratch/save files (`AppX.kt`, `AppY.kt`) — not part of the build, do not commit
- The minimax AI is unbeatable; no alpha-beta pruning needed (3×3 search space is trivially fast on JVM)
- Setting `gameMode = null` in `App()` discards all `TTTGame` `remember` state automatically

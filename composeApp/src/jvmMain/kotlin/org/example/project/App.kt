package org.example.project

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.system.exitProcess
import kotlinx.coroutines.delay


enum class GameMode { TWO_PLAYER, VS_COMPUTER }


fun checkWinnerForBoard(board: List<String>): String? {
    for (row in 0..2) {
        val s = row * 3
        if (board[s].isNotEmpty() && board[s] == board[s+1] && board[s] == board[s+2]) return board[s]
    }
    for (col in 0..2) {
        if (board[col].isNotEmpty() && board[col] == board[col+3] && board[col] == board[col+6]) return board[col]
    }
    if (board[0].isNotEmpty() && board[0] == board[4] && board[0] == board[8]) return board[0]
    if (board[2].isNotEmpty() && board[2] == board[4] && board[2] == board[6]) return board[2]
    return null
}


fun minimaxScore(board: List<String>, isMaximizing: Boolean): Int {
    val winner = checkWinnerForBoard(board)
    if (winner == "O") return 10
    if (winner == "X") return -10
    if (board.all { it.isNotEmpty() }) return 0
    return if (isMaximizing) {
        var best = Int.MIN_VALUE
        for (i in board.indices) {
            if (board[i].isEmpty()) {
                val b = board.toMutableList().also { it[i] = "O" }
                best = maxOf(best, minimaxScore(b, false))
            }
        }
        best
    } else {
        var best = Int.MAX_VALUE
        for (i in board.indices) {
            if (board[i].isEmpty()) {
                val b = board.toMutableList().also { it[i] = "X" }
                best = minOf(best, minimaxScore(b, true))
            }
        }
        best
    }
}


fun findBestMove(board: List<String>): Int {
    var bestScore = Int.MIN_VALUE
    var bestIndex = -1
    for (i in board.indices) {
        if (board[i].isEmpty()) {
            val b = board.toMutableList().also { it[i] = "O" }
            val score = minimaxScore(b, false)
            if (score > bestScore) { bestScore = score; bestIndex = i }
        }
    }
    return bestIndex
}


@Composable
fun App() {
    MaterialTheme {
        var gameMode by remember { mutableStateOf<GameMode?>(null) }
        if (gameMode == null) {
            ModeSelectionScreen(onModeSelected = { gameMode = it })
        } else {
            TTTGame(gameMode = gameMode!!, onBackToMenu = { gameMode = null })
        }
    }
}


@Composable
fun ModeSelectionScreen(onModeSelected: (GameMode) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tic-Tac-Toe", style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(48.dp))
        Text("Select Game Mode", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { onModeSelected(GameMode.TWO_PLAYER) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Two Players") }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onModeSelected(GameMode.VS_COMPUTER) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("vs. Computer") }
    }
}


@Composable
fun TTTGame(gameMode: GameMode, onBackToMenu: () -> Unit) {
    val itemsList = remember { mutableStateListOf(*(1..9).map { "" }.toTypedArray()) }
    val clickCount = remember { mutableStateOf(0) }
    val showErrorDialog = remember { mutableStateOf(false) }
    val winner = remember { mutableStateOf<String?>(null) }
    val isDraw = remember { mutableStateOf(false) }
    val isComputerTurn = remember { mutableStateOf(false) }

    fun checkWinner() = checkWinnerForBoard(itemsList)
    fun checkDraw() = itemsList.all { it.isNotEmpty() } && checkWinner() == null

    val resetGame = {
        itemsList.clear()
        itemsList.addAll(List(9) { "" })
        clickCount.value = 0
        isComputerTurn.value = false
    }

    LaunchedEffect(isComputerTurn.value) {
        if (isComputerTurn.value && winner.value == null && !isDraw.value) {
            delay(500L)
            val best = findBestMove(itemsList.toList())
            if (best != -1) {
                clickCount.value++
                itemsList[best] = "O"
                val w = checkWinner()
                if (w != null) winner.value = w else if (checkDraw()) isDraw.value = true
            }
            isComputerTurn.value = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        val statusText = when {
            gameMode == GameMode.VS_COMPUTER && isComputerTurn.value -> "Computer is thinking..."
            gameMode == GameMode.VS_COMPUTER -> "Your turn (X)"
            clickCount.value % 2 == 0 -> "Player X's turn"
            else -> "Player O's turn"
        }
        Text(
            text = statusText,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f).padding(16.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(itemsList.size) { index ->
                val item = itemsList[index]
                val x = index % 3
                val y = index / 3
                GridItem(item, x, y) {
                    if (gameMode == GameMode.VS_COMPUTER && isComputerTurn.value) return@GridItem
                    if (itemsList[index].isNotEmpty()) {
                        showErrorDialog.value = true
                    } else {
                        clickCount.value++
                        itemsList[index] = if (clickCount.value % 2 == 1) "X" else "O"
                        val w = checkWinner()
                        if (w != null) {
                            winner.value = w
                        } else if (checkDraw()) {
                            isDraw.value = true
                        } else if (gameMode == GameMode.VS_COMPUTER) {
                            isComputerTurn.value = true
                        }
                    }
                }
            }
        }
    }

    if (showErrorDialog.value) {
        AlertDialog(
            onDismissRequest = { showErrorDialog.value = false },
            title = { Text("Invalid Move") },
            text = { Text("This cell is already occupied!") },
            confirmButton = {
                Button(onClick = { showErrorDialog.value = false }) {
                    Text("OK")
                }
            }
        )
    }

    if (winner.value != null) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Game Over!") },
            text = {
                val msg = if (gameMode == GameMode.VS_COMPUTER)
                    if (winner.value == "X") "You win!" else "Computer wins!"
                else "Player ${winner.value} wins!"
                Text(msg)
            },
            confirmButton = {
                Button(onClick = {
                    winner.value = null
                    resetGame()
                }) {
                    Text("Play Again")
                }
            },
            dismissButton = {
                Column {
                    Button(onClick = { winner.value = null; resetGame(); onBackToMenu() }) {
                        Text("Back to Menu")
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { exitProcess(0) }) {
                        Text("Quit")
                    }
                }
            }
        )
    }

    if (isDraw.value) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Game Over!") },
            text = { Text("It's a draw! No one wins.") },
            confirmButton = {
                Button(onClick = {
                    isDraw.value = false
                    resetGame()
                }) {
                    Text("Play Again")
                }
            },
            dismissButton = {
                Column {
                    Button(onClick = { isDraw.value = false; resetGame(); onBackToMenu() }) {
                        Text("Back to Menu")
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { exitProcess(0) }) {
                        Text("Quit")
                    }
                }
            }
        )
    }
}


@Composable
fun GridItem(item: String, x: Int, y: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .border(
                width = 2.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable {
                println("Clicked on $item at location: x=$x, y=$y")
                onClick()
            },
        elevation = 4.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = item,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.displayLarge
            )
        }
    }
}

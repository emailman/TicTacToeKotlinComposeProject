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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.system.exitProcess


@Composable
fun App() {
    MaterialTheme {
        GridExample()
    }
}

@Composable
fun GridExample() {
    val itemsList = remember { mutableStateListOf(*(1..9).map { "" }.toTypedArray()) } // Sample data
    val clickCount = remember { mutableStateOf(0) }
    val showErrorDialog = remember { mutableStateOf(false) }
    val winner = remember { mutableStateOf<String?>(null) }

    fun checkWinner(): String? {
        // Check rows
        for (row in 0..2) {
            val startIndex = row * 3
            if (itemsList[startIndex].isNotEmpty() &&
                itemsList[startIndex] == itemsList[startIndex + 1] &&
                itemsList[startIndex] == itemsList[startIndex + 2]) {
                return itemsList[startIndex]
            }
        }

        // Check columns
        for (col in 0..2) {
            if (itemsList[col].isNotEmpty() &&
                itemsList[col] == itemsList[col + 3] &&
                itemsList[col] == itemsList[col + 6]) {
                return itemsList[col]
            }
        }

        // Check diagonal (top-left to bottom-right)
        if (itemsList[0].isNotEmpty() &&
            itemsList[0] == itemsList[4] &&
            itemsList[0] == itemsList[8]) {
            return itemsList[0]
        }

        // Check diagonal (top-right to bottom-left)
        if (itemsList[2].isNotEmpty() &&
            itemsList[2] == itemsList[4] &&
            itemsList[2] == itemsList[6]) {
            return itemsList[2]
        }

        return null
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // Fixed 3 columns
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(itemsList.size) { index ->
            val item = itemsList[index]
            val x = index % 3 // Column index (0, 1, 2)
            val y = index / 3 // Row index (0, 1, ...)
            GridItem(item, x, y) {
                if (itemsList[index].isNotEmpty()) {
                    showErrorDialog.value = true
                } else {
                    clickCount.value++
                    itemsList[index] = if (clickCount.value % 2 == 1) "X" else "O"
                    
                        val winnerResult = checkWinner()
                        if (winnerResult != null) {
                            winner.value = winnerResult
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
                onDismissRequest = { winner.value = null },
                title = { Text("Game Over!") },
                text = { Text("Player ${winner.value} wins!") },
                confirmButton = {
                    Button(onClick = { 
                        winner.value = null
                        // Reset the game
                        itemsList.clear()
                        itemsList.addAll(List(9) { "" })
                        clickCount.value = 0
                    }) {
                        Text("Play Again")
                    }
                },
                    dismissButton = {
                        Button(onClick = { exitProcess(0) }) {
                            Text("Exit")
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
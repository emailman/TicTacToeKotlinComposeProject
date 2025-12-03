package org.example.project

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    MaterialTheme {
        TTTGame()
    }
}

@Composable
fun TTTGame() {
    val itemsList = remember {
        mutableStateListOf(
            *(1..9).map { "?" }.toTypedArray()
        )
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
            val y = index / 3 // Row index (0, 1, 2)
            GridItem(item, x, y) {
            }
        }
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
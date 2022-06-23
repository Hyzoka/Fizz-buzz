package com.example.fizz_buzz

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fizz_buzz.ui.theme.FizzbuzzTheme

class MainActivity : ComponentActivity() {

    private var listFinal = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FizzbuzzTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BodyContent(modifier =Modifier,changeMultiples(3,5,10,"Fizz","Buzz"))
                }
            }
        }
    }


    fun changeMultiples(int1 : Int, int2 : Int,limit : Int, str1 : String, str2 : String) : List<String>{
        for (i in 0..limit){
            if (checkMultiples(int1,i)){
                listFinal.add(str1)
            }
            else if (checkMultiples(int2,i)){
                listFinal.add(str2)
            }
            else if (checkMultiples(int2,i) && checkMultiples(int2,i)){
                listFinal.add("$str1$str2")
            }
            else{
                listFinal.add(i.toString())
            }
        }
        return listFinal
    }

    fun checkMultiples(mutiple : Int, target : Int) : Boolean {
        return target % mutiple == 0
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier,list: List<String>) {
    Column() {
        StaggeredGrid {
            for (topic in list) {
                Chip(modifier = Modifier.padding(8.dp), text = topic)
            }
        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}
@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    colum: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        // Keep track of the width of each row
        val columWidths = IntArray(colum) { 0 }

        // Keep track of the max height of each row
        val columHeights = IntArray(colum) { 0 }

        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.mapIndexed { index, measurable ->

            // Measure each child
            val placeable = measurable.measure(constraints)

            // Track the width and max height of each row
            val row = index % colum
            columWidths[row] += placeable.width
            columHeights[row] = Math.max(columHeights[row], placeable.height)

            placeable
        }

        // Grid's width is the widest row
        val width = columWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        // Grid's height is the sum of the tallest element of each row
        // coerced to the height constraints
        val height = columHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        // Y of each row, based on the height accumulation of previous rows
        val rowY = IntArray(colum) { 0 }
        for (i in 1 until colum) {
            rowY[i] = rowY[i-1] + columWidths[i-1]
        }

        // Set the size of the parent layout
        layout(width, height) {
            // x cord we have placed up to, per row
            val rowX = IntArray(colum) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % colum
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FizzbuzzTheme {
        Greeting("Android")
    }
}
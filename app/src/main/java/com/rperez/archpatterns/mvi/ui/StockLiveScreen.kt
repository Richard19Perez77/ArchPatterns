package com.rperez.archpatterns.mvi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.rperez.archpatterns.mvi.model.StockIntent
import com.rperez.archpatterns.mvi.viewmodel.StockViewModel
import java.util.Locale

@Composable
fun StockLiveScreen(vm: StockViewModel = viewModel()) {
    val state by vm.state.collectAsState()

    var chartMinY by remember { mutableFloatStateOf(0f) }
    var chartMaxY by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (state.isConnected) {
            Text("Connected", color = Color(0xFF006400))
        } else {
            Button(onClick = { vm.process(StockIntent.Connect) }) {
                Text("Connect")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            vm.process(StockIntent.SubscribeToSymbol("TSM"))
        }) {
            Text("Subscribe to TSM")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "TSMC: ${state.prices.lastOrNull() ?: "Loading..."}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        val step = 20

        if (state.prices.isNotEmpty()) {
            val pointsData: List<Point> =
                state.prices.takeLast(step).mapIndexed { index, price ->
                    Point(index.toFloat() + 1, price)
                }
            val yValues = pointsData.map { it.y }
            chartMinY = yValues.minOrNull() ?: 167f
            chartMaxY = yValues.maxOrNull() ?: 168f
            val yRange = chartMaxY - chartMinY
            val yPadding = yRange * 0.1f  // 10% padding on top and bottom

            val xAxisData = AxisData.Builder()
                .axisStepSize(15.dp)
                .backgroundColor(Color.Blue)
                .steps(step)
                .labelData { i -> i.toString() }
                .labelAndAxisLinePadding(10.dp)
                .build()

            val yAxisData = AxisData.Builder()
                .steps(step)
                .backgroundColor(Color.Red)
                .labelAndAxisLinePadding(20.dp)
                .labelData { i ->
                    val stepValue = (chartMaxY - chartMinY + 2 * yPadding) / 2
                    String.format(
                        Locale.getDefault(),
                        "%.2f", chartMinY - yPadding + (i * stepValue)
                    )
                }
                .build()

            val lineChartData = LineChartData(
                linePlotData = LinePlotData(
                    lines = listOf(
                        Line(
                            dataPoints = pointsData,
                            lineStyle = LineStyle(),
                            intersectionPoint = IntersectionPoint(),
                            selectionHighlightPoint = SelectionHighlightPoint(),
                            shadowUnderLine = ShadowUnderLine(),
                            selectionHighlightPopUp = SelectionHighlightPopUp()
                        )
                    ),
                ),
                xAxisData = xAxisData,
                yAxisData = yAxisData,
                gridLines = GridLines(),
                backgroundColor = Color.White
            )

            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                lineChartData = lineChartData
            )
        }

        LazyColumn {
            items(state.prices.reversed()) { price ->
                Text("TSM: $price")
            }
        }

        state.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Error: $it", color = Color.Red)
        }
    }
}
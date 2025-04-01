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

    var chartMinY by remember { mutableFloatStateOf(900f) }
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
        val step = 50

        if (state.prices.isNotEmpty()) {
            var pointsData: List<Point> =
                state.prices.takeLast(step - 2).mapIndexed { index, price ->
                    Point(index.toFloat(), price)
                }
            val yValues = pointsData.map { it.y }
            chartMinY = minOf(chartMinY, yValues.minOrNull() ?: chartMinY)
            chartMaxY = maxOf(chartMaxY, yValues.maxOrNull() ?: chartMaxY)

            val temp = pointsData.toMutableList()
            temp.add(0, Point(-1f, chartMinY))
            temp.add(0, Point(-2f, chartMaxY))
            pointsData = temp.toList()

            val xAxisData = AxisData.Builder()
                .axisStepSize(15.dp)
                .backgroundColor(Color.Blue)
                .steps(step)
                .labelData { i -> i.toString() }
                .labelAndAxisLinePadding(10.dp)
                .build()

            val yAxisSteps = 10
            val yStepSize = (chartMaxY - chartMinY) / yAxisSteps

            val yAxisData = AxisData.Builder()
                .steps(yAxisSteps)
                .backgroundColor(Color.Red)
                .labelAndAxisLinePadding(12.dp)
                .labelData { i ->
                    String.format(Locale.getDefault(), "%.2f", chartMinY + (i * yStepSize))
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
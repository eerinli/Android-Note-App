import javafx.scene.paint.Color

// a helper class for LineChart with ResizableCanvas
class LineChart(private val model: Model) : ResizableCanvas(model) {
    override fun draw() {
        setupChart()
        chartWidth = width - 30
        val barWidth = (chartWidth) / (n - 1)
        val minValue = (model.currentDataSet.data.minOrNull() ?: -1).toDouble()
        val heightRatio = chartHeight / (maxValue - minValue)

        var lastY = 0.0
        var lastX = 15.0
        model.currentDataSet.data.forEachIndexed { i, it ->
            // last point to cur point
            if (i != 0) {
                gc.strokeLine(
                    lastX, lastY,
                    lastX + barWidth,
                    height - 15 + minValue * heightRatio - heightRatio * it
                )
                lastX += barWidth
            }
            lastY = height - 15.0 + minValue * heightRatio - heightRatio * it
            gc.fill = Color.RED
            gc.fillOval(lastX - 2.5, lastY - 2.5, 5.0, 5.0)
        }
    }
}
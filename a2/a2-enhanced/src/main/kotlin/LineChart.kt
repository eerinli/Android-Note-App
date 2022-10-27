import javafx.scene.paint.Color
import java.lang.Double.max
import java.lang.Double.min

// a helper class for LineChart with ResizableCanvas
class LineChart(private val model: Model) : ResizableCanvas(model) {
    override fun draw() {
        println("redraw")
        setupChart()
        gc.stroke = Color.BLACK
        n = (model.currentDataSet.data.size).toDouble()
        var maxValue1 = (model.currentDataSet.data.maxOrNull() ?: -1).toDouble()
        val minValue1 = (model.currentDataSet.data.minOrNull() ?: -1).toDouble()
        var minValue = minValue1
        if (model.currentDataSet2.data.isEmpty()) {
            maxValue = maxValue1
        } else {
            var maxValue2 = (model.currentDataSet2.data.maxOrNull() ?: -1).toDouble()
            maxValue = max(maxValue1,maxValue2)
            val minValue2 = (model.currentDataSet2.data.minOrNull() ?: -1).toDouble()
            minValue = min(minValue1,minValue2)
        }

        val barWidth = (chartWidth) / (n - 1)
        val heightRatio = chartHeight / (maxValue - minValue)
        println("Max:"+ maxValue+ "Min"+minValue)
        if (model.currentDataSet2.data.isNotEmpty()) {
            val n2 = (model.currentDataSet2.data.size).toDouble()
            val barWidth2 = (chartWidth) / (n2 - 1)

            // dataset2
            var lastY2 = 0.0
            gc.stroke = Color.BLUE
            var lastX2 = 15.0
            model.currentDataSet2.data.forEachIndexed { i, it ->
                // last point to cur point
                if (i != 0) {
                    gc.strokeLine(
                        lastX2, lastY2,
                        lastX2 + barWidth2,
                        height - 15 + minValue * heightRatio - heightRatio * it
                    )
                    lastX2 += barWidth2
                }
                lastY2 = height - 15.0 + minValue * heightRatio - heightRatio * it
                gc.fill = Color.GREEN
                gc.fillOval(lastX2 - 2.5, lastY2 - 2.5, 5.0, 5.0)
            }
        }

        gc.stroke = Color.BLACK
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
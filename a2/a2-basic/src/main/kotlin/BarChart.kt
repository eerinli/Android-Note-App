import javafx.scene.paint.Color
import kotlin.math.*

// a helper class for BarChart with ResizableCanvas
class BarChart(private val model: Model) : ResizableCanvas(model) {
    override fun draw() {
        setupChart()

        val barWidth = (chartWidth - (n + 1) * 5.0) / n
        val minValue = (model.currentDataSet.data.minOrNull() ?: -1).toDouble()
        val heightRatio = (chartHeight) / (maxValue - min(0.0, minValue))

        gc.strokeLine(
            15.0,
            chartHeight + 15 + min(0.0, minValue) * heightRatio,
            width - 15,
            height - 15 + min(0.0, minValue) * heightRatio
        )

        model.currentDataSet.data.forEachIndexed { i, it ->
            gc.fill = Color.hsb(ratio * (i + 1), 1.0, 1.0)

            val y = if (it < 0) height - 15 + min(0.0, minValue) * heightRatio
            else height - 15 + min(0.0, minValue) * heightRatio - heightRatio * abs(it)

            gc.fillRect(10 + (i + 1) * 5 + i * barWidth, y, barWidth, heightRatio * abs(it))
        }
    }
}
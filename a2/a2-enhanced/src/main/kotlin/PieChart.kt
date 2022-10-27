import javafx.scene.paint.Color
import javafx.scene.shape.ArcType
import kotlin.math.*

// a class for PieChart with ResizableCanvas
class PieChart(private val model: Model) : ResizableCanvas(model) {
    override fun draw() {
        setupChart()
        val sum = model.currentDataSet.data.sum()
        val radius = min(chartWidth, chartHeight)
        val ratio = 360.0 / n

        var lastAngle = 0.0
        model.currentDataSet.data.forEachIndexed{ i, it ->
            gc.fill = Color.hsb(ratio *(i + 1), saturation, brightness)
            var x = width - radius - 10.0
            var y = height - radius - 10.0
            if (height > width) y -= (height - width) / 2
            else x +=  (height - width) / 2
            val offset = it * 360.0 / sum
            gc.fillArc(x, y, radius, radius, lastAngle, offset , ArcType.ROUND)
            lastAngle += offset
        }
    }
}
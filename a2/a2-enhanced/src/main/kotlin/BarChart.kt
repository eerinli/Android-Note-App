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
            gc.fill = Color.hsb(ratio * (i + 1), saturation+0.0, brightness+0.0)

            val y = if (it < 0) height - 15 + min(0.0, minValue) * heightRatio
            else height - 15 + min(0.0, minValue) * heightRatio - heightRatio * abs(it)

            gc.fillRect(10 + (i + 1) * 5 + i * barWidth, y, barWidth, heightRatio * abs(it))
        }

        // data box for mean and sem
        val mean = model.currentDataSet.data.sum() / n
        val meanString = "mean: " + String.format("%.2f", mean)
        val sem = calculateSD(model.currentDataSet.data, mean, n) / sqrt(n)
        val semString = "SEM: " + String.format("%.2f", sem)

        // semi-transparent box
        gc.fill = Color.WHITE
        gc.globalAlpha = 0.7
        gc.fillRect(
            20.0, 35.0,
            max(meanString.length, semString.length) * 8.0, 110.0
        )
        gc.globalAlpha = 1.0

        // text in box
        gc.fill = Color.BLACK
        gc.fillText(meanString, 25.0, 50.0)
        gc.fillText(semString, 25.0, 65.0)
        val sum = model.currentDataSet.data.sum()
        val sumString = "sum: " + String.format("%.2f", sum)
        val sd = calculateSD(model.currentDataSet.data, mean, n)
        val sdString = "SD: " + String.format("%.2f", sd)
        gc.fillText(sdString, 25.0, 80.0)
        gc.fillText("max: " + String.format("%.2f", maxValue), 25.0, 95.0)
        gc.fillText(sumString, 25.0, 110.0)
        val min = (model.currentDataSet.data.minOrNull() ?: -1).toDouble()
        gc.fillText("min: " + String.format("%.2f", min), 25.0, 125.0)
        val median = calculateMedian(model.currentDataSet.data)
        gc.fillText("median: " + String.format("%.2f", median), 25.0, 140.0)
    }
    private fun calculateSD(numArray: MutableList<Double>, mean:Double, n: Double): Double {
        var standardDeviation = 0.0
        for (num in numArray) {
            standardDeviation += (num - mean).pow(2.0)
        }
        return sqrt(standardDeviation / n)
    }

    private fun calculateMedian(numArray: MutableList<Double>): Double {
        val sorted = numArray.sorted()
        return if (sorted.size % 2 == 0) (sorted[sorted.size / 2] + sorted[(sorted.size - 1) / 2]) / 2
        else sorted[sorted.size / 2]
    }
}
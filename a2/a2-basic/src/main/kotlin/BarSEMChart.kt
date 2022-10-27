import javafx.scene.paint.Color
import kotlin.math.*

//  a helper class for BarChart with SEM and mean with ResizableCanvas
class BarSEMChart(private val model: Model) : ResizableCanvas(model) {
    override fun draw() {
        setupChart()
        // x axis
        gc.strokeLine(15.0, height - 15, width - 15, height - 15)

        // bar display
        val barWidth = (chartWidth - (n + 1) * 5.0) / n
        val heightRatio = chartHeight / maxValue

        model.currentDataSet.data.forEachIndexed { i, it ->
            gc.fill = Color.hsb(ratio * (i + 1), 1.0, 1.0)
            gc.fillRect(
                10 + (i + 1) * 5 + i * barWidth, height - 15.0 - heightRatio * it,
                barWidth, heightRatio * it
            )
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
            max(meanString.length, semString.length) * 7.3, 38.0
        )
        gc.globalAlpha = 1.0

        // text in box
        gc.fill = Color.BLACK
        gc.fillText(meanString, 25.0, 50.0)
        gc.fillText(semString, 25.0, 65.0)

        // mean line
        gc.setLineDashes(0.0)
        gc.strokeLine(
            15.0, height - 15.0 - heightRatio * mean,
            width - 15, height - 15.0 - heightRatio * mean
        )
        // sem lines
        gc.setLineDashes(7.0, 15.0)
        gc.strokeLine(
            15.0, height - 15.0 - heightRatio * (mean - sem),
            width - 15, height - 15.0 - heightRatio * (mean - sem)
        )
        gc.strokeLine(
            15.0, height - 15.0 - heightRatio * (mean + sem),
            width - 15, height - 15.0 - heightRatio * (mean + sem)
        )
    }

    private fun calculateSD(numArray: MutableList<Double>, mean:Double, n: Double): Double {
        var standardDeviation = 0.0
        for (num in numArray) {
            standardDeviation += (num - mean).pow(2.0)
        }
        return sqrt(standardDeviation / n)
    }
}
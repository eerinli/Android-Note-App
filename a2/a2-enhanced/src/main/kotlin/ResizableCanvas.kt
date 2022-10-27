import javafx.beans.Observable
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

// Define a class for canvas that can resize
abstract class ResizableCanvas(private val model: Model) : Canvas() {
    val gc: GraphicsContext = graphicsContext2D
    var n = 0.0
    var chartWidth = 0.0
    var chartHeight = 0.0
    var maxValue = 0.0
    var ratio = 0.0

    var saturation = 0.0
    var brightness = 0.0

    init {
        // Redraw canvas when size changes.
        widthProperty().addListener { _: Observable? -> draw() }
        heightProperty().addListener { _: Observable? -> draw() }
    }

    // draw the chart
    open fun draw() {}

    // setup background and common calculated data
    fun setupChart() {
        // background
        gc.fill = Color.WHITE
        gc.fillRect(0.0, 0.0, width, height)

        //  update width and height
        val width = width
        val height = height

        // useful values
        n = (model.currentDataSet.data.size).toDouble()
        chartWidth = width - 20
        chartHeight = height - 30
        maxValue = (model.currentDataSet.data.maxOrNull() ?: -1).toDouble()
        ratio = 360.0 / n

        gc.lineWidth = 0.5
        gc.setLineDashes(0.0)

        // enhanced
        saturation = model.saturation
        brightness = model.brightness
    }

    override fun isResizable(): Boolean {
        return true
    }

    override fun prefWidth(height: Double): Double {
        return width
    }

    override fun prefHeight(width: Double): Double {
        return height
    }
}
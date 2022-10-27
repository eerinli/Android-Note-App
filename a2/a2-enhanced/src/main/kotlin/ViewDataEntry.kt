import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.scene.control.ScrollPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority

class ViewDataEntry(
    private val model: Model
) : ScrollPane(), IView {

    private val addEntryButton = Button("Add Entry ")
    private val title = Label().apply {
        alignment = Pos.CENTER
    }
    private val entries = VBox().apply {
        spacing = 10.0
        padding = Insets(10.0)
    }

    // enhanced : color selector
    val saturationCaption = Label("Saturation:")
    val saturationLevel = Slider(0.0, 1.0, 1.0)
    val saturationValue = Label(
        saturationLevel.value.toString()
    )
    val brightnessCaption = Label("Brightness:")
    val brightnessLevel = Slider(0.0, 1.0, 1.0)
    val brightnessValue = Label(
        brightnessLevel.value.toString()
    )

    val saturationBox = HBox(saturationCaption, saturationLevel, saturationValue).apply {
        spacing = 10.0
        alignment = Pos.CENTER
    }
    val brightnessBox = HBox(brightnessCaption, brightnessLevel, brightnessValue).apply {
        spacing = 10.0
        alignment = Pos.CENTER
    }


    override fun updateView() {
        if (model.changeDataEntry) {
            entries.children.clear()
            // name of the data
            title.text = "Dataset name: "+ model.currentTitle
            entries.children.add(title)

            // each data entry
            model.dataTable.forEachIndexed { i, it ->
                val label = Label("Entry #$i")
                val line = HBox(label.apply {HBox.setHgrow(this, Priority.NEVER)
                    minWidth = 55.0}
                    , it.valueInput.apply {HBox.setHgrow(this, Priority.ALWAYS)}
                    , it.button.apply {HBox.setHgrow(this, Priority.NEVER) }).apply {
                    spacing = 10.0
                    alignment = Pos.CENTER
                    minWidth = 100.0
                }
                entries.children.add(line)
            }
            entries.children.add(addEntryButton)

//            // dataset 2
            val title2 = Label().apply {
                alignment = Pos.CENTER
                text = "Dataset name 2: "+ model.currentTitle2
            }
            entries.children.add(title2)

            // each data entry
            model.dataTable2.forEachIndexed { i, it ->
                val label = Label("Entry #$i")
                val line = HBox(label.apply {HBox.setHgrow(this, Priority.NEVER)
                    minWidth = 55.0}
                    , it.valueInput.apply {HBox.setHgrow(this, Priority.ALWAYS)}
                    , it.button.apply {HBox.setHgrow(this, Priority.NEVER)
                        isDisable = true}).apply {
                    spacing = 10.0
                    alignment = Pos.CENTER
                    minWidth = 100.0
                }
                entries.children.add(line)
            }

            entries.children.add(saturationBox)
            entries.children.add(brightnessBox)
        }
    }

    init {
        addEntryButton.setOnMouseClicked {
            model.addEntry()
        }
        addEntryButton.prefWidthProperty().bind(this.widthProperty())

        this.isFitToWidth = true
        this.hbarPolicy = ScrollBarPolicy.NEVER
        this.vbarPolicy = ScrollBarPolicy.ALWAYS

        // enhanced:
        saturationLevel.valueProperty().addListener { _, _, new_val ->
            model.saturation = new_val.toDouble()
            saturationValue.text = String.format("%.2f", new_val)
            model.changeColor()
        }
        brightnessLevel.valueProperty().addListener { _, _, new_val ->
            model.brightness = new_val.toDouble()

            brightnessValue.text = String.format("%.2f", new_val)
            model.changeColor()
        }
        brightnessBox.prefWidthProperty().bind(this.widthProperty())
        saturationBox.prefWidthProperty().bind(this.widthProperty())

        content = entries
        model.addView(this)


    }
}
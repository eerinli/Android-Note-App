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

    override fun updateView() {
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
    }

    init {
        addEntryButton.setOnMouseClicked {
            model.addEntry()
        }
        addEntryButton.prefWidthProperty().bind(this.widthProperty())

        this.isFitToWidth = true
        this.hbarPolicy = ScrollBarPolicy.NEVER
        this.vbarPolicy = ScrollBarPolicy.ALWAYS
        content = entries
        model.addView(this)
    }
}
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.VPos
import javafx.scene.control.*
import javafx.scene.control.ChoiceBox
import javafx.scene.layout.HBox

class ViewToolbar(
    private val model: Model
) : HBox(), IView {

    // data set selector
    private val dataSetSelector = ChoiceBox<String>()
    private val separator = Separator()
    private val separator2 = Separator()

    // data set creator
    private val createButton = Button("Create")
    private val createNameInput = TextField()

    // visualization selector
    private val lineButton = Button("Line")
    private val barButton = Button("Bar")
    private val barSEMButton = Button("Bar (SEM)")
    private val pieButton = Button("Pie")

    // When notified by the model that things have changed,
    // update to display the new value
    override fun updateView() {
        if (model.addNewDataset) {
            dataSetSelector.items.add(model.currentDataSet.title)
            dataSetSelector.selectionModel.select(model.currentDataSet.title)
        }
        // disable button for bonus
        barSEMButton.isDisable = !model.curNoNegative
        pieButton.isDisable = !model.curNoNegative
    }

    init {
        // format initialization
        dataSetSelector.items.addAll(
            "quadratic", "negative quadratic", "alternating",  "random", "inflation ‘90-‘22")
        dataSetSelector.prefWidth = 150.0
        dataSetSelector.selectionModel.selectFirst()

        separator.orientation = Orientation.VERTICAL
        separator.valignment = VPos.CENTER
        separator.padding = (Insets(0.0, 23.0, 0.0, 23.0))

        createButton.prefWidth = 55.0
        createButton.maxWidth = 55.0

        createNameInput.prefWidth = 150.0
        createNameInput.maxWidth = 150.0
        createNameInput.prefHeight = 8.0
        createNameInput.maxHeight = 8.0
        createNameInput.promptText = "Data set name"

        separator2.orientation = Orientation.VERTICAL
        separator2.valignment = VPos.CENTER
        separator2.padding = (Insets(0.0, 23.0, 0.0, 23.0))

        lineButton.prefWidth = 75.0
        barButton.prefWidth = 75.0
        barSEMButton.prefWidth = 75.0
        pieButton.prefWidth = 75.0

        // event handling
        dataSetSelector.setOnAction {
            if (model.addNewDataset) {
                model.addNewDataset = false
            } else {
                val selected = dataSetSelector.selectionModel.selectedItem.toString()
                model.changeDataSet(selected)
            }
        }

        createButton.setOnMouseClicked {
            model.addDataSet(createNameInput.text)
            createNameInput.clear()
        }

        lineButton.setOnMouseClicked {
            model.changeVisualization(CHARTTYPE.LINE)
        }
        barButton.setOnMouseClicked {
            model.changeVisualization(CHARTTYPE.BAR)
        }
        barSEMButton.setOnMouseClicked {
            model.changeVisualization(CHARTTYPE.BAR_SEM)
        }
        pieButton.setOnMouseClicked {
            model.changeVisualization(CHARTTYPE.PIE)
        }

        this.children.addAll(dataSetSelector,separator, createNameInput, createButton,
            separator2, HBox(lineButton, barButton, barSEMButton, pieButton))
        model.addView(this)
    }
}
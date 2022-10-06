import javafx.collections.FXCollections
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox

internal class ToolbarView(
    private val model: Model
) : VBox(), IView {

    // buttons to select list/grid view
    private val listButton = Button("List").apply {
        prefWidth = 50.0
    }
    private val gridButton = Button("Grid").apply {
        prefWidth = 50.0
    }

    override fun updateView() {
        // gray out button associated with the currently active grid
        if (model.isList) {
            listButton.isDisable = true
            gridButton.isDisable = false
        } else {
            listButton.isDisable = false
            gridButton.isDisable = true
        }
    }

    init {
        // view group to switch between the views
        val viewLabel = Label("View:")
        // register the controller as a handler for the "List" button
        listButton.addEventHandler(MouseEvent.MOUSE_CLICKED) {
            model.changeViewSelection()
        }
        // register the controller as a handler for the "Grid" button
        gridButton.addEventHandler(MouseEvent.MOUSE_CLICKED) {
            model.changeViewSelection()
        }

        val viewGroup = HBox(viewLabel, listButton, gridButton, Separator(Orientation.VERTICAL)).apply {
            alignment = Pos.CENTER_LEFT
            spacing = 10.0
            HBox.setHgrow(this, Priority.NEVER)
        }

        // archived group to show or hide archived notes
        val archivedLabel = Label("Show archived:")
        val checkArchived = CheckBox()
        checkArchived.isSelected = false
        // register the controller as a handler for the "Show Archived" checkbox
        checkArchived.addEventHandler(MouseEvent.MOUSE_CLICKED) {
            model.displayArchived()
        }

        val archivedGroup = HBox(archivedLabel, checkArchived, Separator(Orientation.VERTICAL)).apply {
            alignment = Pos.CENTER_LEFT
            HBox.setHgrow(this, Priority.NEVER)
            spacing = 10.0
        }

        // sort group to specify the sort order
        val sortLabel = Label("Order by:")
        val sortChoice = ChoiceBox(FXCollections.observableArrayList("Length (asc)","Length (desc)"))
        sortChoice.selectionModel.select(0)
        sortChoice.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            if (newValue != null) {
                model.changeOrder(newValue)
            }
        }

        val sortGroup = HBox(sortLabel,sortChoice).apply {
            alignment = Pos.CENTER_LEFT
            HBox.setHgrow(this, Priority.ALWAYS)
            spacing = 10.0
        }

        // button to removes all notes from the system
        val clearButton = Button("Clear") // setOnAction
        // register the controller as a handler for the "Clear" button
        clearButton.addEventHandler(MouseEvent.MOUSE_CLICKED) {
            model.clear()
        }

        // toolbar
        val toolbar = ToolBar(viewGroup, archivedGroup, sortGroup, clearButton)
        toolbar.style = "-fx-padding: 10px;"

        // add toolbar to the pane
        this.children.addAll(toolbar)

        // register with the model when we're ready to start receiving data
        model.addView(this)

    }
}
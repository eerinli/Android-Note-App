import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.TextAlignment

internal class MainView(
    private val model: Model
) : VBox(), IView {

    // Special Note in list view
    private val listInput = TextArea().apply {
    }
    private val listCreateButton = Button("Create").apply {
        alignment = Pos.CENTER
        prefHeight = 42.0
        prefWidth = 75.0
    }
    private val listCreateNote = HBox(listInput, listCreateButton).apply {
        background = Background(
            BackgroundFill(Color.LIGHTSALMON,
                CornerRadii(10.0),
                Insets(5.0))
        )
        spacing = 10.0
        padding = Insets(10.0 * UNIT)
        prefHeight = 62.0
        alignment = Pos.CENTER
        HBox.setHgrow(listCreateButton, Priority.NEVER)
        HBox.setHgrow(listInput, Priority.ALWAYS)
    }

    // Special Note in grid view
    private val gridInput = TextArea().apply {
        prefHeight = 210.0
    }
    private val gridCreateButton = Button("Create").apply {
        prefWidth = 205.0
        textAlignment = TextAlignment.CENTER
    }
    private val gridCreateNote = VBox(gridInput, gridCreateButton).apply {
        background = Background(
            BackgroundFill(Color.LIGHTSALMON,
                CornerRadii(10.0),
                Insets(5.0))
        )
        spacing = 10.0
        padding = Insets(10.0 * UNIT)
        maxHeight = 225.0
        maxWidth = 225.0
        HBox.setHgrow(gridCreateButton, Priority.ALWAYS)
        HBox.setHgrow(gridInput, Priority.NEVER)
    }

    // Special Note in desktop view
    private val desktopInput = TextArea().apply {
        prefHeight = 210.0
    }
    private val desktopCreateButton = Button("Create").apply {
        prefWidth = 205.0
        textAlignment = TextAlignment.CENTER
    }
    private val desktopCreateNote = VBox(desktopInput, desktopCreateButton).apply {
        background = Background(
            BackgroundFill(Color.LIGHTSALMON,
                CornerRadii(10.0),
                Insets(5.0))
        )
        spacing = 10.0
        padding = Insets(10.0 * UNIT)
        maxHeight = 225.0
        maxWidth = 225.0
        HBox.setHgrow(desktopCreateButton, Priority.ALWAYS)
        HBox.setHgrow(desktopInput, Priority.NEVER)
    }

    override fun updateView() {

        // clear both views
        listRoot.children.clear()
        gridRoot.children.clear()
        desktopRoot.children.clear()

        // add special note
        listRoot.children.add(listCreateNote)
        gridRoot.children.add(gridCreateNote)

        // add notes
        for (note in model.notes) {
            if (note.isActive || !model.showArchived) {
                listRoot.children.add(note.listNote)
                gridRoot.children.add(note.gridNote)
                desktopRoot.children.add(note.desktopNote)
            }
        }

        // add special note in desktop
        desktopRoot.children.add(desktopCreateNote)

        // display selected view
        when (model.viewIndex) {
            0 -> {
                listScrollPane.isVisible=true
                listScrollPane.isManaged=true
                gridScrollPane.isVisible=false
                gridScrollPane.isManaged=false
                desktopScrollPane.isVisible=false
                desktopScrollPane.isManaged=false
            }
            1 -> { // Grid view
                listScrollPane.isVisible=false
                listScrollPane.isManaged=false
                gridScrollPane.isVisible=true
                gridScrollPane.isManaged=true
                desktopScrollPane.isVisible=false
                desktopScrollPane.isManaged=false
            }
            2 -> {
                listScrollPane.isVisible=false
                listScrollPane.isManaged=false
                gridScrollPane.isVisible=false
                gridScrollPane.isManaged=false
                desktopScrollPane.isVisible=true
                desktopScrollPane.isManaged=true
            }
        }
    }


    init {
        // Create starting notes
        model.create("This is quite the long note, not quite sure what it" +
                " is about, though. Don't forget to buy milk for")
        model.create("note1")
        model.create("note22", false)
        model.create("note333", false)
        model.create("note4444")

        // register the controller as a handler for the "Create" button in list view
        listCreateButton.addEventHandler(MouseEvent.MOUSE_CLICKED) {
            model.create(listInput.text)
            listInput.clear()
        }

        // register the controller as a handler for the "Create" button in list view
        gridCreateButton.addEventHandler(MouseEvent.MOUSE_CLICKED) {
            model.create(gridInput.text)
            gridInput.clear()
        }

        // register the controller as a handler for the "Create" button in desktop view
        desktopCreateButton.addEventHandler(MouseEvent.MOUSE_CLICKED) {
            model.create(desktopInput.text)
            desktopInput.clear()
        }

        // enable special note in Desktop view to be dragged
        with (desktopCreateNote) {
            makeDraggable()
        }

        // set views to be fit to the pane size
        gridScrollPane.isFitToHeight = true
        gridScrollPane.isFitToWidth = true
        listScrollPane.isFitToWidth = true
        desktopScrollPane.isFitToHeight = true
        desktopScrollPane.isFitToWidth = true

        // add list / grid view to the pane
        children.addAll(gridScrollPane, listScrollPane, desktopScrollPane)

        // register with the model when we're ready to start receiving data
        model.addView(this)
    }
}
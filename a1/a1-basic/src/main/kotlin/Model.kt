import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.TextAlignment

class Model {
    // region View Management

    // all views of this model
    private val views: ArrayList<IView> = ArrayList()

    // all notes in list/grid view
    val notes: ArrayList<Note> = ArrayList()

    // variables for toolbar bar
    var showArchived = true
    var isList = true
    private var ascOrder = true

    // variables for status bar
    var noteTotal = 0
    var activeTotal = 0

    // common variable
    private val ACTIVE_BACKGROUND = Background(
            BackgroundFill(Color.LIGHTYELLOW,
                    CornerRadii(10.0),
                    Insets(5.0))
    )
    private val ARCHIVED_BACKGROUND = Background(
            BackgroundFill(Color.LIGHTGRAY,
                    CornerRadii(10.0),
                    Insets(5.0))
    )


    // method that the views can use to register themselves with the Model
    // once added, they are told to update and get state from the Model
    fun addView(view: IView) {
        views.add(view)
        view.updateView()
    }

    // the model uses this method to notify all Views that the data has changed
    // the expectation is that the Views will refresh themselves to display new data when appropriate
    private fun notifyObservers() {
        if (ascOrder) notes.sortBy{it.text.length}
        else notes.sortByDescending{it.text.length}
        for (view in views) {
            view.updateView()
        }
    }

    // method that to change the order of notes by sortValue
    fun changeOrder(sortValue: Number) {
        ascOrder = (sortValue == 0)
        notifyObservers()
    }

    // method to display list view
    fun changeViewSelection() {
        isList = !isList
        notifyObservers()
    }

    // method to show / hide archived note
    fun displayArchived() {
        showArchived = !showArchived
        notifyObservers()
    }

    // method to removes all notes from the system and updates all views
    fun clear() {
        notes.clear()
        noteTotal = 0
        activeTotal = 0
        notifyObservers()
    }

    // method to create a note with content txt that is archived/active according to active
    fun create(txt: String, active: Boolean = true) {
        // checkbox group for archived in list view
        val listCheckNoteArchived = CheckBox().apply {
            isSelected = false
        }
        val listNoteArchivedLabel = Label("Archived")
        val listNoteArchived = HBox(listCheckNoteArchived, listNoteArchivedLabel).apply {
            alignment = Pos.TOP_CENTER
            spacing = 5.0
        }
        // note content in list view
        val listNoteLabel = Label(txt).apply {
            textFill = Color.BLACK
            textAlignment =  TextAlignment.LEFT
            isWrapText = true
            alignment = Pos.TOP_LEFT
        }
        // create note in list view
        val listNote = BorderPane().apply {
            center = listNoteLabel
            right = listNoteArchived
            background = ACTIVE_BACKGROUND
            padding = Insets(10.0 * UNIT)
        }
        BorderPane.setAlignment(listNoteLabel, Pos.CENTER_LEFT)

        // increment values for status bar
        noteTotal++
        activeTotal++

        // checkbox group for archived in grid view
        val gridCheckNoteArchived = CheckBox().apply {
            isSelected = false
        }
        val gridNoteArchivedLabel = Label("Archived")
        val gridNoteArchived = HBox(gridCheckNoteArchived, gridNoteArchivedLabel).apply {
            alignment = Pos.BOTTOM_LEFT
            spacing = 5.0
        }
        // note content in grid view
        val gridNoteLabel = Label(txt).apply {
            textFill = Color.BLACK
            textAlignment =  TextAlignment.LEFT
            isWrapText = true
            alignment = Pos.TOP_LEFT
            maxHeight = 180.0
        }
        // create note in grid view
        val gridNote = BorderPane().apply {
            top = gridNoteLabel
            bottom = gridNoteArchived
            background = ACTIVE_BACKGROUND
            padding = Insets(10.0 * UNIT)
            prefHeight = 225.0
            prefWidth = 225.0
            maxHeight = 225.0
            maxWidth = 225.0

        }

        // add note to notes
        val note = Note(txt, listNote, gridNote, true)
        notes.add(note)

        // listener for archived checkbox in list view
        listCheckNoteArchived.selectedProperty().addListener {
            _, _, newValue ->
            if (newValue) {
                listNote.background = ARCHIVED_BACKGROUND
                if (note.isActive) {
                    // to reflect the archived result in list view
                    note.changeArchived()
                    activeTotal--
                    gridCheckNoteArchived.isSelected = true
                }

            } else {
                listNote.background = ACTIVE_BACKGROUND
                if (!note.isActive) {
                    // to reflect the active result in list view
                    note.changeArchived()
                    activeTotal++
                    gridCheckNoteArchived.isSelected = false
                }
            }
            notifyObservers()
        }

        // listener for archived checkbox in grid view
        gridCheckNoteArchived.selectedProperty().addListener {
            _, _, newValue ->
            if (newValue) {
                gridNote.background = ARCHIVED_BACKGROUND
                if (note.isActive) {
                    // to reflect the archived result in grid view
                    note.changeArchived()
                    activeTotal--
                    listCheckNoteArchived.isSelected = true
                }

            } else {
                gridNote.background = ACTIVE_BACKGROUND
                if (!note.isActive) {
                    // to reflect the active result in grid view
                    note.changeArchived()
                    activeTotal++
                    listCheckNoteArchived.isSelected = false
                }
            }
            notifyObservers()
        }

        // only for starting note that is archived
        if (!active) {
            listCheckNoteArchived.isSelected = true
        }
        notifyObservers()

    }
}
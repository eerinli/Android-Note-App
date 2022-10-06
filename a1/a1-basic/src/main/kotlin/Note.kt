import javafx.scene.layout.BorderPane

// Class Note for each note in view
class Note(
    _text: String, _listNote: BorderPane, _gridNote: BorderPane, _isActive: Boolean) {
    var text: String = _text
    var listNote : BorderPane = _listNote
    var gridNote : BorderPane = _gridNote
    var isActive : Boolean = _isActive

    // method to change the note to archived/active
    fun changeArchived() {
        isActive = !isActive
    }
}
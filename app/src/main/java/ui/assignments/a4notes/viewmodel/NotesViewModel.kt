package ui.assignments.a4notes.viewmodel

import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import ui.assignments.a4notes.model.Model


class NotesViewModel : ViewModel() {

    /**
     *  The representation of a [Model.ModelNote] in the ViewModel. Only [VMNote]s are exposed to the View.
     */
    data class VMNote(val id: Int, var title: String, var content: String, var important : Boolean = false, var archived : Boolean = false)  {
        constructor(note : Model.ModelNote) : this(note.id, note.title, note.content, note.important, note.archived)
    }

    // model
    private val model = Model()

    // list of all currently visible / displayed notes
    private val notes = MutableLiveData<MutableList<MutableLiveData<VMNote>>>(mutableListOf())

    // the selected note to edit
    var editNote = MutableLiveData(VMNote(0,"default","default"))

    // UI state indicating if archived notes should be displayed
    private val viewArchived = MutableLiveData(false)

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel>
            create(modelClass: Class<T>, extras: CreationExtras): T {
                return NotesViewModel() as T
            }
        }
    }

    init {
        // noteChangeCallback responds to all changes *within* a ModelNote:
        //   onPropertyChanged received notifications from ModelNote if its state has changed, and updates the
        //   corresponding MVNote accordingly. The VMNote is wrapped in MutableLiveData and exposed as LiveData to the
        //   View.
        val noteChangeCallback = object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(modelNote: Observable?, propertyId: Int) {
                notes.value?.find { it.value?.id == propertyId }?.apply {// find MVNote in notes
                    modelNote as Model.ModelNote
                    this.value = VMNote(modelNote.id, modelNote.title, modelNote.content, modelNote.important, modelNote.archived)
                    if (modelNote.archived && viewArchived.value == false) { // if note is archived and archived notes are not showing, remove from notes
                        notes.value = notes.value.apply {
                            this?.removeIf { note -> note.value?.id == value?.id }
                        }
                    } else { // if not, apply changes from ModelNote to MVNote
                        notes.value = notes.value.apply {
                            this?.sortWith { a, b -> model.compareNotes(a?.value!!.id, b?.value!!.id) }
                        }
                    }
                }
            }
        }

        // addOnListChangedCallback responds to all changes of the list of ModelNotes:
        //   onItemRangeInserted is called if a ModelNote is successfully added to the Model
        //   onItemRangeRemoved is called if a ModelNote is successfully removed from the Model
        model.notes.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableArrayList<Model.ModelNote>>() {
            override fun onChanged(sender: ObservableArrayList<Model.ModelNote>?) {  }
            override fun onItemRangeChanged(sender: ObservableArrayList<Model.ModelNote>?, positionStart: Int, itemCount: Int) {  }
            override fun onItemRangeInserted(sender: ObservableArrayList<Model.ModelNote>?, positionStart: Int, itemCount: Int) {
                val addedNote = sender?.get(positionStart) // get new ModelNote
                addedNote?.addOnPropertyChangedCallback(noteChangeCallback) // add listener to ModelNote
                if (addedNote?.archived?.not() == true) { //
                    notes.value = notes.value.apply {
                        this?.add(MutableLiveData(VMNote(addedNote)))
                        this?.sortWith { p0, p1 -> model.compareNotes(p0?.value!!.id, p1?.value!!.id) }
                    }
                }
            }
            override fun onItemRangeMoved(sender: ObservableArrayList<Model.ModelNote>?, fromPosition: Int, toPosition: Int, itemCount: Int) {  }
            override fun onItemRangeRemoved(sender: ObservableArrayList<Model.ModelNote>?, positionStart: Int, itemCount: Int) {
                notes.value = notes.value!!.apply {
                    removeIf { mvnote -> (sender?.find { modelnote -> modelnote.id == mvnote.value!!.id }) == null }
                }
            }
        })

        model.addSomeNotes()
    }


    /**
     * Returns a read-only version of [notes], which stores read-only observables of [VMNote]. Observe to receive notifications about changes to the list.
     * @see notes
     */
    fun getNotes() : LiveData<MutableList<LiveData<VMNote>>> {
        return notes as LiveData<MutableList<LiveData<VMNote>>>
    }

    /**
     * set the value of [viewArchived] from value, which stores read-only observables of [Boolean]. Update read-only version of [notes] accordingly from [notes] in model
     * @param value value of viewArchived to be set
     * @see viewArchived
     */
    fun setViewArchived(value: Boolean) {
        viewArchived.value = value
        // update note
        notes.value?.clear()
        val newNotes: MutableList<MutableLiveData<VMNote>> = (mutableListOf())
        model.notes.forEach { modelNote ->
            if (!modelNote.archived || viewArchived.value == true) {
                val newNote: MutableLiveData<VMNote> = MutableLiveData<VMNote>(
                    VMNote(
                        modelNote.id, modelNote.title,
                        modelNote.content, modelNote.important, modelNote.archived
                    )
                )
                newNotes.add(newNote)
            }
        }
        newNotes.sortWith { a, b -> model.compareNotes(a?.value!!.id, b?.value!!.id) }
        notes.value = newNotes
    }

    /**
     * forward requests to add note in [notes] from the View to the Model.
     *  @param title title of the note
     *  @param content content or body of the note
     *  @param important indicates if the note is important
     * @see notes
     */
    fun addNote(title: String, content: String, important: Boolean = false) {
        model.addNote(title, content, important)
    }

    /**
     * forward requests to remove note with given id in [notes] from the View to the Model.
     *  @param id unique id of the note to be removed
     * @see notes
     */
    fun removeNote(id: Int) {
        model.removeNote(id)
    }

    /**
     * forward requests to update archived field of note with given id in [notes] from the View to the Model.
     *  @param id unique id of the note to be updated
     *  @param archived indicates if the note is archived
     * @see notes
     */
    fun updateNoteArchived(id: Int, archived: Boolean) {
        model.updateNoteArchived(id, archived)
    }

    /**
     * Update a read-only version of [editNote] in the View
     *  @param id unique id of the note to be updated
     *  @param title title of the note
     *  @param content content or body of the note
     *  @param important indicates if the note is important
     *  @param archived indicates if the note is archived
     * @see editNote
     */
    fun updateEditNote(id: Int?, title: String?, content: String?, important: Boolean?, archived: Boolean?) {
        editNote.value = VMNote(id!!, title!!, content!!, important!!, archived!!)
    }

    /**
     * return a read-only version of [editNote] in the View
     * @see editNote
     */
    fun getEditNote(): LiveData<VMNote> {
        return editNote
    }

    /**
     * forward requests to update title field of note with given id in [notes] from the View to the Model.
     *  @param id unique id of the note to be updated
     *  @param title new title of the note
     * @see notes
     */
    fun updateNoteTitle(id: Int, title: String) {
        model.updateNoteTitle(id, title)
    }

    /**
     * forward requests to update content field of note with given id in [notes] from the View to the Model.
     *  @param id unique id of the note to be updated
     *  @param content new content of the note
     * @see notes
     */
    fun updateNoteContent(id: Int, content: String) {
        model.updateNoteContent(id, content)
    }

    /**
     * forward requests to update important field of note with given id in [notes] from the View to the Model.
     *  @param id unique id of the note to be updated
     *  @param important indicates if the note is important
     * @see notes
     */
    fun updateNoteImportant(id: Int, important: Boolean) {
        model.updateNoteImportant(id, important)
    }

    /**
     * forward requests to update timestamp field of note with given id in [notes] from the View to the Model.
     *  @param id unique id of the note to be updated
     * @see notes
     */
    fun updateNoteTimestamp(id: Int) {
        model.updateNoteTimestamp(id)
    }
}
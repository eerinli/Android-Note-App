package ui.assignments.a4notes.ui

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ui.assignments.a4notes.viewmodel.NotesViewModel
import ui.assignments.a4notes.R
import androidx.appcompat.widget.SwitchCompat
import androidx.navigation.fragment.findNavController

class ViewMain : Fragment() {

    private val viewModel: NotesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        // layout is defined in "res/layouts/fragment_main.xml"
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        // add UI handles for navigation here
        val buttonFab = root.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab)
        buttonFab.setOnClickListener {
            // (setup navigation actions in "rs/navigation/navigation.xml")
            findNavController().navigate(R.id.action_main_to_add)
        }

        // add UI handlers that call view model here
        val switchShowArchived = root.findViewById<SwitchCompat>(R.id.switch_show_archived)
        switchShowArchived.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
            viewModel.setViewArchived(b)
        }

        // observe changes in viewModel
        viewModel.getNotes().observe(viewLifecycleOwner) { list ->
            val linearLayout = root.findViewById<LinearLayout>(R.id.note_container)
            linearLayout.removeAllViews()
            list.forEach { note ->
                val noteRoot = inflater.inflate(R.layout.note_item, container, false)
                noteRoot.apply {
                    // display title and content
                    findViewById<TextView>(R.id.item_title).text = note.value?.title
                    findViewById<TextView>(R.id.item_content).text = note.value?.content

                    val noteId = note.value?.id
                    // When activating the “archive”-button, the associated note should be updated at the model
                    findViewById<Button>(R.id.button_archive).setOnClickListener {
                        if (noteId != null) {
                            viewModel.updateNoteArchived(noteId, !note.value?.archived!!)
                        }
                    }

                    // When activating the “delete”-button, the associated note should be deleted from the model
                    findViewById<Button>(R.id.button_delete).setOnClickListener {
                        if (noteId != null) {
                            viewModel.removeNote(noteId)
                        }
                    }
                    if (note.value?.important == true) {
                        // “important” notes should be emphasized
                        this.setBackgroundColor(Color.YELLOW)
                    } else if (note.value?.archived == true) {
                        // “archived” notes should be de-emphasized
                        this.setBackgroundColor(Color.LTGRAY)
                    }
                    linearLayout.addView(this)
                }

                // UI handler to edit screen
                noteRoot.setOnClickListener{
                    viewModel.updateEditNote(note.value?.id,
                        note.value?.title,note.value?.content,
                        note.value?.important,note.value?.archived)
                    // (setup navigation actions in "rs/navigation/navigation.xml")
                    findNavController().navigate(R.id.action_main_to_edit)
                }
            }
        }

        return root
    }
}

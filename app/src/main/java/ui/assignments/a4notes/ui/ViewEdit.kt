package ui.assignments.a4notes.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ui.assignments.a4notes.viewmodel.NotesViewModel
import ui.assignments.a4notes.R

class ViewEdit : Fragment() {

    private val viewModel: NotesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        // layout is defined in "res/layouts/fragment_edit.xml"
        val root = inflater.inflate(R.layout.fragment_edit, container, false)

        // add UI handlers that call your view model here
        val switchImportant = root.findViewById<SwitchCompat>(R.id.switch_important_edit)
        val switchArchived = root.findViewById<SwitchCompat>(R.id.switch_archived_edit)
        val textTitle = root.findViewById<TextView>(R.id.note_title)
        val textContent = root.findViewById<TextView>(R.id.note_content)

        // observe changes in viewModel
        viewModel.getEditNote().observe(viewLifecycleOwner) { note->
            // fill information with selected note in Main Screen
            switchImportant.isChecked = note.important
            switchArchived.isChecked = note.archived
            textTitle.text = note.title
            textContent.text = note.content

            // save changes
            textTitle.addTextChangedListener {
                viewModel.updateNoteTitle(note.id, it.toString())
            }
            textContent.addTextChangedListener {
                viewModel.updateNoteContent(note.id, it.toString())
            }

            switchImportant.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
                if (b) { // cannot be important and archived
                    switchArchived.isChecked = false
                }
                viewModel.updateNoteImportant(note.id, b)
            }

            switchArchived.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
                if (b) { // cannot be important and archived
                    switchImportant.isChecked = false
                }
                viewModel.updateNoteArchived(note.id, b)
            }
            // update last-edit date
            viewModel.updateNoteTimestamp(note.id)
        }

        return root
    }
}
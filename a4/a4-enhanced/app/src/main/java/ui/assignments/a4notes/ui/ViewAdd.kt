package ui.assignments.a4notes.ui

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ui.assignments.a4notes.viewmodel.NotesViewModel
import ui.assignments.a4notes.R

class ViewAdd : Fragment() {

    private val viewModel: NotesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // layout is defined in "res/layouts/activity_add.xml"
        val root = inflater.inflate(R.layout.fragment_add, container, false)

        // add UI handlers that call view model here
        val buttonCreate = root.findViewById<Button>(R.id.button_create)
        buttonCreate.setOnClickListener {
            var noteContent = root.findViewById<EditText>(R.id.note_content_add).text.toString()
            if (noteContent == "") noteContent = "Content"
            var noteTitle = root.findViewById<EditText>(R.id.note_title_add).text.toString()
            if (noteTitle == "") noteTitle = "Title"
            val noteImportant = root.findViewById<SwitchCompat>(R.id.switch_important_add).isChecked
            viewModel.addNote(noteTitle,noteContent,noteImportant)

            // (setup navigation actions in "rs/navigation/navigation.xml")
            findNavController().navigate(R.id.action_add_to_main)
        }
        return root
    }
}
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text

internal class StatusBarView(
    private val model: Model
) : VBox(), IView {

    private var notePlural = "s"
    private var activePlural = "s"
    private var activeVerb = "are"
    private var statusText : String = ""
    override fun updateView() {

        // clear the status bar
        children.clear()

        // make sure status text follows English grammar
        notePlural = if (model.noteTotal <= 1) "" else "s"
        activePlural = if (model.activeTotal <= 1) "" else "s"
        activeVerb = if (model.activeTotal <= 1) "is" else "are"

        // create status text
        statusText = " "+model.noteTotal.toString() + " note"+ notePlural + ", "+
                model.activeTotal.toString()+" of which " + activeVerb + " active"

        val status = Text(statusText)
        val statusBar = HBox(status)

        // add status bar to the pane
        children.addAll(statusBar)
    }

    init {
        // register with the model when we're ready to start receiving data
        model.addView(this)
    }
}
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage

// global variable
const val UNIT = 1.55

// List view and Grid View
// Note: listScrollPane and gridScrollPane are set as global variable
//       to avoid resizing issues
val listRoot = VBox().apply {
    padding = Insets(5.5)
    spacing = 2.0
}
val listScrollPane = ScrollPane(listRoot)

val gridRoot = FlowPane().apply {
    padding = Insets(10.0 * UNIT)
    hgap = 10.0
    vgap = 10.0
}
val gridScrollPane = ScrollPane(gridRoot) .apply {
}


class Notes : Application()  {

    override fun start(stage: Stage) {

        // model to control views
        val model = Model()

        // top: toolbar
        val toolbar = ToolbarView(model)

        // bottom: status bar
        val statusBar = StatusBarView(model)

        // centre: notes
        MainView(model)
        val notes = StackPane(listScrollPane,gridScrollPane)

        // layout setup
        val border = BorderPane()
        border.top = toolbar
        border.bottom = statusBar
        border.center = notes
        val scene = Scene(border, 800.0, 600.0)

        // stage setup
        stage.minWidth = 640.0
        stage.minHeight = 480.0
        stage.isResizable = true
        stage.scene = scene
        stage.title = "CS349 - A1 Notes - j2469li"
        stage.show()
    }
}


import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.SplitPane
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

class Graphs : Application()  {

    override fun start(stage: Stage) {
        // model to control views
        val model = Model()

        // top: toolbar
        val toolbar = ViewToolbar(model)
        // left: dataEntry
        val dataEntry = ViewDataEntry(model)
        // right: visualization
        val visualization = ViewVisualizer(model)

        // layout setup
        val border = BorderPane()
        border.top = toolbar
        border.center = SplitPane(dataEntry, visualization)
        val scene = Scene(border, 800.0, 600.0)

        // stage setup
        stage.minWidth = 640.0
        stage.minHeight = 480.0
        stage.isResizable = true
        stage.scene = scene
        stage.title = "CS349 - A2 Graphs - j2469li"

        stage.show()
    }
}


package ui.assignments.connectfour

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Stage
import ui.assignments.connectfour.ui.ViewGrid
import ui.assignments.connectfour.ui.ViewMessage
import ui.assignments.connectfour.ui.ViewPlayerControl

class ConnectFourApp : Application() {
    override fun start(stage: Stage) {
        val grid = ViewGrid()
        val players = ViewPlayerControl()
        val message = ViewMessage()
        val scene = Scene(Pane(players,grid,message), 880.0, 700.0)
        stage.title = "CS349 - A3 Connect Four - j2469li"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }
}
package ui.assignments.connectfour.ui

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Polygon
import javafx.scene.text.Font
import javafx.scene.text.Text
import ui.assignments.connectfour.model.Model
import ui.assignments.connectfour.model.Player

class ViewMessage : Pane(), InvalidationListener {
    private val messageBox = Polygon(340.0, 450.0,
        540.0, 450.0,
        540.0, 300.0,
        440.0, 200.0,
        340.0, 300.0)
    private var messageText =  Text().apply {
        font = Font.font(20.0)
    }
    private var message = StackPane(messageBox,messageText).apply {
        messageText.toFront()
        this.prefWidth = 880.0
        this.prefHeight = 700.0
    }

    init {
        Model.onGameDraw.addListener(this)
        Model.onGameWin.addListener(this)

        this.prefWidth = 880.0
        this.prefHeight = 700.0

        this.isVisible = false

        children.addAll(message)
        messageText.toFront()
    }


    override fun invalidated(observable: Observable?) {
        // resolution
        if (Model.onGameWin.value.equals(Player.ONE)) {
            messageText.text = "Player #1 won!!!"
            messageBox.fill = Color.LIGHTSALMON
            this.isVisible = true
        } else if (Model.onGameWin.value.equals(Player.TWO)) {
            messageText.text = "Player #2 won!!!"
            messageBox.fill = Color.LIGHTYELLOW
            this.isVisible = true
        } else if (Model.onGameDraw.value.equals(true)) {
            messageText.text = "draw"
            messageBox.fill = Color.LIGHTGRAY
            this.isVisible = true
        }
    }
}
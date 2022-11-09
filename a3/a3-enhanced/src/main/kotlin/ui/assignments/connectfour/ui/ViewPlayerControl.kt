package ui.assignments.connectfour.ui

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import ui.assignments.connectfour.model.Model
import ui.assignments.connectfour.model.Player

class ViewPlayerControl : Pane(), InvalidationListener {
    private val player1Control = Text(150.0, 20.0,"Player #1").apply {
        x = 10.0
        y = 20.0
        font = Font.font(20.0)
        fill = Color.GRAY
    }
    private val player2Control = Text(150.0, 20.0,"Player #2").apply {
        x = 790.0
        y = 20.0
        font = Font.font(20.0)
        fill = Color.GRAY
    }

    init {
        Model.onNextPlayer.addListener(this)
        Model.onGameDraw.addListener(this)
        Model.onGameWin.addListener(this)
        this.children.addAll(player1Control,player2Control)
    }

    override fun invalidated(observable: Observable?) {
        if (Model.onGameWin.value != Player.NONE || Model.onGameDraw.value == true) { // resolution
            player1Control.fill = Color.GRAY
            player2Control.fill = Color.GRAY
        } else if (Model.onNextPlayer.value.equals(Player.ONE)) { // active player 1
            player1Control.fill = Color.BLACK
            player2Control.fill = Color.GRAY
        } else if (Model.onNextPlayer.value.equals(Player.TWO)) { // active player 2
            player2Control.fill = Color.BLACK
            player1Control.fill = Color.GRAY
        } else { // game not started
            player1Control.fill = Color.GRAY
            player2Control.fill = Color.GRAY
        }
    }
}
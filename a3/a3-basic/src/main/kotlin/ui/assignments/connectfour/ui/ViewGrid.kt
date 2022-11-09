package ui.assignments.connectfour.ui

import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.util.Duration
import ui.assignments.connectfour.model.Model
import ui.assignments.connectfour.model.Player

class ViewGrid : Pane(), InvalidationListener {

    // grid
    private var gridNode = ImageView(
        javaClass.getResource("/ui/assignments/connectfour/grid_8x7.png")?.toString()
        ?: throw IllegalArgumentException("Image file not found")).apply {
        fitHeight = image.height * 0.8
        fitWidth = image.width * 0.8
        x = 120.0
        y = 120.0
    }

    // start button: No separate view since its update does not related to model
    private val startText = Text(150.0, 20.0,"Click here to start game!").apply {
        x = 340.0
        y = 75.0
        font = Font.font(20.0)
    }
    private val startButton = Rectangle(200.0, 30.0, 480.0, 70.0).apply {
        fill = Color.LIGHTGREEN
    }

    // used for updating the view
    private val columnPieceCnt = IntArray(8) { 0 }
    private enum class STATE { PRESS, DRAG, RELEASE}
    private var oldPlayer: Player = Model.onNextPlayer.value

    init {
        startButton.addEventFilter(MouseEvent.MOUSE_CLICKED) {
            startButton.isVisible = false
            startText.isVisible = false
            Model.startGame()
        }

        this.children.addAll(gridNode, startButton,startText)
        Model.onNextPlayer.addListener(this)
    }

    override fun invalidated(observable: Observable?) {
        // game loop
        if  (oldPlayer != Model.onNextPlayer.value // onNextPlayer indeed changed
            && !Model.onNextPlayer.value.equals(Player.NONE)) {
            val newPiece : Circle
            // start value of piece
            val pieceStartY = 70.0
            var pieceStartX = 50.0

            if (Model.onNextPlayer.value.equals(Player.ONE)) {
                newPiece = Circle(pieceStartX, pieceStartY, 35.0)
                newPiece.fill = Color.RED
            } else {
                pieceStartX = 830.0
                newPiece = Circle(pieceStartX, pieceStartY, 35.0)
                newPiece.fill = Color.YELLOW
            }

            this.children.add(newPiece)
            gridNode.toFront()

            // to drag and drop piece, modified from boundaries in cs349-repo
            var startX = -1.0
            var startY = -1.0
            var state = STATE.PRESS
            var differenceX = 0.0
            var col = -1

            newPiece.setOnMousePressed { event ->
                if (state == STATE.PRESS) {
                    startX = event.x
                    differenceX = startX - newPiece.centerX
                    startY = event.y
                    state = STATE.DRAG
                }
            }

            newPiece.setOnMouseDragged { event ->
                if (state == STATE.DRAG) { // event is mouse position
                    val dx = event.x - startX
                    val dy = event.y - startY

                    if (newPiece.centerY + dy in 60.0..85.0) {
                        newPiece.centerY += dy
                        startY = event.y
                    }
                    if (newPiece.centerX + dx in 35.0..845.0) {
                        // check in columns: 120.0 200.0| 280| 360| 440| 520| 600| 680| 760|
                        val newCenterX = newPiece.centerX + dx
                        var inGrid = false
                        val offset = 40.0
                        var left = 120.0
                        col = -1
                        while (left < 760) {
                            col++
                            val right = left + offset * 2
                            if (newCenterX in left..right) {
                                newPiece.centerX = left + offset
                                startX =  left + offset + differenceX
                                inGrid = true
                                break
                            }
                            left = right
                        }
                        if (!inGrid) { // not in grid
                            col = -1
                            newPiece.centerX += dx
                            startX = event.x
                        }
                    }
                }
            }


            // function to obtain Timeline to move newPiece linearly
            fun getPieceTimeline(count: Int): Timeline {
                val yPos = if (count == -1) pieceStartY else 720.0 - count * 80.0
                val xPos = if (count == -1) pieceStartX else newPiece.centerX // do not change

                return Timeline(
                    KeyFrame(
                        Duration.ZERO,
                        KeyValue(
                            newPiece.centerYProperty(),
                            newPiece.centerY,
                            Interpolator.EASE_BOTH
                        ),
                        KeyValue(
                            newPiece.centerXProperty(),
                            newPiece.centerX,
                            Interpolator.EASE_BOTH
                        ),
                    ),
                    KeyFrame(
                        Duration.seconds(0.4),
                        KeyValue(
                            newPiece.centerYProperty(),
                            yPos,
                            Interpolator.EASE_BOTH
                        ),
                        KeyValue(
                            newPiece.centerXProperty(),
                            xPos,
                            Interpolator.EASE_BOTH
                        ),
                    )
                )
            }

            newPiece.setOnMouseReleased {
                if (state == STATE.DRAG) {
                    state = STATE.RELEASE
                    val t: Timeline
                    if (col != -1) {
                        Model.dropPiece(col) // onPieceDropped is only changed in dropPiece
                        if (Model.onPieceDropped.value != null) { // dropped successfully
                            columnPieceCnt[col]++
                            t = getPieceTimeline(columnPieceCnt[col])
                        } else { // over a full slot
                            t = getPieceTimeline(-1)
                            state = STATE.PRESS
                        }
                    } else { // not over the grid
                        t = getPieceTimeline(-1)
                        state = STATE.PRESS
                    }
                    t.play()
                }
            } // end of newPiece.setOnMouseReleased
            oldPlayer = Model.onNextPlayer.value
        }
    } // end of invalidated
}
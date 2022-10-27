import javafx.scene.control.Button
import javafx.scene.control.TextField

// a helper class for each data entry
class DataEntry(initialVal:Double, model: Model, index:Int, lastOne:Boolean) {
    private val value = initialVal
    val valueInput = TextField().apply {
        text = value.toString()
    }
    val button = Button("X")

    init {
        button.isDisable = lastOne
        valueInput.textProperty().addListener { _, _, new ->
            model.currentDataSet.data[index] = new.toDouble()
            model.graphRefresh()
        }
        button.setOnMouseClicked {
            model.deleteEntry(index)
        }
    }
}
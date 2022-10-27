import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.stage.Stage

class Model {
    // region View Management

    // from layouts in public repo
    /*
    * Base class for all of our windows
    * Used to set default values for all windows
    */
    open inner class StandardWindow @JvmOverloads internal constructor(x: Float = 0f, y: Float = 0f) :
        Stage() {
        init {
            this.x = x.toDouble()
            this.y = y.toDouble()
            width = 300.0
            height = 50.0
            this.isResizable = true
        }
    }
    private inner class WarningWindow @JvmOverloads constructor(x: Float = 0f, y: Float = 0f) :
        StandardWindow(x, y) {
        init {
            val warning = Label("You cannot add a dataset with the same name")
            val root = Pane()
            root.children.add(warning)
            scene = Scene(root)
            title = "Warning"
            show()
        }
    }

    // all views of this model
    private val views: ArrayList<IView> = ArrayList()

    // datasets with initialization
    private var dataSets: MutableList<DataSet> = mutableListOf(
        createSampleDataSet("quadratic"),
        createSampleDataSet("negative quadratic"),
        createSampleDataSet("alternating"),
        createSampleDataSet("random"),
        createSampleDataSet("inflation ‘90-‘22"),
        createSampleDataSet("")
    )

    // current view data
    var currentDataSet = dataSets[0]
    var currentTitle = currentDataSet.title
    var curNoNegative = true

    var currentDataSet2 = dataSets[5]
    var currentTitle2 = currentDataSet2.title

    // for toolbar update
    var addNewDataset = false

    // for chart canvas update
    var viewType = CHARTTYPE.LINE
    var saturation = 1.0
    var brightness = 1.0

    var changeDataEntry = true

    // data table for data entries
    var dataTable: MutableList<DataEntry> = mutableListOf(
        DataEntry(currentDataSet.data[0], this, 0, false))
    var dataTable2: MutableList<DataEntry> = mutableListOf()

    init {
        // initialize dataTable
        updateDataTable()
    }

    // method that the views can use to register themselves with the Model
    // once added, they are told to update and get state from the Model
    fun addView(view: IView) {
        views.add(view)
        view.updateView()
    }

    // the model uses this method to notify all Views that the data has changed
    // the expectation is that the Views will refresh themselves to display new data when appropriate
    private fun notifyObservers() {
        for (view in views) {
            view.updateView()
        }
    }

    // refresh graph
    fun graphRefresh() {
        // to update button
        curNoNegative = true
        currentDataSet.data.forEachIndexed { _, it ->
            if (it < 0) curNoNegative = false
        }
        currentDataSet2.data.forEachIndexed { _, it ->
            if (it < 0) curNoNegative = false
        }
        notifyObservers()
    }

    // change the visualization UI
    fun changeVisualization(visualization: CHARTTYPE) {
        changeDataEntry = false
        viewType = visualization
        notifyObservers()
    }

    // add new DataSet with one entry
    fun addDataSet(datasetName: String) {
        val index = titleSearch(datasetName)
        if (index != -1) {
            WarningWindow(50.0F, 50.0F)
        } else {
            addNewDataset = true
            viewType = CHARTTYPE.LINE
            val newDataSet = DataSet(datasetName, mutableListOf(0.0))
            dataSets.add(newDataSet)
            currentTitle = newDataSet.title
            // initialize one entry
            currentDataSet = newDataSet
            // update dataTable
            updateDataTable()
            notifyObservers()
        }
    }

    // add a new entry to current dataset
    fun addEntry() {
        changeDataEntry = true
        dataSets[titleSearch(currentTitle)].data.add(0.0)
        updateCurrent(currentTitle)
    }

    // delete a entry in current dataset
    fun deleteEntry(index: Int) {
        changeDataEntry = true
        dataSets[titleSearch(currentTitle)].data.removeAt(index)
        updateCurrent(currentTitle)
    }

    // change the dataset selection
    fun changeDataSet(title: String) {
        changeDataEntry = true
        currentTitle = title
        // show line graph
        viewType = CHARTTYPE.LINE
        updateCurrent(title)
    }

    // change the dataset selection2
    fun changeDataSet2(title: String) {
        changeDataEntry = true
        currentTitle2 = title
        // show line graph
        viewType = CHARTTYPE.LINE
        updateCurrent2(title)
    }

    // change color selection in visualization
    fun changeColor() {
        changeDataEntry = false
        notifyObservers()
    }

    // update current dataset and graph
    private fun updateCurrent(title: String) {
        // update currentDataSet
        currentDataSet = dataSets[titleSearch(title)]
        // update dataTable
        updateDataTable()
        notifyObservers()
    }

    // update current dataset2 and graph
    private fun updateCurrent2(title: String) {
        // update currentDataSet
        currentDataSet2 = dataSets[titleSearch(title)]
        // update dataTable
        updateDataTable2()
        notifyObservers()
    }

    // update dataTable in model
    private fun updateDataTable() {
        dataTable.clear()
        val disable = (currentDataSet.data.size == 1)
        curNoNegative = true
        currentDataSet.data.forEachIndexed { i, it ->
            dataTable.add(DataEntry(it, this, i, disable))
            if (it < 0) curNoNegative = false
        }

        currentDataSet2.data.forEachIndexed { _, it ->
            if (it < 0) curNoNegative = false
        }
    }

    // update dataTable in model
    private fun updateDataTable2() {
        curNoNegative = true
        currentDataSet.data.forEachIndexed { _, it ->
            if (it < 0) curNoNegative = false
        }

        dataTable2.clear()
        val disable2 = (currentDataSet2.data.size == 1)
        currentDataSet2.data.forEachIndexed { i, it ->
            dataTable2.add(DataEntry(it, this, i, disable2))
            if (it < 0) curNoNegative = false
        }
    }

    // Searches for the index of the title in Datasets
    private fun titleSearch(title: String): Int {
        dataSets.forEachIndexed { i, it ->
            if (it.title == title) {
                return i
            }
        }
        return -1
    }
}
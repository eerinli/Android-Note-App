class Model {
    // region View Management

    // all views of this model
    private val views: ArrayList<IView> = ArrayList()

    // datasets with initialization
    private var dataSets: MutableList<DataSet> = mutableListOf(
        createSampleDataSet("quadratic"),
        createSampleDataSet("negative quadratic"),
        createSampleDataSet("alternating"),
        createSampleDataSet("random"),
        createSampleDataSet("inflation ‘90-‘22")
    )

    // current view data
    var currentDataSet: DataSet = dataSets[0]
    var currentTitle: String = currentDataSet.title
    var curNoNegative: Boolean = true

    // for toolbar update
    var addNewDataset: Boolean = false

    // for chart canvas update
    var viewType = CHARTTYPE.LINE

    // data table for data entries
    var dataTable: MutableList<DataEntry> = mutableListOf(
        DataEntry(currentDataSet.data[0], this, 0, false))

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
        notifyObservers()
    }

    // change the visualization UI
    fun changeVisualization(visualization: CHARTTYPE) {
        viewType = visualization
        notifyObservers()
    }

    // add new DataSet
    fun addDataSet(datasetName: String) {
        addNewDataset = true
        viewType = CHARTTYPE.LINE
        val newDataSet = DataSet(datasetName, mutableListOf())
        dataSets.add(newDataSet)
        currentTitle = newDataSet.title
        addEntry() // initialize one entry
    }

    // add a new entry to current dataset
    fun addEntry() {
        dataSets[titleSearch(currentTitle)].data.add(0.0)
        updateCurrent(currentTitle)
    }

    // delete a entry in current dataset
    fun deleteEntry(index: Int) {
        dataSets[titleSearch(currentTitle)].data.removeAt(index)
        updateCurrent(currentTitle)
    }

    // change the dataset selection
    fun changeDataSet(title: String) {
        // show line graph
        currentTitle = title
        viewType = CHARTTYPE.LINE
        updateCurrent(title)
    }

    // update current dataset and graph
    private fun updateCurrent(title: String) {
        // update currentDataSet
        currentDataSet = dataSets[titleSearch(title)]
        // update dataTable
        updateDataTable()
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
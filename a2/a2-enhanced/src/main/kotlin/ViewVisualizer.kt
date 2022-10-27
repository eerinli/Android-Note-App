import javafx.scene.layout.Pane

class ViewVisualizer(
private val model: Model
) : Pane(), IView {
    // four chart types
    private val barChart = BarChart(model)
    private val barSEMChart = BarSEMChart(model)
    private val pieChart = PieChart(model)
    private val lineChart = LineChart(model)

    override fun updateView() {
        when (model.viewType) {
            CHARTTYPE.LINE -> {
                lineChart.draw()
                lineChart.isVisible=true
                lineChart.isManaged=true
                pieChart.isVisible=false
                pieChart.isManaged=false
                barChart.isVisible=false
                barChart.isManaged=false
                barSEMChart.isVisible=false
                barSEMChart.isManaged=false
            }
            CHARTTYPE.BAR -> {
                barChart.draw()
                barChart.isVisible=true
                barChart.isManaged=true
                barSEMChart.isVisible=false
                barSEMChart.isManaged=false
                pieChart.isVisible=false
                pieChart.isManaged=false
                lineChart.isVisible=false
                lineChart.isManaged=false
            }
            CHARTTYPE.BAR_SEM -> {
                barSEMChart.draw()
                barSEMChart.isVisible=true
                barSEMChart.isManaged=true
                pieChart.isVisible=false
                pieChart.isManaged=false
                barChart.isVisible=false
                barChart.isManaged=false
                lineChart.isVisible=false
                lineChart.isManaged=false
            }
            CHARTTYPE.PIE -> {
                pieChart.draw()
                pieChart.isVisible=true
                pieChart.isManaged=true
                barSEMChart.isVisible=false
                barSEMChart.isManaged=false
                barChart.isVisible=false
                barChart.isManaged=false
                lineChart.isVisible=false
                lineChart.isManaged=false
            }
        }
    }

    init {
        barChart.widthProperty().bind(this.widthProperty())
        barChart.heightProperty().bind(this.heightProperty())
        barSEMChart.widthProperty().bind(this.widthProperty())
        barSEMChart.heightProperty().bind(this.heightProperty())
        pieChart.widthProperty().bind(this.widthProperty())
        pieChart.heightProperty().bind(this.heightProperty())
        lineChart.widthProperty().bind(this.widthProperty())
        lineChart.heightProperty().bind(this.heightProperty())

        this.children.addAll(barChart,barSEMChart,lineChart,pieChart)
        model.addView(this)
    }
}
import kotlin.random.Random

// data set class
data class DataSet(
    var title: String,
    var data: MutableList<Double>,
)

// to create sample datasets
fun createSampleDataSet(name: String): DataSet {

    return when (name) {
        "" -> DataSet(
            "",
            mutableListOf()
        )
        "quadratic" -> DataSet(
            "quadratic",
            mutableListOf(0.1, 1.0, 4.0, 9.0, 16.0)
        )
        "negative quadratic" -> DataSet(
            "negative quadratic",
            mutableListOf(-0.1, -1.0, -4.0, -9.0, -16.0)
        )
        "alternating" -> DataSet(
            "alternating",
            mutableListOf(-1.0, 3.0, -1.0, 3.0, -1.0, 3.0)
        )
        "random" -> DataSet(
            "random",
            MutableList(20) { Random.nextDouble(-100.0, 100.0) }
        )
        "inflation ‘90-‘22" -> DataSet(
            "inflation ‘90-‘22",
            mutableListOf(4.8, 5.6, 1.5, 1.9, 0.2, 2.1, 1.6, 1.6, 1.0, 1.7,
                2.7, 2.5, 2.3, 2.8, 1.9, 2.2, 2.0, 2.1, 2.4, 0.3, 1.8, 2.9, 1.5,
                0.9, 1.9, 1.1, 1.4, 1.6, 2.3, 1.9, 0.7, 3.4, 6.8)
        )
        else -> DataSet(
            "ERROR",
            mutableListOf(-1.0, 3.0, -1.0, 3.0, -1.0, 3.0)
        )
    }
}

package com.paisleydavis.transcribe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.aachartmodel.aainfographics.aachartcreator.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BarGraphFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class BarGraphFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewOfLayout: View
//    private var chart: BarChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // create example chart for now
        viewOfLayout = inflater.inflate(com.paisleydavis.transcribe.R.layout.fragment_topic_graph, container, false)

        val aaChartView = viewOfLayout.findViewById<AAChartView>(R.id.frag_chart)

        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .title("Timeframe and Frequency of Common Effects")
            .backgroundColor(R.color.almost_white)
            .dataLabelsEnabled(true)
            .xAxisLabelsEnabled(true)
            .yAxisTitle("Users")
            .categories(arrayOf("Weight Loss", "Increased Appetite", "Extreme Mood Swings"))
                // categories are x-axis labels
            .series(arrayOf(
                AASeriesElement()
                    .name("After 1 Month") // 1 bar per array entry for each item in time period
                    .data(arrayOf(
                        arrayOf("Weight Loss", 4),
                        arrayOf("Increased Appetite", 7),
                        arrayOf("Extreme Mood Swings", 5)
                    )),
                AASeriesElement()
                    .name("After 3 Months")
                    .data(arrayOf(
                        arrayOf("Weight Loss", 2),
                        arrayOf("Increased Appetite", 2),
                        arrayOf("Extreme Mood Swings", 3)
                    )),
                AASeriesElement()
                    .name("After 6 Months")
                    .data(arrayOf(
                        arrayOf("Weight Loss", 1),
                        arrayOf("Increased Appetite", 7),
                        arrayOf("Extreme Mood Swings", 3)
                    ))
            )
            )

        //The chart view object calls the instance object of AAChartModel and draws the final graphic
        aaChartView.aa_drawChartWithChartModel(aaChartModel)

        // Inflate the layout for this fragment
        return viewOfLayout
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BarGraphFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                BarGraphFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
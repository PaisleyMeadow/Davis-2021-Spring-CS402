package com.paisleydavis.transcribe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BarGraphFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BarGraphFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewOfLayout: View
    private var chart: BarChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewOfLayout = inflater.inflate(com.paisleydavis.transcribe.R.layout.fragment_bar_graph, container, false)
        // create a new chart object
        chart = BarChart(activity);
        chart!!.description.isEnabled = false;

////        val mv = MyMarkerView(activity, R.layout.custom_marker_view)
////        mv.setChartView(chart) // For bounds control
//
//        chart!!.marker = mv

        chart!!.setDrawGridBackground(true);
        chart!!.setDrawBarShadow(false);

//        chart!!.setData(generateBarData(1, 20000f, 12));

        val parent: FrameLayout = viewOfLayout.findViewById(com.paisleydavis.transcribe.R.id.chart_parent)
        parent.addView(chart)

//        // Inflate the layout for this fragment
        return viewOfLayout
    }

    protected fun generateBarData(dataSets: Int, range: Float, count: Int): BarData? {
        val sets: ArrayList<IBarDataSet> = ArrayList()
        for (i in 0 until dataSets) {
            val entries: ArrayList<BarEntry> = ArrayList()
            for (j in 0 until count) {
                entries.add(BarEntry(j.toFloat(), (Math.random() * range).toFloat() + range / 4))
            }
//            val ds = BarDataSet(entries, getLabel(i))
//            ds.setColors(*ColorTemplate.VORDIPLOM_COLORS)
//            sets.add(ds)
        }
        val d = BarData(sets)
        return d
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
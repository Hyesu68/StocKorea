package com.susuryo.stockorea.view

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.susuryo.stockorea.R
import com.susuryo.stockorea.databinding.FragmentGraphBinding
import com.susuryo.stockorea.model.StockPrice
import com.susuryo.stockorea.viewmodel.DetailViewModel

class GraphFragment : Fragment() {
    private lateinit var binding: FragmentGraphBinding
    private lateinit var item: List<StockPrice>
    private lateinit var activity: DetailActivity

    private lateinit var viewModel: DetailViewModel

    private var pageNo = 1
    private var numOfRows = 7

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGraphBinding.inflate(inflater, container, false)
        activity = requireActivity() as DetailActivity

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        viewModel.refresh(activity.id, numOfRows, pageNo)

        observeViewModel()

        binding.day7.setOnClickListener { setPeriod(7) }
        binding.day30.setOnClickListener { setPeriod(30) }
        binding.day365.setOnClickListener { setPeriod(365) }

        binding.prev.setOnClickListener {
            pageNo += 1
            viewModel.refresh(activity.id, numOfRows, pageNo)
        }
        binding.next.setOnClickListener {
            if (pageNo > 1) {
                pageNo -= 1
                viewModel.refresh(activity.id, numOfRows, pageNo)
            } else {
                Toast.makeText(requireContext(), "This is the most recent data.", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun setPeriod(num: Int) {
        numOfRows = num
        pageNo = 1
        binding.toggleButton.isEnabled = false
        viewModel.refresh(activity.id, numOfRows, pageNo)
    }

    private fun observeViewModel() {
        viewModel.detailResponse.observe(viewLifecycleOwner) { data ->
            data?.let {
                item = it.response.body.items.item
                if (item.isNotEmpty()) {
                    setChart()
                    setDate()
                } else {
                    Toast.makeText(requireContext(), "This is the First page.", Toast.LENGTH_SHORT).show()
                }
                binding.toggleButton.isEnabled = true

            }
        }

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                binding.progressbar.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
    }

    private fun setDate() {
        val start = item.last().basDt
        val end = item.first().basDt
        binding.date.text = changeDateFormat(start) + " ~ " + changeDateFormat(end)
    }

    private fun changeDateFormat(date: String): String {
        val year = date.substring(0, 4)
        val month = date.substring(4, 6)
        val day = date.substring(6, 8)
        return "$year.$month.$day"
    }

    private fun setChart() {
        val entries = mutableListOf<CandleEntry>()
        val dates = mutableListOf<String>()
        var num = 0f
        val reversedItem = item.reversed()
        for (i in reversedItem) {
            val h = i.hipr.toFloat()
            val l = i.lopr.toFloat()
            val o = i.mkp.toFloat()
            val c = i.clpr.toFloat()
            val tmp = CandleEntry(num, h, l, o, c)
            num += 1f
            entries.add(tmp)
            dates.add(i.basDt) // Add the date to the dates list
        }

        val candleDataSet = CandleDataSet(entries, "Stock Data")
        candleDataSet.color = Color.rgb(80, 80, 80)
        candleDataSet.shadowColor = Color.DKGRAY
        candleDataSet.shadowWidth = 0.7f
        candleDataSet.decreasingColor = Color.BLUE
        candleDataSet.decreasingPaintStyle = Paint.Style.FILL
        candleDataSet.increasingColor = Color.RED
        candleDataSet.increasingPaintStyle = Paint.Style.FILL
        candleDataSet.neutralColor = Color.RED

        val data = CandleData(candleDataSet)
        binding.chart.data = data
        binding.chart.description.isEnabled = false
        binding.chart.legend.isEnabled = false
        binding.chart.isDoubleTapToZoomEnabled = true
        binding.chart.setDrawGridBackground(false)
//        binding.chart.isHighlightPerTapEnabled = false
//        binding.chart.defaultFocusHighlightEnabled = false
//        binding.chart.isHighlightPerDragEnabled = false

        binding.chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e == null) {
                    return
                }

                if (e is CandleEntry) {
                    val high = e.high
                    val low = e.low
                    val label = binding.chart.xAxis.valueFormatter.getFormattedValue(e.x)

                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Do you want to calculate using the price on the date you selected?")
                        .setMessage("Highest Price: $high \n Lowest Price: $low")
                        .setNegativeButton("매수") { dialog, which ->
                            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)
                            val bundle = bundleOf("key" to label.toInt(), "isSell" to false)
                            setFragmentResult("date", bundle)
                            viewPager.currentItem = 1
                        }
                        .setPositiveButton("매도") { dialog, which ->
                            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)
                            val bundle = bundleOf("key" to label.toInt(), "isSell" to true)
                            setFragmentResult("date", bundle)
                            viewPager.currentItem = 1
                        }

                    val dialog = builder.create()
                    dialog.show()
                }
            }

            override fun onNothingSelected() {
                // Do something when nothing is selected
            }
        })

// Enable touch gestures on the chart
        binding.chart.setTouchEnabled(true)
        binding.chart.isDragEnabled = true
        binding.chart.setScaleEnabled(true)
        binding.chart.setPinchZoom(true)

        // Set the date labels
        val xAxis = binding.chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(dates)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chart.axisLeft.isEnabled = false

        binding.chart.invalidate()
    }


}
package br.edu.ifsp.dmo.financeapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import br.edu.ifsp.dmo.financeapp.databinding.ActivityChartBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class ChartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setPickupDate()
        setTroggleButtonClickListener()
        setBarChart()
        setPieChart()
        setLineChart()
    }

    private fun setPickupDate() {
        binding.pickupDateButton.setOnClickListener {
            val constraintDate = CalendarConstraints.Builder()
                .setEnd(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Selecione um intervalo de datas")
                .setSelection(
                    Pair(MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds())
                )
                .setTheme(R.style.CustomDatePicker)
                .setCalendarConstraints(constraintDate)
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .build()

            dateRangePicker.addOnPositiveButtonClickListener { selection ->
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date1 = Date(selection.first)
                val date2 = Date(selection.second)
                val formattedDate = simpleDateFormat.format(date1) + " " + simpleDateFormat.format(date2)
                Toast.makeText(this, "Data escolhida: $formattedDate", Toast.LENGTH_LONG).show()
            }

            dateRangePicker.addOnNegativeButtonClickListener {
                dateRangePicker.dismiss()
            }

            dateRangePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
        }
    }

    private fun setTroggleButtonClickListener() {
        binding.toggleButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.bar_chart_button -> {
                        binding.barChart.visibility = View.VISIBLE
                        binding.pieChart.visibility = View.GONE
                        binding.lineChart.visibility = View.GONE
                    }
                    R.id.pie_chart_button -> {
                        binding.barChart.visibility = View.GONE
                        binding.pieChart.visibility = View.VISIBLE
                        binding.lineChart.visibility = View.GONE
                    }
                    R.id.line_chart_button -> {
                        binding.barChart.visibility = View.GONE
                        binding.pieChart.visibility = View.GONE
                        binding.lineChart.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setBarChart() {
        val barChart = binding.barChart

        val entries = listOf(
            BarEntry(0f, 5f),
            BarEntry(1f, 10f),
            BarEntry(2f, 15f),
            BarEntry(3f, 8f)
        )

        val dataSet = BarDataSet(entries, "Vendas").apply {
            setColors(*ColorTemplate.MATERIAL_COLORS)
            valueTextColor = Color.WHITE
            valueTextSize = 14f
        }

        val data = BarData(dataSet)
        barChart.data = data

        barChart.setBackgroundColor(Color.TRANSPARENT)
        barChart.xAxis.textColor = Color.WHITE
        barChart.axisLeft.textColor = Color.WHITE
        barChart.axisRight.textColor = Color.WHITE

        barChart.xAxis.setDrawGridLines(false)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.setDrawGridLines(false)

        barChart.invalidate()
    }

    private fun setPieChart() {
        val pieChart = binding.pieChart

        val entries = listOf(
            PieEntry(0f, 5f),
            PieEntry(1f, 10f),
            PieEntry(2f, 15f),
            PieEntry(3f, 8f)
        )

        val dataSet = PieDataSet(entries, "Vendas").apply {
            setColors(*ColorTemplate.MATERIAL_COLORS)
            valueTextColor = Color.WHITE
            valueTextSize = 14f
        }

        val data = PieData(dataSet)
        pieChart.data = data

        pieChart.setBackgroundColor(Color.TRANSPARENT)
        pieChart.invalidate()
    }

    private fun setLineChart() {
        val lineChart = binding.lineChart

        val entries = listOf(
            Entry(0f, 5f),
            Entry(1f, 10f),
            Entry(2f, 15f),
            Entry(3f, 8f)
        )

        val dataSet = LineDataSet(entries, "Vendas").apply {
            setColors(*ColorTemplate.MATERIAL_COLORS)
            valueTextColor = Color.WHITE
            valueTextSize = 14f
        }

        val data = LineData(dataSet)
        lineChart.data = data

        lineChart.setBackgroundColor(Color.TRANSPARENT)
        lineChart.xAxis.textColor = Color.WHITE
        lineChart.axisLeft.textColor = Color.WHITE
        lineChart.axisRight.textColor = Color.WHITE

        lineChart.xAxis.setDrawGridLines(false)
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisRight.setDrawGridLines(false)

        lineChart.invalidate()
    }
}
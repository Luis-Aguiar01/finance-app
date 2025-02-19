package br.edu.ifsp.dmo.financeapp.ui.activities.chart

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.dmo.financeapp.R
import br.edu.ifsp.dmo.financeapp.databinding.ActivityChartBinding
import br.edu.ifsp.dmo.financeapp.util.Constants
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class ChartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChartBinding
    private lateinit var viewModel: ChartViewModel
    private val customColors = listOf(
        Color.rgb(255, 99, 132),
        Color.rgb(54, 162, 235),
        Color.rgb(255, 206, 86),
        Color.rgb(153, 102, 255),
        Color.rgb(255, 159, 64),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ChartViewModel::class.java)

        openBundle()
        configObservers()
        configListeners()
    }

    // Quando a base de dados muda, os gráficos atualizam com os novos dados
    private fun configObservers() {
        viewModel.totalByCategory.observe(this, Observer {
            setPieChart(it)
            setBarChart(it)
        })
    }

    private fun configListeners() {
        binding.arrowBack.setOnClickListener {
            finish()
        }

        // Configura um botão que abre um calendário
        binding.pickupDateButton.setOnClickListener {
            val constraintDate = CalendarConstraints.Builder()
                .setEnd(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            // Cria um calendário do tipo range picker para o usuário escolher
            // um range de datas.
            val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(getString(R.string.select_range_date))
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .setTheme(R.style.CustomDatePicker)
                .setCalendarConstraints(constraintDate)
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .build()

            dateRangePicker.addOnPositiveButtonClickListener { selection ->
                val oneDayMillis = 24 * 60 * 60 * 1000 // 1 dia em milissegundos
                val timeZone = TimeZone.getDefault()

                val localCalendar = Calendar.getInstance(timeZone)

                // Inicializa o  primeiro dia em 00:00 para pegar o dia inteiro desde o começo
                // Foi preciso somar um dia, pois estava com um dia de atraso, em relação ao
                // escolhido no calendário
                localCalendar.timeInMillis = selection.first + oneDayMillis
                localCalendar.set(Calendar.HOUR_OF_DAY, 0)
                localCalendar.set(Calendar.MINUTE, 0)
                localCalendar.set(Calendar.SECOND, 0)
                localCalendar.set(Calendar.MILLISECOND, 0)
                val startDateMillis = localCalendar.timeInMillis

                // Inicializa a segunda data em 23:59:99 para pegar o range completo dos dias.
                localCalendar.timeInMillis = selection.second + oneDayMillis
                localCalendar.set(Calendar.HOUR_OF_DAY, 23)
                localCalendar.set(Calendar.MINUTE, 59)
                localCalendar.set(Calendar.SECOND, 59)
                localCalendar.set(Calendar.MILLISECOND, 999)
                val endDateMillis = localCalendar.timeInMillis

                // Formata as datas no padrão brasileiro
                val simpleDateFormat =
                    SimpleDateFormat(getString(R.string.date_pattern), Locale.getDefault())
                val formattedStartDate = simpleDateFormat.format(Date(startDateMillis))
                val formattedEndDate = simpleDateFormat.format(Date(endDateMillis))

                binding.currentRangeDate.text = "$formattedStartDate - $formattedEndDate"
                binding.currentRangeDate.visibility = View.VISIBLE

                viewModel.getTotalByDate(startDateMillis, endDateMillis)
            }

            dateRangePicker.addOnNegativeButtonClickListener {
                dateRangePicker.dismiss()
            }

            dateRangePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
        }

        // Muda as visibilidades dos gráficos com base no clique dos botões
        binding.toggleButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.bar_chart_button -> {
                        binding.barChart.visibility = View.VISIBLE
                        binding.pieChart.visibility = View.GONE
                    }

                    R.id.pie_chart_button -> {
                        binding.barChart.visibility = View.GONE
                        binding.pieChart.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    // Cria e exibe um gráfico de barras, com um conjuto "categoria - soma(valor)"
    private fun setBarChart(map: Map<String, Double>) {
        val barChart = binding.barChart

        val entries = map.values.mapIndexed { index, value ->
            BarEntry(index.toFloat(), value.toFloat())
        }

        val dataSet = BarDataSet(entries, getString(R.string.buys_by_category)).apply {
            colors = customColors
            valueTextColor = Color.WHITE
            valueTextSize = 9f
            setDrawValues(true)
            valueFormatter = object : ValueFormatter() {
                override fun getBarLabel(barEntry: BarEntry?): String {
                    val index = barEntry?.x?.toInt() ?: 0
                    return map.keys.elementAtOrNull(index) ?: ""
                }
            }
        }

        val data = BarData(dataSet)
        barChart.data = data

        barChart.setBackgroundColor(Color.TRANSPARENT)
        barChart.axisLeft.textColor = Color.WHITE
        barChart.axisRight.textColor = Color.WHITE
        barChart.description.isEnabled = false

        barChart.legend.apply {
            isEnabled = true
            textColor = Color.WHITE
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            orientation = Legend.LegendOrientation.HORIZONTAL
        }

        barChart.xAxis.setDrawGridLines(false)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.setDrawGridLines(false)
        barChart.xAxis.textColor = Color.WHITE
        barChart.xAxis.setDrawLabels(false)
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisRight.axisMinimum = 0f

        barChart.setDrawValueAboveBar(true)
        barChart.setExtraOffsets(0f, 0f, 0f, 10f)

        barChart.invalidate()
    }

    // Cria e exibe um gráfico de pizza, com um conjuto "categoria - soma(valor)"
    private fun setPieChart(map: Map<String, Double>) {
        val pieChart = binding.pieChart

        val entries = map.map { (category, sum) ->
            PieEntry(sum.toFloat(), category)
        }

        val dataSet = PieDataSet(entries, getString(R.string.buys_by_category)).apply {
            colors = customColors
            valueTextColor = Color.WHITE
            valueTextSize = 14f
        }

        val data = PieData(dataSet)
        pieChart.data = data

        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 50f

        pieChart.legend.isEnabled = false
        pieChart.setBackgroundColor(Color.TRANSPARENT)
        pieChart.description.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)

        pieChart.invalidate()
    }

    private fun openBundle() {
        val extras = intent.extras
        if (extras != null) {
            val email = extras.getString(Constants.USER_EMAIL)
            if (email != null) {
                viewModel.setEmail(email)
            }
        }
    }
}
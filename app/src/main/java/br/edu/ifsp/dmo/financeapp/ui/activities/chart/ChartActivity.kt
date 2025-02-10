package br.edu.ifsp.dmo.financeapp.ui.activities.chart

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class ChartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChartBinding
    private lateinit var viewModel: ChartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ChartViewModel::class.java)

        openBundle()
        configObservers()
        configListeners()
    }

    private fun configObservers(){
        viewModel.totalByCategory.observe(this, Observer {
            setPieChart(it)
            setBarChart(it)
        })
    }

    private fun configListeners() {
        binding.arrowBack.setOnClickListener {
            finish()
        }

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
                val startDate = Date(selection.first)
                val endDate = Date(selection.second)
                val formattedDate = simpleDateFormat.format(startDate) + " " + simpleDateFormat.format(endDate)
                Toast.makeText(this, "Data escolhida: $formattedDate", Toast.LENGTH_LONG).show()
            }

            dateRangePicker.addOnNegativeButtonClickListener {
                dateRangePicker.dismiss()
            }

            dateRangePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
        }

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

    private fun setBarChart(map: Map<String, Double>) {
        val barChart = binding.barChart

        val entries = map.values.mapIndexed { index, value ->
            BarEntry(index.toFloat(), value.toFloat())
        }

        val customColors = listOf(
            Color.rgb(255, 99, 132),  // Vermelho suave
            Color.rgb(54, 162, 235),   // Azul claro
            Color.rgb(75, 192, 192),   // Turquesa
            Color.rgb(255, 206, 86),   // Amarelo dourado
            Color.rgb(153, 102, 255),  // Roxo suave
            Color.rgb(255, 159, 64),   // Laranja suave
            Color.rgb(83, 102, 255),   // Azul escuro
            Color.rgb(255, 99, 255),   // Rosa
        )

        val dataSet = BarDataSet(entries, "Compras").apply {
            colors = customColors
            valueTextColor = Color.WHITE
            valueTextSize = 14f
            setDrawValues(true)
        }

        val data = BarData(dataSet)
        barChart.data = data

        // Adiciona as categorias no eixo X
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(map.keys.toList())
        barChart.setBackgroundColor(Color.TRANSPARENT)
        barChart.xAxis.textColor = Color.WHITE
        barChart.axisLeft.textColor = Color.WHITE
        barChart.axisRight.textColor = Color.WHITE

        barChart.legend.apply {
            textColor = Color.WHITE
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            orientation = Legend.LegendOrientation.HORIZONTAL
            yOffset = 20f
            xEntrySpace = 10f
            yEntrySpace = 5f
            textSize = 12f
            maxSizePercent = 0.9f
        }

        barChart.xAxis.setDrawGridLines(false)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.setDrawGridLines(false)

        barChart.description.apply {
            text = "Gráfico de Vendas por Categoria"
            textColor = Color.WHITE
            textSize = 14f
            setPosition(0f, 0f)
        }

        barChart.setExtraOffsets(0f, 0f, 0f, 30f)

        barChart.invalidate()
    }

    private fun setPieChart(map: Map<String, Double>) {
        val pieChart = binding.pieChart

        val entries = map.map { (category, sum) ->
            PieEntry(sum.toFloat(), category)
        }

        val customColors = listOf(
            Color.rgb(255, 99, 132),  // Vermelho suave
            Color.rgb(54, 162, 235),   // Azul claro
            Color.rgb(75, 192, 192),   // Turquesa
            Color.rgb(255, 206, 86),   // Amarelo dourado
            Color.rgb(153, 102, 255),  // Roxo suave
            Color.rgb(255, 159, 64),   // Laranja suave
            Color.rgb(83, 102, 255),   // Azul escuro
            Color.rgb(255, 99, 255),   // Rosa
            Color.rgb(99, 255, 132),   // Verde claro
            Color.rgb(255, 99, 71),    // Vermelho coral
            Color.rgb(199, 199, 199),  // Cinza claro
            Color.rgb(255, 182, 193)   // Rosa claro
        )

        val dataSet = PieDataSet(entries, "Gastos por Categoria").apply {
            colors = customColors
            valueTextColor = Color.WHITE
            valueTextSize = 14f
        }

        val data = PieData(dataSet)
        pieChart.data = data

        pieChart.setHoleColor(Color.TRANSPARENT) // 🔴 Deixa o centro do gráfico transparente
        pieChart.isDrawHoleEnabled = true // Ativa o buraco no centro
        pieChart.holeRadius = 50f

        pieChart.legend.isEnabled = false
        pieChart.setBackgroundColor(Color.TRANSPARENT)
        pieChart.description.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)

        pieChart.invalidate()
    }

    private fun openBundle(){
        val extras = intent.extras
        if(extras != null) {
            val email = extras.getString(Constants.USER_EMAIL)
            if(email != null) {
                viewModel.setEmail(email)
            }
        }
    }
}
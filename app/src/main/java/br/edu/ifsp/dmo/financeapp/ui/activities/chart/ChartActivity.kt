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
            setLineChart(it)
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

    private fun setBarChart(map: Map<String, Double>) {
        val barChart = binding.barChart

        // Converter o Map<String, Double> para uma lista de BarEntry (X = índice, Y = valor)
        val entries = map.values.mapIndexed { index, value ->
            BarEntry(index.toFloat(), value.toFloat())
        }

        val dataSet = BarDataSet(entries, "Vendas").apply {
            setColors(*ColorTemplate.MATERIAL_COLORS) // Define cores para as barras
            valueTextColor = Color.WHITE // Cor do texto dos valores
            valueTextSize = 14f // Tamanho do texto dos valores
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

    private fun setPieChart(map: Map<String, Double>) {
        val pieChart = binding.pieChart

        // Converter o Map<String, Double> em uma lista de PieEntry
        val entries = map.map { (category, sum) ->
            PieEntry(sum.toFloat(), category) // Converte Double para Float e usa a categoria como rótulo
        }

        val customColors = listOf(
            Color.rgb(255, 0, 0), // Vermelho
            Color.rgb(54, 162, 235),  // Azul
            Color.rgb(75, 192, 192),  // Verde
            Color.rgb(153, 102, 255), // Roxo
            Color.rgb(255, 159, 64),  // Laranja
            Color.rgb(83, 102, 255),  // Azul escuro
            Color.rgb(255, 99, 255)   // Rosa
        )

        // Criar o PieDataSet
        val dataSet = PieDataSet(entries, "Gastos por Categoria").apply {
            colors = customColors // Usar a lista de cores personalizadas
            valueTextColor = Color.WHITE
            valueTextSize = 14f
        }

        val data = PieData(dataSet)
        pieChart.data = data

        // Configurar a legenda
        val legend = pieChart.legend
        legend.textColor = Color.WHITE
        legend.textSize = 10f // Tamanho reduzido do texto
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)

        // Configurações adicionais do PieChart
        pieChart.setBackgroundColor(Color.TRANSPARENT)
        pieChart.description.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)

        // Atualizar o gráfico
        pieChart.invalidate()
    }

    private fun setLineChart(map: Map<String, Double>) {
        val lineChart = binding.lineChart

        val entries = map.values.mapIndexed { index, value ->
            Entry(index.toFloat(), value.toFloat())
        }

        val dataSet = LineDataSet(entries, "Vendas").apply {
            setColors(*ColorTemplate.MATERIAL_COLORS) // Define cores para as linhas
            valueTextColor = Color.WHITE // Cor do texto dos valores
            valueTextSize = 14f // Tamanho do texto dos valores
            setCircleColor(Color.WHITE) // Cor dos pontos
            setDrawCircleHole(false) // Remover buracos nos pontos
            setDrawValues(true) // Exibir valores nos pontos
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
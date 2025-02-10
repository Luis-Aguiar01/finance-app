package br.edu.ifsp.dmo.financeapp.ui.activities.historical

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.dmo.financeapp.R
import br.edu.ifsp.dmo.financeapp.databinding.ActivityHistoricalBinding
import br.edu.ifsp.dmo.financeapp.ui.adapter.bill.BillAdapter
import br.edu.ifsp.dmo.financeapp.ui.adapter.historical.HistoricalAdapter
import br.edu.ifsp.dmo.financeapp.util.Constants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class HistoricalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricalBinding
    private lateinit var viewModel : HistoricalViewModel
    private var adapter = HistoricalAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(HistoricalViewModel::class.java)


        openBundle()
        configObservers()
        configRecyclerView()
        configListeners()
    }


    private fun configRecyclerView(){
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun configObservers() {
        viewModel.bills.observe(this, Observer {
            adapter.submitDatabase(it)
        })
    }

    private fun openBundle(){
        val extras = intent.extras
        if(extras != null) {
            val email = extras.getString(Constants.USER_EMAIL)
            if(email != null) {
                Toast.makeText(this, "Email: ${email}", Toast.LENGTH_SHORT).show()
                viewModel.setEmail(email)
            }
        }
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
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds())
                )
                .setTheme(R.style.CustomDatePicker)
                .setCalendarConstraints(constraintDate)
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .build()

            dateRangePicker.addOnPositiveButtonClickListener { selection ->
                // Obtém as datas selecionadas em milissegundos
                val startDateMillis = selection.first
                val endDateMillis = selection.second



                // Exibe as datas selecionadas em um Toast

                Toast.makeText(this, "Start Date: ${startDateMillis}", Toast.LENGTH_LONG).show()
                Toast.makeText(this, "Final Date: ${endDateMillis}", Toast.LENGTH_LONG).show()
                // Passa as datas em milissegundos para o ViewModel
                viewModel.getBillByDate(startDateMillis, endDateMillis + 86399000)
            }


            dateRangePicker.addOnNegativeButtonClickListener {
                dateRangePicker.dismiss()
            }

            dateRangePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
        }
    }
}
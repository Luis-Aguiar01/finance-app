package br.edu.ifsp.dmo.financeapp.ui.activities.historical

import android.os.Bundle
import android.view.View
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
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

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
                val oneDayMillis = 24 * 60 * 60 * 1000 // 1 dia em milissegundos
                val timeZone = TimeZone.getDefault()

                val localCalendar = Calendar.getInstance(timeZone)

                localCalendar.timeInMillis = selection.first + oneDayMillis
                localCalendar.set(Calendar.HOUR_OF_DAY, 0)
                localCalendar.set(Calendar.MINUTE, 0)
                localCalendar.set(Calendar.SECOND, 0)
                localCalendar.set(Calendar.MILLISECOND, 0)
                val startDateMillis = localCalendar.timeInMillis

                localCalendar.timeInMillis = selection.second + oneDayMillis
                localCalendar.set(Calendar.HOUR_OF_DAY, 23)
                localCalendar.set(Calendar.MINUTE, 59)
                localCalendar.set(Calendar.SECOND, 59)
                localCalendar.set(Calendar.MILLISECOND, 999)
                val endDateMillis = localCalendar.timeInMillis

                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedStartDate = simpleDateFormat.format(Date(startDateMillis))
                val formattedEndDate = simpleDateFormat.format(Date(endDateMillis))

                binding.currentRangeDate.text = "$formattedStartDate - $formattedEndDate"
                binding.currentRangeDate.visibility = View.VISIBLE

                viewModel.getBillByDate(startDateMillis, endDateMillis)
            }

            dateRangePicker.addOnNegativeButtonClickListener {
                dateRangePicker.dismiss()
            }

            dateRangePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
        }
    }
}
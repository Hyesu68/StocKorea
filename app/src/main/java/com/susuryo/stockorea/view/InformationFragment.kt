package com.susuryo.stockorea.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.susuryo.stockorea.R
import com.susuryo.stockorea.databinding.FragmentInformationBinding
import com.susuryo.stockorea.model.StockPrice
import com.susuryo.stockorea.viewmodel.DetailViewModel
import java.text.SimpleDateFormat
import java.util.*

class InformationFragment: Fragment() {
    private lateinit var binding: FragmentInformationBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var item: List<StockPrice>
    private lateinit var activity: DetailActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInformationBinding.inflate(inflater, container, false)
        activity = requireActivity() as DetailActivity

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
//        viewModel.refresh(activity.id, 1, 1)
        observeViewModel()

        binding.buyDateButton.setOnClickListener { selectDate(false) }
        binding.sellDateButton.setOnClickListener { selectDate(true) }
        binding.resultButton.setOnClickListener { setResult() }

        binding.buy.editText?.addTextChangedListener(setTextWatcher(binding.buy))
        binding.buyNumber.editText?.addTextChangedListener(setTextWatcher(binding.buyNumber))
        binding.sell.editText?.addTextChangedListener(setTextWatcher(binding.sell))
        binding.sellNumber.editText?.addTextChangedListener(setTextWatcher(binding.sellNumber))

        binding.buy.isEnabled = false
        binding.buyNumber.isEnabled = false
        binding.sell.isEnabled = false
        binding.sellNumber.isEnabled = false

        setFragmentResultListener("date") { key, bundle ->
            bundle.getBoolean("isSell").let {
                this.isSell = it
            }
            bundle.getInt("key")?.let {
                val year = it.toString().substring(0, 4)
                val month = it.toString().substring(4, 6)
                val day = it.toString().substring(6, 8)
                setDate(isSell, "$year.$month.$day")
                viewModel.getFromDate(activity.id, it)
            }
        }

        return binding.root
    }

    private fun setResult() {
        val buyPrice = binding.buy.editText?.text.toString()
        val buyNum = binding.buyNumber.editText?.text.toString()
        val sellPrice = binding.sell.editText?.text.toString()
        val sellNum = binding.sellNumber.editText?.text.toString()

        if (buyPrice.isEmpty() || buyNum.isEmpty() || sellPrice.isEmpty() || sellNum.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all the information.", Toast.LENGTH_SHORT).show()
        } else {
            val result = ((sellPrice.toInt()*sellNum.toInt() - buyPrice.toInt()*buyNum.toInt()))
            binding.result.text = "Profit and loss: ${result}"
        }
    }

    private fun setTextWatcher(textInputLayout: TextInputLayout): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textInputLayout.error = null
            }

            override fun afterTextChanged(p0: Editable?) {
                val txt = textInputLayout.editText?.text.toString()
                if (txt.isEmpty()) return
                when (textInputLayout.id) {
                    R.id.buy -> {
                        if (txt.toInt() < buyLow!! || txt.toInt() > buyHigh!!) {
                            textInputLayout.error = "Please set the buying price within the low and high price range."
                        }
                    }
                    R.id.sell -> {
                        if (txt.toInt() < sellLow!! || txt.toInt() > sellHigh!!) {
                            textInputLayout.error = "Please set the selling price within the low and high price range."
                        }
                    }
                    R.id.buyNumber -> {
                        if (binding.sellNumber.editText?.text?.isEmpty() == true) return
                        val sellnum = binding.sellNumber.editText?.text.toString().toInt()
                        if (sellnum > textInputLayout.editText?.text.toString().toInt()) {
                            textInputLayout.error = "Please set the buying quantity to be equal to or higher than the selling quantity."
                        }
                    }
                    R.id.sellNumber -> {
                        if (binding.buyNumber.editText?.text?.isEmpty() == true) return
                        val buynum = binding.buyNumber.editText?.text.toString().toInt()
                        if (buynum < textInputLayout.editText?.text.toString().toInt()) {
                            textInputLayout.error = "Please set the selling quantity to be equal to or lower than the buying quantity."
                        }
                    }
                }
            }
        }
    }

    var isSell = false
    private fun selectDate(isSell: Boolean) {
        this.isSell = isSell
        val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointBackward.before(System.currentTimeMillis()))
                        .build()
                )
                .build()
        datePicker.addOnPositiveButtonClickListener { dateInMillis ->
            val dateFormatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            val formattedDate = dateFormatter.format(Date(dateInMillis))
            setDate(isSell, formattedDate)

            val dateFormatter2 = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val formattedDate2 = dateFormatter2.format(Date(dateInMillis))
            viewModel.getFromDate(activity.id, formattedDate2.toInt())
        }
        datePicker.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun setDate(isSell: Boolean, formattedDate: String?) {
        if (isSell) {
            binding.sellDate.text = formattedDate
            binding.sell.isEnabled = true
            binding.sellNumber.isEnabled = true
        } else {
            binding.buyDate.text = formattedDate
            binding.buy.isEnabled = true
            binding.buyNumber.isEnabled = true
        }
    }

    private var buyLow: Int? = null
    private var buyHigh: Int? = null
    private var sellLow: Int? = null
    private var sellHigh: Int? = null
    private fun observeViewModel() {
        viewModel.detailResponse.observe(viewLifecycleOwner) { countries ->
            countries?.let {
                item = it.response.body.items.item
                if (item.isNotEmpty()) {
                    val price = "Lowest Price: ${item[0].lopr}, Highest Price: ${item[0].hipr}"
                    if (isSell) {
                        binding.sellPrice.text = price
                        sellLow = item[0].lopr.toInt()
                        sellHigh = item[0].hipr.toInt()
                        binding.sell.editText?.setText(item[0].hipr)
                        binding.sellDate.text = changeDateFormat(item[0].basDt)
                    } else {
                        binding.buyPrice.text = price
                        buyLow = item[0].lopr.toInt()
                        buyHigh = item[0].hipr.toInt()
                        binding.buy.editText?.setText(item[0].lopr)
                        binding.buyDate.text = changeDateFormat(item[0].basDt)
                    }
                } else {
                    Toast.makeText(requireContext(), "This is the First page.", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun changeDateFormat(date: String): String {
        val year = date.substring(0, 4)
        val month = date.substring(4, 6)
        val day = date.substring(6, 8)
        return "$year.$month.$day"
    }
}
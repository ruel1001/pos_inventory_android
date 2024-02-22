package com.android.boilerplate.utils.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import com.android.boilerplate.R
import com.android.boilerplate.databinding.DialogCalendarBinding
import com.android.boilerplate.utils.setOnSingleClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CalendarDialog: BottomSheetDialogFragment(){

    private var viewBinding: DialogCalendarBinding? = null
    private lateinit var callback: ((selectedDate: String) -> Unit)
    private var date: String = ""
    var format: String = ""

    private lateinit var calendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.dialog_calendar,
            container,
            false
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = DialogCalendarBinding.bind(view)
        setUpCalendar()
        setClickListener()
    }


    private fun setUpCalendar() = viewBinding?.run {
        calendar = Calendar.getInstance()

        val dayFormat = SimpleDateFormat(format, Locale.getDefault())

        try {
            calendar.time = dayFormat.parse(date) ?: Date()
        } catch (e: ParseException) {
            Log.e(TAG, e.message, e)
        }

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val year = calendar.get(Calendar.YEAR)

        val month = calendar.get(Calendar.MONTH)
        calendar.set(year, month, day)
        birthdayDatePicker.init(year, month, day) { _, year1, monthOfYear, dayOfMonth ->
            calendar.set(year1, monthOfYear, dayOfMonth)
        }

      //  val minDate = Calendar.getInstance()
       // minDate.add(Calendar.YEAR, 0)

       // birthdayDatePicker.maxDate = minDate.time.time
    }

    private fun saveDate() = viewBinding?.run {
        birthdayDatePicker.clearFocus()
        callback.invoke(SimpleDateFormat(format, Locale.getDefault())
            .format(calendar.time))
        dismiss()
    }


    private fun setClickListener() = viewBinding?.run{
        positiveButton.setOnSingleClickListener {
            saveDate()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }



    companion object {
        fun newInstance(date: String, callback: ((selectedDate: String) -> Unit)) = CalendarDialog().apply {
            this.callback = callback
            this.date = date
        }

        val TAG: String = CalendarDialog::class.java.simpleName
    }
}
package com.example.nguyenanhlinh_viewpager.fragment

import android.annotation.SuppressLint
import android.content.Context

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nguyenanhlinh_viewpager.adapter.CalendarAdapter
import com.example.nguyenanhlinh_viewpager.R
import com.example.nguyenanhlinh_viewpager.model.DayOfMonth
import kotlinx.android.synthetic.main.fragment_day_of_month.view.*
import java.time.LocalDate
import java.time.YearMonth
import kotlin.collections.ArrayList

class DayOfMonthFragment : Fragment() {
    lateinit var selectedDate: LocalDate
    var startDay = ""
    var custom_line: Int = R.drawable.custom_line
    private val TAG = "CalendarAdapter"
    var isPageCenter = false
    private lateinit var calendarAdapters: CalendarAdapter
    private var listDayOfMonth: ArrayList<DayOfMonth> = ArrayList()
    private var weekTitle = mutableListOf("MON", "TUE", "WED", "THUR", "FRI", "SAT", "SUN")
    private val week = mutableListOf("MON", "TUE", "WED", "THUR", "FRI", "SAT", "SUN")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedDate = it.getSerializable("month") as LocalDate
            startDay = it.getString("startDay") as String
            isPageCenter = it.getBoolean("isCenter")
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_day_of_month, container, false)
        calendarAdapters = CalendarAdapter(listDayOfMonth, context)
        initDataList(selectedDate, startDay)
        view.calendarRecyclerView.layoutManager =
            GridLayoutManager(activity, 7)
        view.calendarRecyclerView.setHasFixedSize(true)
        view.calendarRecyclerView.adapter = calendarAdapters
        var itemDecoration =
            DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.custom_line)!!)
        view.calendarRecyclerView.addItemDecoration(itemDecoration)
        if (selectedDate.month.value - 1 == 12){
            val share = requireActivity().getSharedPreferences("month_year", Context.MODE_PRIVATE)
            val editor = share.edit()
            editor.putInt("month", selectedDate.month.value )
            editor.putInt("year", selectedDate.year -1)
            editor.commit()
        }else{
            val share = requireActivity().getSharedPreferences("month_year", Context.MODE_PRIVATE)
            val editor = share.edit()
            editor.putInt("month", selectedDate.month.value -1)
            editor.putInt("year", selectedDate.year)
            editor.commit()
        }

//        val share = requireActivity().getSharedPreferences("date", Context.MODE_PRIVATE)
//        calendarAdapters.setItemClick {
////            if (calendarAdapters.index == -1)
////                calendarAdapters.index = it
////            listDayOfMonth[calendarAdapters.index].check = false
////            listDayOfMonth[it].check = true
////            calendarAdapters.notifyItemChanged(calendarAdapters.index)
////            calendarAdapters.index = it
////            calendarAdapters.color = Color.GREEN
////            calendarAdapters.notifyItemChanged(it)
////            val editor = share?.edit()
////            editor?.putInt("index", it)
////            editor?.putInt("dayOfWeek", listDayOfMonth[it].day.toInt())
////            editor?.putBoolean("isCurrentMonth", listDayOfMonth[it].checkDayOfMonth)
////            editor?.putInt("month", selectedDate.month.value)
////            editor?.putInt("year", selectedDate.year)
////            editor?.commit()
//
//        }
        return view
    }


    @SuppressLint("NotifyDataSetChanged")

    private fun addTitleWeek(startDay: String) {
        weekTitle =  when (startDay) {
            "TUE" -> mutableListOf("TUE", "WED", "THUR", "FRI", "SAT", "SUN", "MON")
            "WED" -> mutableListOf("WED", "THUR", "FRI", "SAT", "SUN", "MON", "TUE")
            "THUR" -> mutableListOf("THUR", "FRI", "SAT", "SUN", "MON", "TUE", "WED")
            "FRI" -> mutableListOf("FRI", "SAT", "SUN", "MON", "TUE", "WED", "THUR")
            "SAT" -> mutableListOf("SAT", "SUN", "MON", "TUE", "WED", "THUR", "FRI")
            "MON" -> mutableListOf("MON", "TUE", "WED", "FRI", "FRI", "SAT", "SUN")
            else -> mutableListOf("SUN", "MON", "TUE", "WED", "FRI", "FRI", "SAT")

        }
        for (i in 0 until weekTitle.size)
            listDayOfMonth.add(DayOfMonth(weekTitle[i], check = false, checkDayOfMonth = true))


    }

    fun initDataList(date: LocalDate, startDay: String) {
        listDayOfMonth.clear()
        addTitleWeek(startDay)
        val times = date.withDayOfMonth(1)
//        val month = selectedDate.monthValue
//        val year = selectedDate.year
        val yearMonth = YearMonth.from(date)
        val isOfMonth = yearMonth.lengthOfMonth()
        val index = weekTitle.indexOf(week[times.dayOfWeek.value - 1]) + 1
        val dayOfPreviousMonth = date.withDayOfMonth(1).plusDays((-index).toLong()).dayOfMonth
//        val share = requireActivity().getSharedPreferences("date", Context.MODE_PRIVATE)
        var currentTime = LocalDate.now()
        for (i in 1 until index) {
            listDayOfMonth.add(
                DayOfMonth(
                    (dayOfPreviousMonth + i).toString(),
                    check = false, checkDayOfMonth = false
                )
            )
        }

        for (i in 1..isOfMonth) {
//            if (i == currentTime.dayOfMonth && date.month.value == currentTime.month.value && date.year == currentTime.year) {
//                listDayOfMonth.add(DayOfMonth(i.toString(), check = true, checkDayOfMonth = true))
//            }else{
                listDayOfMonth.add(DayOfMonth(i.toString(), check = false, checkDayOfMonth = true))
//            }


//            if (i == share.getInt(
//                    "dayOfWeek",
//                    0
//                ) && month == share.getInt(
//                    "month",
//                    0
//                ) && year == share.getInt(
//                    "year", 0) && isPageCenter
//            ) {
//                listDayOfMonth.add(DayOfMonth(i.toString(), check = true, checkDayOfMonth = true))
//                Log.d(
//                    TAG,
//                    "bindData: ${
//                        share.getInt(
//                            "month",
//                            0
//                        )
//                    } ${selectedDate.monthValue} day: ${share.getInt("dayOfWeek", 0)}"
//                )
//            }
//        else


        }


        var nextDay = 0
        while (listDayOfMonth.size % 7 != 0) {
            nextDay++
            listDayOfMonth.add(
                DayOfMonth(
                    nextDay.toString(),
                    check = false,
                    checkDayOfMonth = false
                )
            )
        }

        calendarAdapters.month = selectedDate.month.value
        calendarAdapters.year = selectedDate.year
        Log.d(TAG,"${calendarAdapters.month}")

        calendarAdapters.notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateViewPager(newMonth: LocalDate, isCenter: Boolean) {
        isPageCenter = isCenter
        initDataList(newMonth, startDay)
        view?.calendarRecyclerView?.adapter = calendarAdapters
        calendarAdapters.notifyDataSetChanged()

    }

    companion object {
        @JvmStatic
        fun newInstance(month: LocalDate, startDays: String, isCenter : Boolean) =
            DayOfMonthFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("month", month)
                    putString("startDay", startDays)
                    putBoolean("isCenter", isCenter)
                }
            }
    }

}



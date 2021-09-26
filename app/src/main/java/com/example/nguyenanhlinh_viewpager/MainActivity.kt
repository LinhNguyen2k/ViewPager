package com.example.nguyenanhlinh_viewpager


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext

import androidx.viewpager.widget.ViewPager
import com.example.nguyenanhlinh_viewpager.adapter.CalendarAdapter

import com.example.nguyenanhlinh_viewpager.adapter.ViewPagerAdapter
import com.example.nguyenanhlinh_viewpager.fragment.DayOfMonthFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_item.*

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val PAGE_CENTER = 1

class MainActivity : AppCompatActivity() {
    private lateinit var listFragment: ArrayList<DayOfMonthFragment>
    lateinit var pageAdapter: ViewPagerAdapter
    var index = 1
    var startDay = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        listFragment = ArrayList()
        var localDate = LocalDate.now()
        monthYearTV.text = monthYearFromDate(localDate)
        pageAdapter = ViewPagerAdapter(supportFragmentManager, listFragment)
        viewPager_layout.adapter = pageAdapter
        viewPager_layout.setCurrentItem(1, false)
        setting.setOnClickListener {
            val arrItems = arrayOf(
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
                "Sunday"
            )

            fun setAdapter() {
                var dateTime = pageAdapter.fragList[1].selectedDate
                pageAdapter.setCalendar(dateTime, startDay)
            }
            MaterialAlertDialogBuilder(this)
                .setItems(arrItems) { _, which ->
                    startDay = when (which) {
                        0 -> "MON"
                        1 -> "TUE"
                        2 -> "WED"
                        3 -> "THUR"
                        4 -> "FRI"
                        5 -> "SAT"
                        else -> "SUN"
                    }
                    setAdapter()

                }.show()
        }


        listFragment.apply {
            add(DayOfMonthFragment.newInstance(localDate.minusMonths(1),"", false))
            add(DayOfMonthFragment.newInstance(localDate, "", true))
            add(DayOfMonthFragment.newInstance(localDate.plusMonths(1), "", false))
        }


        viewPager_layout.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
            }

            override fun onPageSelected(position: Int) {
                index = position
                var dateTime = pageAdapter.fragList[index].selectedDate
                monthYearTV.text = monthYearFromDate(dateTime)
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    val start = pageAdapter.fragList[1].startDay
                    var dateTime = pageAdapter.fragList[1].selectedDate
                    if (index < PAGE_CENTER) {
                        dateTime = dateTime.minusMonths(1)
                    } else if (index > PAGE_CENTER) {
                        dateTime = dateTime.plusMonths(1)
                    }

                    monthYearTV.text = monthYearFromDate(dateTime)
                    pageAdapter.setCalendar(dateTime, start)
                    viewPager_layout.setCurrentItem(1, false)


                }
            }

        })

    }


    private fun monthYearFromDate(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date!!.format(formatter)
    }

}

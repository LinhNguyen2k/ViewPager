package com.example.nguyenanhlinh_viewpager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.example.nguyenanhlinh_viewpager.fragment.DayOfMonthFragment
import java.time.LocalDate

class ViewPagerAdapter(fragmentManager: FragmentManager, var fragList: ArrayList<DayOfMonthFragment>) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return 3
    }


    override fun getItem(position: Int): Fragment {
        return fragList[position]
    }


    fun setCalendar(currentMonth: LocalDate, startDay : String) {
        var leftMonth = currentMonth.minusMonths(1)

        var rightMonth = currentMonth.plusMonths(1)
        fragList[0].selectedDate = leftMonth
        fragList[1].selectedDate = currentMonth
        fragList[2].selectedDate = rightMonth
        fragList[0].startDay = startDay
        fragList[1].startDay = startDay
        fragList[2].startDay = startDay
        fragList[0].updateViewPager(leftMonth, false)
        fragList[1].updateViewPager(currentMonth, true)
        fragList[2].updateViewPager(rightMonth, false)

        notifyDataSetChanged()



    }
}
package com.example.nguyenanhlinh_viewpager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.nguyenanhlinh_viewpager.MainActivity_Notes
import com.example.nguyenanhlinh_viewpager.OnDoubleClick
import com.example.nguyenanhlinh_viewpager.R
import com.example.nguyenanhlinh_viewpager.SQLite_Notes
import com.example.nguyenanhlinh_viewpager.model.DayOfMonth
import java.util.*

class CalendarAdapter(
    private val daysOfMonth: ArrayList<DayOfMonth>,
    var context: Context?,
) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    var color: Int = Color.CYAN
    var month = 0
    var year = 0
    var check = 0
    var dateChecked = ""
    lateinit var onItemClick: (index: Int) -> Unit
    var index = -1
    private val TAG = "CalendarAdapter"
    fun setItemClick(callback: (index: Int) -> Unit) {
        onItemClick = callback
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_dayOfMonth: TextView = view.findViewById(R.id.tv_cellDayText)
        var bg_layout: ConstraintLayout = view.findViewById(R.id.constraint_layout)

        init {
            itemView.setOnClickListener {
                onItemClick.invoke(adapterPosition)
                index = adapterPosition
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.custom_item, parent, false)
//        val layoutParams = view.layoutParams
//        layoutParams.height = (parent.height * 0.166666666).toInt()
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        var days = daysOfMonth[position]
        holder.tv_dayOfMonth.text = days.day

        if (position <= 6) {
            holder.tv_dayOfMonth.textSize = 15F
            holder.tv_dayOfMonth.setTextColor(ContextCompat.getColor(context!!, R.color.gray))
        }
        if (days.check) {
            index = position
            holder.bg_layout.setBackgroundColor(color)


        } else {
            holder.bg_layout.setBackgroundColor(0)
            val share = context?.getSharedPreferences("date", Context.MODE_PRIVATE)

            val sqliteNotes = SQLite_Notes(context)
            dateChecked = "${daysOfMonth[holder.adapterPosition].day}/${
                month}/${year}"
            if (!sqliteNotes.checkLocalDate(dateChecked) && daysOfMonth[holder.adapterPosition].checkDayOfMonth == share?.getBoolean(
                    "checkDayOfMonth",
                    false
                )
            ) {
                holder.bg_layout.setBackgroundColor(Color.GREEN)

            }

//            if (month == share?.getInt("month", 0)
//                && year == share.getInt("year", 0)
//                && position > 6
//                && daysOfMonth[holder.adapterPosition].day.toInt() == share.getInt("dayOfWeek", 0)
//                && daysOfMonth[holder.adapterPosition].checkDayOfMonth == share.getBoolean(
//                    "checkDayOfMonth",
//                    false
//                )
//
//            ) {
//                if (check == 1) {
//                    index = position
//                    holder.bg_layout.setBackgroundColor(Color.GREEN)
//                } else if (check == 2) {
//                    index = position
//                    holder.bg_layout.setBackgroundColor(Color.RED)
//                }
//            }

        }
        if (!days.checkDayOfMonth) {
            holder.tv_dayOfMonth.setTextColor(ContextCompat.getColor(context!!, R.color.gray))
        } else {
            holder.tv_dayOfMonth.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        }
//        holder.itemView.setOnTouchListener { _, motionEvent ->
//            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
//                check++
//
//                Handler(Looper.getMainLooper()).postDelayed({
//                    check = 0
//                }, 300)
//            }
//            false
//
//        }

        val sharedPreferences1 = context!!.getSharedPreferences("date", Context.MODE_PRIVATE)
        val sharedPreferences2 = context!!.getSharedPreferences("month_year", Context.MODE_PRIVATE)
        holder.itemView.setOnClickListener(object : OnDoubleClick() {

            override fun onDoubleClick() {
//                check = 2
                if (index == -1) index = position
                daysOfMonth[index].check = false
                daysOfMonth[position].check = true
                notifyItemChanged(index)
                index = position
                color = Color.CYAN
                notifyItemChanged(position)
                val editor1 = sharedPreferences1.edit()
                editor1?.putInt("index", position)
                editor1?.putInt("dayOfWeek", daysOfMonth[position].day.toInt())
                editor1?.putBoolean("checkDayOfMonth", daysOfMonth[position].checkDayOfMonth)
                editor1?.putInt("month", sharedPreferences2!!.getInt("month", 0))
                editor1?.putInt("year", sharedPreferences2!!.getInt("year", 0))
                editor1?.commit()
                val intent = Intent(context, MainActivity_Notes::class.java)
                context!!.startActivity(intent)
            }

            override fun onSingleClick() {
//                check = 1
                if (index == -1) index = position
                daysOfMonth[index].check = false
                daysOfMonth[position].check = true
                notifyItemChanged(index)
                index = position
                color = Color.CYAN
                notifyItemChanged(position)
                val editor1 = sharedPreferences1.edit()
                editor1?.putInt("index", position)
                editor1?.putInt("dayOfWeek", daysOfMonth[position].day.toInt())
                editor1?.putBoolean("checkDayOfMonth", daysOfMonth[position].checkDayOfMonth)
                editor1?.putInt("month", sharedPreferences2!!.getInt("month", 0))
                editor1?.putInt("year", sharedPreferences2!!.getInt("year", 0))
                editor1?.commit()

            }

        })


    }
}

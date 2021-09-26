package com.example.nguyenanhlinh_viewpager.adapter

import android.annotation.SuppressLint
import android.content.Context
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
import com.example.nguyenanhlinh_viewpager.OnDoubleClick
import com.example.nguyenanhlinh_viewpager.R
import com.example.nguyenanhlinh_viewpager.model.DayOfMonth
import java.util.*

class CalendarAdapter(
    private val daysOfMonth: ArrayList<DayOfMonth>,
    var context: Context?,
) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    //    var index = -1
    var color: Int = Color.GREEN
    var month = 0
    var year = 0
    var check = 0
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

        var days = daysOfMonth[holder.adapterPosition]
        holder.tv_dayOfMonth.text = days.day

        if (holder.adapterPosition <= 6) {
            holder.tv_dayOfMonth.textSize = 15F
            holder.tv_dayOfMonth.setTextColor(ContextCompat.getColor(context!!, R.color.gray))
        }
        if (days.check) {

            if (check == 1) {
                index = holder.adapterPosition
                holder.bg_layout.setBackgroundColor(color)
            } else if (check > 1) {
                index = holder.adapterPosition
                holder.bg_layout.setBackgroundColor(Color.RED)
            }else{
                index = holder.adapterPosition
                holder.bg_layout.setBackgroundColor(color)
                daysOfMonth[index].check = false
            }

        } else {
            holder.bg_layout.setBackgroundColor(0)
            val share = context?.getSharedPreferences("date", Context.MODE_PRIVATE)

            if (month == share?.getInt("month", 0)
                && year == share.getInt("year", 0)
                && holder.adapterPosition > 6
                && daysOfMonth[holder.adapterPosition].day.toInt() == share.getInt("dayOfWeek", 0)
                && daysOfMonth[holder.adapterPosition].checkDayOfMonth == share.getBoolean(
                    "isCurrentMonth",
                    false
                )

            ) {
                Log.d(
                    TAG,
                    "bindData: ${holder.adapterPosition} - ${share.getInt("index", 0)} ${
                        share.getInt(
                            "month",
                            0
                        )
                    } $month"
                )
                index = holder.adapterPosition
                holder.bg_layout.setBackgroundColor(color)
            }

        }
        if (!days.checkDayOfMonth) {
            holder.tv_dayOfMonth.setTextColor(ContextCompat.getColor(context!!, R.color.gray))
        } else {
            holder.tv_dayOfMonth.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        }
        holder.itemView.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                check++

                Handler(Looper.getMainLooper()).postDelayed({
                    check = 0
                }, 300)
            }
            false

        }

//        sharedPreferences1 = context.getSharedPreferences("date", Context.MODE_PRIVATE)
//        sharedPreferences2 = context.getSharedPreferences("month_year", Context.MODE_PRIVATE)
//        holder.itemView.setOnClickListener(object : OnDoubleClick() {
//
//            override fun onDoubleClick() {
//                if (index == -1) index = position
//                daysOfMonth[index].check = false
//                daysOfMonth[position].check = true
//                notifyItemChanged(index)
//                index = position
//                color = Color.RED
//                notifyItemChanged(position)
//
//            }

//            override fun onSingleClick() {
//                if (index == -1) index = position
//                daysOfMonth[index].check = false
//                daysOfMonth[position].check = true
//                notifyItemChanged(index)
//                index = position
//                color = Color.GREEN
//                notifyItemChanged(position)
////                val editor1 = sharedPreferences1.edit()
////                editor1?.putInt("index", position)
////                editor1?.putInt("dayOfWeek", daysOfMonth[position].day.toInt())
//////                editor1?.putBoolean("checkDayOfMonth", daysOfMonth[position].checkDayOfMonth)
////                editor1?.putInt("month2", sharedPreferences2!!.getInt("month1", 0))
////                editor1?.putInt("year2", sharedPreferences2!!.getInt("year1", 0))
////                editor1?.apply()


//
//            }

//        })


    }
}

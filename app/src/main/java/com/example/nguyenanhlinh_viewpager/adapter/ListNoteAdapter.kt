package com.example.nguyenanhlinh_viewpager.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.example.nguyenanhlinh_viewpager.model.Notes
import kotlinx.android.synthetic.main.activity_main_list_notes.*
import androidx.lifecycle.ViewModel
import com.example.nguyenanhlinh_viewpager.*


class ListNoteAdapter(private var list_notes : ArrayList<Notes>, val context: Context) :
    RecyclerView.Adapter<ListNoteAdapter.ViewHolder>() {
    lateinit var listNoteAdapter: ListNoteAdapter
    private val viewBinderHelper = ViewBinderHelper()
//    lateinit var onClickItem: (positions: Int) -> Unit
//
//    fun setOnclickItem(even: (position: Int) -> Unit) {
//        onClickItem = even
//    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tv_content_note: TextView = view.findViewById(R.id.tv_content_notes)
        var tv_date: TextView = view.findViewById(R.id.tv_date)
        var layout_custom: RelativeLayout = view.findViewById(R.id.layout_custom)
        var swipeRevealLayout: SwipeRevealLayout = view.findViewById(R.id.swipeRevealLayout)
        var layoutDelete: LinearLayout = view.findViewById(R.id.layoutDelete)

//        init {
//            layout_custom.setOnClickListener {
//                onClickItem.invoke(adapterPosition)
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.custom_item_list_note,parent,false)
        return ViewHolder(view)
    }
    fun updateData(notes: ArrayList<Notes>) {
        list_notes.clear()
        list_notes.addAll(notes)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item_list_note = list_notes[position]

        holder.tv_content_note.text = item_list_note.notes
        holder.tv_date.text = item_list_note.localdate
         var sqliteNotes = SQLite_Notes(context)
        viewBinderHelper.bind(holder.swipeRevealLayout, position.toString())
        viewBinderHelper.setOpenOnlyOne(true)
        viewBinderHelper.closeLayout(holder.adapterPosition.toString())
        holder.layoutDelete.setOnClickListener {
            sqliteNotes.deleteItemNotes(item_list_note.localdate)
            list_notes.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            notifyDataSetChanged()
        }
        holder.layout_custom.setOnClickListener {
//            val shared = context.getSharedPreferences("push_detail",Context.MODE_PRIVATE)
//            val edit = shared.edit()
//            edit.putString("content",list_notes[holder.adapterPosition].notes)
//            edit.putInt("check",2)
//            edit.commit()

            val intent = Intent(context,MainActivity_Detail::class.java)
            intent.putExtra("content", holder.tv_content_note.text.toString())
            intent.putExtra("localdate", holder.tv_date.text.toString())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        if (list_notes == null) return 0
        return list_notes.size
    }

    fun filterList(filter: ArrayList<Notes>) {
        list_notes = filter
        notifyDataSetChanged()
    }

}
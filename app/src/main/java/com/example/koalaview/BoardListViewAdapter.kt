package com.example.koalaview

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import org.w3c.dom.Text
import java.util.*
class BoardListViewAdapter(val boardList:MutableList<BoardModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(position: Int): Any {
        return boardList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
            view = LayoutInflater.from(parent?.context).inflate(R.layout.board_list_item, parent, false)

        val title = view?.findViewById<TextView>(R.id.titleArea)
        title!!.text = boardList[position].title

        val time = view?.findViewById<TextView>(R.id.timeArea)
        time!!.text = boardList[position].period

        val time2=view?.findViewById<TextView>(R.id.timeArea2)
        time2!!.text=boardList[position].perriod


        val ddabong=view?.findViewById<TextView>(R.id.tvddabong)
        ddabong!!.text=boardList[position].agreeCount.toString()

        val dDay=view?.findViewById<TextView>(R.id.tvdDay)

        val today= Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time
        val d = (today-boardList[position].date.toLong())/(24 * 60 * 60 * 1000)
        dDay!!.text="D- "+(30-d).toString()

        if(boardList[position].agreeCount>=30||d>30){
            val lin=view?.findViewById<LinearLayout>(R.id.linear)
            lin!!.setBackgroundResource(R.drawable.border3)
            dDay!!.text="D+ "+(d-30).toString()
        }
        return view!!
    }

}
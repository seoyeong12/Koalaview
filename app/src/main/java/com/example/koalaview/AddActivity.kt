package com.example.koalaview

import android.app.ProgressDialog.show
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.koalaview.MyApplication.Companion.getTime
import com.example.koalaview.MyApplication.Companion.getUid

import com.example.koalaview.databinding.ActivityAddBinding
import com.example.koalaview.databinding.CustomDialog2Binding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar2))


        binding.buttonSave.setOnClickListener {
            val database= Firebase.database
            val boardRef=database.getReference("board")
            val title=binding.addtitleView.text.toString()
            var agrees=mutableListOf<String>()
            agrees.add("κΉμμ")

            val name=binding.addNameView.text.toString()
            val content=binding.addContView.text.toString()
            val uid=getUid()
            val time= getTime()
            val date= Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time.time
            val period=makeperiod()
            val perriod=makeperriod()

            boardRef
                .push()
                .setValue(BoardModel(title,name,uid,content,time,0,agrees,date.toString(),period,perriod))

            Toast.makeText(this,"κ²μκΈ μλ ₯ μλ£",Toast.LENGTH_LONG).show()
            finish()
        }
        val dia=CustomDialog2Binding.inflate(layoutInflater)

        binding.process.setOnClickListener {
            AlertDialog.Builder(this).run{
                setView(dia.root)
                dia.textTitle.text="μ²­μ μ μ°¨μλ΄"
                dia.textMessage.setText(R.string.process)
                show()
            }
        }
        val dia2=CustomDialog2Binding.inflate(layoutInflater)
        binding.danger.setOnClickListener {
            AlertDialog.Builder(this).run{
                setView(dia2.root)
                dia2.textTitle.text="μμ± μ μ μμ¬ν­ μλ΄"
                dia2.textMessage.setText(R.string.danger)
                show()
            }
        }
        val dia3=CustomDialog2Binding.inflate(layoutInflater)
        binding.example.setOnClickListener {
            AlertDialog.Builder(this).run{
                setView(dia2.root)
                dia2.textTitle.text="μ²­μμ μμλ¬Έ"
                dia2.textMessage.setText(R.string.example)
                show()

            }
        }

    }
    private fun makeperiod():String{
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val mon = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val period:String
        if(mon<9){
            if(day<9){
                period=year.toString()+"-"+"0"+(mon+1).toString()+"-"+"0"+day.toString()+" ~ "
            }
            else{
                period=year.toString()+"-"+"0"+(mon+1).toString()+"-"+day.toString()+" ~ "
            }

        }else{
            if(day<9){
                period=year.toString()+"-"+(mon+1).toString()+"-"+"0"+day.toString()+" ~ "
            }
            else{
                period=year.toString()+"-"+(mon+1).toString()+"-"+day.toString()+" ~ "
            }
        }

        return period

    }
    private fun makeperriod():String{
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val mon = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val perriod:String
        if(mon<8){
            if(day<9){
                perriod=year.toString()+"-"+"0"+(mon+2).toString()+"-"+"0"+day.toString()
            }
            else{
                perriod=year.toString()+"-"+"0"+(mon+2).toString()+"-"+day.toString()
            }

        }else{
            if(day<9){
                perriod=year.toString()+"-"+(mon+2).toString()+"-"+"0"+day.toString()
            }
            else{
                perriod=year.toString()+"-"+(mon+2).toString()+"-"+day.toString()
            }
        }
        return perriod
    }
}
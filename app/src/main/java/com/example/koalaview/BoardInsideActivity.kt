package com.example.koalaview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import com.example.koalaview.MyApplication.Companion.getTime
import com.example.koalaview.MyApplication.Companion.getUid
import com.example.koalaview.databinding.ActivityAddBinding
import com.example.koalaview.databinding.ActivityBoardInsideBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardInsideBinding
    private lateinit var key:String
    private var agreecount:Int=0
    private var agreeuidlist= mutableListOf<String>()

    private lateinit var dataModel: BoardModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityBoardInsideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar4))

        key = intent.getStringExtra("key").toString()

        getBoardData(key)

        binding.boardSettingIcon.setOnClickListener{
            showDialog()
        }
        binding.btnAgree.setOnClickListener {

            AgreeEvent()
        }
        val today=Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time
        Log.d("mobileApp",today.toString())

    }

    private fun AgreeEvent(){
        val database= Firebase.database
        val boardRef=database.getReference("board")

        val uid= FirebaseAuth.getInstance().currentUser?.uid
        var bool=false

        val myUid = getUid()
        val writeUid = dataModel.uid

        if(agreecount==0){
            agreeuidlist.removeAt(0)
            agreeuidlist.add(uid.toString())
            agreecount=agreecount+1
            boardRef.child(key)
                .setValue(BoardModel(
                    dataModel!!.title,
                    dataModel!!.name,
                    dataModel!!.uid,
                    dataModel!!.content,
                    dataModel!!.time,
                    agreecount,agreeuidlist,
                dataModel!!.date,
                dataModel!!.period,
                dataModel!!.perriod))
        }
        try{
            for(i in 0..agreecount){
                if(agreeuidlist[i].equals(uid)){
                    bool=true
                    break
                }
            }
        }catch(e:Exception) {
        }

        if(bool==false){
            agreecount=agreecount+1
            agreeuidlist.add(uid.toString())
            boardRef.child(key)
                .setValue(BoardModel(
                    dataModel!!.title,
                    dataModel!!.name,
                    dataModel!!.uid,
                    dataModel!!.content,
                    dataModel!!.time,
                    agreecount,agreeuidlist,
                    dataModel!!.date,
                dataModel!!.period,
                dataModel!!.perriod))
        }
        Log.d("mobileApp",agreecount.toString())
        Log.d("mobileApp",agreeuidlist.toString())


    }
    private fun getBoardData(key: String) {
        val postListener = object : ValueEventListener {

            override fun onDataChange(datasnapshot: DataSnapshot) {

                try{
                    dataModel = datasnapshot.getValue(BoardModel::class.java)!!
                    binding.titleTv.text=dataModel!!.title
                    binding.contentTv.text=dataModel!!.content
                    binding.timeTv.text="동의 기간   |  "+dataModel!!.period
                    binding.timeTv2.text=dataModel!!.perriod
                    binding.agreeTv.text=" 동의 수      |  "+dataModel!!.agreeCount
                    var myUid = getUid().toString()
                    var writeUid = dataModel!!.uid.toString()

                    agreecount=dataModel!!.agreeCount
                    for(i in 0..agreecount){
                        agreeuidlist.add(dataModel!!.agrees.get(i))
                    }

                    if(myUid.equals(writeUid)){
                        binding.boardSettingIcon.isVisible = true
                    } else {
                    }
                } catch(e:Exception) {
                }
                var myUid = getUid().toString()
                var writeUid = dataModel!!.uid.toString()
                if(myUid.equals(writeUid)){
                    binding.boardSettingIcon.isVisible = true
                } else {
                }

                val today=Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time.time
                val dDay = (today-dataModel!!.date.toLong())/(24 * 60 * 60 * 1000)
                binding.dDayTv.text="                             D- "+(30-dDay).toString()
                binding.progressBar.progress=dataModel!!.agreeCount
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        val database= Firebase.database
        val boardRef=database.getReference("board")
        boardRef.child(key).addValueEventListener(postListener)
    }
    private fun showDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")
        val database= Firebase.database
        val boardRef=database.getReference("board")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {

            Toast.makeText(this, "수정", Toast.LENGTH_LONG).show()
            val intent= Intent(this,BoardEditActivity::class.java)
            intent.putExtra("key",key)
            startActivity(intent)
        }
        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {
            boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제", Toast.LENGTH_LONG).show()
            finish()
        }
    }

}
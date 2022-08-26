package com.example.koalaview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.koalaview.MyApplication.Companion.getTime
import com.example.koalaview.databinding.ActivityBoardEditBinding
import com.example.koalaview.databinding.ActivityBoardInsideBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class BoardEditActivity : AppCompatActivity() {
    private lateinit var key:String
    private lateinit var binding: ActivityBoardEditBinding
    private lateinit var dataModel: BoardModel
    private lateinit var writeUid : String
    private var agreecount:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityBoardEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        setSupportActionBar(findViewById(R.id.toolbar3))
        binding.buttonEdit.setOnClickListener {
            editBoardData(key)
        }

    }

    private fun editBoardData(key:String){
        val database= Firebase.database
        val boardRef=database.getReference("board")
        val agreeuidlist= mutableListOf<String>()
        try{
            for(i in 0..agreecount){
                agreeuidlist.add(dataModel!!.agrees.get(i))
            }
        }catch(e:Exception) {
        }

        boardRef.child(key)
            .setValue(
                BoardModel(binding.addtitleView.text.toString(),
                    binding.addNameView.text.toString(),
                    writeUid,
                    binding.addContView.text.toString(),
                    getTime(),
                    dataModel.agreeCount,
                    agreeuidlist,
                    dataModel.date,
                    dataModel.period,
                    dataModel.perriod
                )
            )
        Toast.makeText(this,"수정완료", Toast.LENGTH_LONG).show()
        finish()
    }
    private fun getBoardData(key: String) {
        val postListener = object : ValueEventListener {

            override fun onDataChange(datasnapshot: DataSnapshot) {
                dataModel = datasnapshot.getValue(BoardModel::class.java)!!
                binding.addtitleView.setText(dataModel?.title)
                binding.addNameView.setText(dataModel?.name)
                binding.addContView.setText(dataModel?.content)
                writeUid = dataModel!!.uid
                agreecount=dataModel!!.agreeCount
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        val database= Firebase.database
        val boardRef=database.getReference("board")
        boardRef.child(key).addValueEventListener(postListener)


    }
}
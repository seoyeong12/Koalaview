package com.example.koalaview

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.get
import com.example.koalaview.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.internal.InternalTokenProvider
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val boardDataList = mutableListOf<BoardModel>()
    private val boardKeyList = mutableListOf<String>()

    private lateinit var boardRVAdapter : BoardListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        binding.btnLogin.setOnClickListener{
            val intent = Intent(this, AuthActivity::class.java)
            intent.putExtra("data","logout")
            startActivity(intent)
        }

        binding.addFab.setOnClickListener{
            if(MyApplication.checkAuth()){
                val intent =Intent()
                intent.action="ACTION_ADD"
                intent.data= Uri.parse("http://www.google.com")
                startActivity(Intent(this, AddActivity::class.java))
            }
            else{
                Toast.makeText(this,"인증진행해주세요..", Toast.LENGTH_SHORT).show()
            }
        }

        boardRVAdapter = BoardListViewAdapter(boardDataList)
        binding.boardListView.adapter = boardRVAdapter

        binding.boardListView.setOnItemClickListener{ parent, view, position, id ->
            val intent = Intent(this, BoardInsideActivity::class.java)
            intent.putExtra("key", boardKeyList[position])
            startActivity(intent)

        }

        getFBBoardData()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_btn1 ->{
                val intent = Intent(this, AuthActivity::class.java)
                intent.putExtra("data","login")
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }
    override fun onStart() {
        super.onStart()
        if(MyApplication.checkAuth() || MyApplication.email != null){
            binding.boardListView.visibility=View.VISIBLE
            binding.addFab.visibility=View.VISIBLE
            binding.startpage.visibility=View.GONE
            binding.btnLogin.visibility=View.GONE
            binding.ivmain.visibility=View.VISIBLE
            binding.toolbar.visibility=View.VISIBLE
        }
        else{
            binding.btnLogin.visibility=View.VISIBLE
            binding.addFab.visibility=View.GONE
            binding.boardListView.visibility=View.GONE
            binding.startpage.visibility=View.VISIBLE
            binding.drawer.visibility=View.VISIBLE
            binding.ivmain.visibility=View.GONE
            binding.toolbar.visibility=View.GONE
        }
    }

    private fun getFBBoardData(){

        val postListener = object : ValueEventListener {

            override fun onDataChange(datasnapshot: DataSnapshot) {
                boardDataList.clear()

                for (dataModel in datasnapshot.children){
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }
                boardKeyList.reverse()
                boardDataList.reverse()
                boardRVAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        val database= Firebase.database
        val boardRef=database.getReference("board")
        boardRef.addValueEventListener(postListener)

    }

}
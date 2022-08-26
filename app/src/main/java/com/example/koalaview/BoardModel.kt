package com.example.koalaview

import android.icu.text.CaseMap

data class BoardModel (

    val title: String = "",
    val name: String = "",
    val uid: String = "",
    val content: String = "",
    val time: String = "",   // 날짜
    var agreeCount:Int=0,
    var agrees: MutableList<String> = mutableListOf(),
    val date:String="",   // 밀리초
    val period:String="",
    val perriod:String=""// 동의 기간
)
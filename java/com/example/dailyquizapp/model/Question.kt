package com.example.dailyquizapp.model

// 定义问题的数据类
data class Question(
    val id: Int, // 问题的唯一ID
    val question: String, // 问题文本
    val answer: String, // 简答题的正确答案
    val category: String? = null, // 问题类别，可为空
    val difficulty: String? = null // 问题难度，可为空
)

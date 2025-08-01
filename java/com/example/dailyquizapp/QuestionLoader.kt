package com.example.dailyquizapp.utils

import android.content.Context
import com.example.dailyquizapp.model.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

// 负责从assets目录加载问题的工具类
class QuestionLoader(private val context: Context) {

    // 加载问题的函数
    fun loadQuestions(): List<Question> {
        val jsonString: String
        try {
            // 从assets目录打开questions.json文件并读取其内容
            jsonString = context.assets.open("questions.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            // 如果发生IO错误，打印堆栈跟踪并返回空列表
            ioException.printStackTrace()
            return emptyList()
        }

        // 使用Gson将JSON字符串解析为Question对象的列表
        // TypeToken用于处理泛型类型（List<Question>）的解析
        val listType = object : TypeToken<List<Question>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }
}

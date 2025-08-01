package com.example.dailyquizapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dailyquizapp.model.Question

@Composable
fun MyScreen(
    questions: List<Question>,
    navController: NavController? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "我的",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 菜单项：查看所有题目
        Button(
            onClick = { navController?.navigate("all_questions") },
            modifier = Modifier.fillMaxWidth(),
            enabled = true // 始终启用，未来可根据需求调整
        ) {
            Text("查看所有题目")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 其他内容（目前留空，可扩展）
        Text(
            text = "更多功能待开发...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

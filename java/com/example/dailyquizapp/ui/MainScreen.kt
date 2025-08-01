package com.example.dailyquizapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.dailyquizapp.model.Question
import java.time.LocalDate
import androidx.navigation.NavController

private const val QUESTIONS_PER_DAY = 10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    questions: List<Question>,
    navController: NavController? = null,
    modifier: Modifier = Modifier
) {
    if (questions.isEmpty()) {
        Column(modifier = modifier.padding(16.dp)) {
            Text("加载中或没有问题...", style = MaterialTheme.typography.headlineSmall)
        }
        return
    }

    val numberOfSets = questions.size / QUESTIONS_PER_DAY
    if (numberOfSets == 0) {
        Column(modifier = modifier.padding(16.dp)) {
            Text(
                text = "题目数量不足${QUESTIONS_PER_DAY}道，请添加更多题目。",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { navController?.navigate("my") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("查看所有问题")
            }
            Spacer(Modifier.height(16.dp))
            questions.forEachIndexed { index, question ->
                QuestionDisplayCard(question = question, questionNumber = index + 1)
                Spacer(Modifier.height(24.dp))
            }
        }
        return
    }

    val dailySetIndex = (LocalDate.now().toEpochDay() % numberOfSets).toInt()
    val startIndex = dailySetIndex * QUESTIONS_PER_DAY
    val endIndex = (startIndex + QUESTIONS_PER_DAY).coerceAtMost(questions.size)
    val dailyQuestions = questions.subList(startIndex, endIndex)

    var currentQuestionIndex by remember { mutableStateOf(0) }

    Column(modifier = modifier.padding(16.dp)) {
        Text("每日十题", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        val currentQuestion = dailyQuestions[currentQuestionIndex]
        QuestionDisplayCard(
            question = currentQuestion,
            questionNumber = currentQuestionIndex + 1
        )

        Spacer(Modifier.height(24.dp))
        Divider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    currentQuestionIndex = (currentQuestionIndex - 1).coerceAtLeast(0)
                },
                enabled = currentQuestionIndex > 0
            ) {
                Text("上一题")
            }

            Text(
                text = "${currentQuestionIndex + 1} / ${dailyQuestions.size}",
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = {
                    currentQuestionIndex = (currentQuestionIndex + 1).coerceAtMost(dailyQuestions.size - 1)
                },
                enabled = currentQuestionIndex < dailyQuestions.size - 1
            ) {
                Text("下一题")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionDisplayCard(
    question: Question,
    questionNumber: Int,
    modifier: Modifier = Modifier
) {
    var userAnswer by remember(question.id) { mutableStateOf("") }
    var showReferenceAnswer by remember(question.id) { mutableStateOf(false) }
    var isAnswerSubmitted by remember(question.id) { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("问题 $questionNumber", style = MaterialTheme.typography.titleMedium)
            if (question.category != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = question.category,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = question.question,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = userAnswer,
            onValueChange = { newValue ->
                if (!isAnswerSubmitted) {
                    userAnswer = newValue
                }
            },
            label = { Text("你的答案") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            enabled = !isAnswerSubmitted
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                isAnswerSubmitted = true
                showReferenceAnswer = false
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = userAnswer.isNotBlank() && !isAnswerSubmitted
        ) {
            Text("提交答案")
        }

        if (isAnswerSubmitted) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = """你已提交答案。请点击"显示参考答案"进行比对。""",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { showReferenceAnswer = !showReferenceAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            val buttonText = if (showReferenceAnswer) "隐藏参考答案" else "显示参考答案"
            Text(buttonText)
        }

        if (showReferenceAnswer) {
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "参考答案",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = question.answer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
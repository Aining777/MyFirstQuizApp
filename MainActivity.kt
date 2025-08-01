package com.example.dailyquizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dailyquizapp.model.Question
import com.example.dailyquizapp.ui.AllQuestionsScreen
import com.example.dailyquizapp.ui.MainScreen
import com.example.dailyquizapp.ui.MyScreen
import com.example.dailyquizapp.ui.theme.DailyQuizAppTheme
import com.example.dailyquizapp.utils.QuestionLoader
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.navigation.NavBackStackEntry

// ViewModel用于管理问题数据
class QuestionViewModel : ViewModel() {
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    init {
        viewModelScope.launch {
            val questionLoader = QuestionLoader(ApplicationContextProvider.context)
            val loadedQuestions = questionLoader.loadQuestions()
            _questions.value = loadedQuestions
        }
    }
}

// 提供ApplicationContext的辅助类
object ApplicationContextProvider {
    lateinit var context: android.content.Context
}

// 主Activity
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationContextProvider.context = applicationContext

        setContent {
            DailyQuizAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel = QuestionViewModel()
                    val questions by produceState(initialValue = emptyList<Question>(), producer = {
                        viewModel.questions.collect { value = it }
                    })

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Home, contentDescription = "首页") },
                                    label = { Text("首页") },
                                    selected = currentRoute == "main",
                                    onClick = { navController.navigate("main") { launchSingleTop = true; restoreState = true } }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Person, contentDescription = "我的") },
                                    label = { Text("我的") },
                                    selected = currentRoute == "my",
                                    onClick = { navController.navigate("my") { launchSingleTop = true; restoreState = true } }
                                )
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "main",
                            modifier = Modifier.padding(innerPadding).fillMaxSize() // 确保填充
                        ) {
                            composable("main") {
                                MainScreen(
                                    questions = questions,
                                    navController = navController
                                )
                            }
                            composable("my") {
                                MyScreen(
                                    questions = questions,
                                    navController = navController
                                )
                            }
                            composable("all_questions") {
                                AllQuestionsScreen(
                                    questions = questions,
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}
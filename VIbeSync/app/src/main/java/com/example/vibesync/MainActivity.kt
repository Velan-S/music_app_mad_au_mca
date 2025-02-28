package com.example.vibesync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import com.example.vibesync.ui.theme.VibeSyncTheme
import com.example.vibesync.ui.theme.Home
import com.example.vibesync.ui.theme.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }

            VibeSyncTheme(darkTheme = isDarkTheme) {
                val homeViewModel: HomeViewModel = viewModel()
                Home(
                    toggleTheme = { isDarkTheme = !isDarkTheme },
                    navigateToFullList = { category -> println("Navigating to: $category") },
                    viewModel = homeViewModel
                )

            }
        }
    }
}

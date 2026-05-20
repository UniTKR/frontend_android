package com.unitt.unitt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.unitt.unitt.designsystem.UniTTAppTheme
import com.unitt.unitt.navigation.UniTTApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniTTAppTheme {
                UniTTApp()
            }
        }
    }
}

package com.example.laboratoriono12

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.laboratoriono12.ui.theme.LaboratorioNo12Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaboratorioNo12Theme  {
                AppNavigation()
            }
        }
    }
}

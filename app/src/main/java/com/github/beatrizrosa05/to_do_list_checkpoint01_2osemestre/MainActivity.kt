package com.github.beatrizrosa05.to_do_list_checkpoint01_2osemestre

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.beatrizrosa05.to_do_list_checkpoint01_2osemestre.navigation.AppNavigation
import com.github.beatrizrosa05.to_do_list_checkpoint01_2osemestre.viewmodel.TarefaViewModel
import com.github.beatrizrosa05.to_do_list_checkpoint01_2osemestre.ui.theme.ToDoListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoListTheme {
                val viewModel: TarefaViewModel = viewModel(
                    factory = TarefaViewModel.factory(applicationContext)
                )
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}

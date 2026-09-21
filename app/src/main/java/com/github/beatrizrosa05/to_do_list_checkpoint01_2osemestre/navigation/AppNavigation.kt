package com.github.beatrizrosa05.to_do_list_checkpoint01_2osemestre.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.beatrizrosa05.to_do_list_checkpoint01_2osemestre.ui.FormularioTarefaScreen
import com.github.beatrizrosa05.to_do_list_checkpoint01_2osemestre.ui.ListaTarefasScreen
import com.github.beatrizrosa05.to_do_list_checkpoint01_2osemestre.viewmodel.TarefaViewModel


@Composable
fun AppNavigation(viewModel: TarefaViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            ListaTarefasScreen(
                viewModel = viewModel,
                onNovaTarefa = { navController.navigate("formulario/0") },
                onEditarTarefa = { id -> navController.navigate("formulario/$id") }
            )
        }
        composable("formulario/{tarefaId}") { backStackEntry ->
            val tarefaId = backStackEntry.arguments?.getString("tarefaId")?.toInt() ?: 0
            FormularioTarefaScreen(
                viewModel = viewModel,
                tarefaId = tarefaId,
                onVoltar = { navController.popBackStack() }
            )
        }
    }
}
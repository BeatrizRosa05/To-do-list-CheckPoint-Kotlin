package com.github.beatrizrosa05.to_do_list_checkpoint01_2osemestre.repository

import com.github.beatrizrosa05.to_do_list_checkpoint01_2osemestre.data.Tarefa
import com.github.beatrizrosa05.to_do_list_checkpoint01_2osemestre.data.TarefaDao
import kotlinx.coroutines.flow.Flow

class TarefaRepository(private val dao: TarefaDao) {

    val tarefas: Flow<List<Tarefa>> = dao.listarTodas()

    suspend fun inserir(tarefa: Tarefa) = dao.inserir(tarefa)

    suspend fun atualizar(tarefa: Tarefa) = dao.atualizar(tarefa)

    suspend fun deletar(tarefa: Tarefa) = dao.deletar(tarefa)
}

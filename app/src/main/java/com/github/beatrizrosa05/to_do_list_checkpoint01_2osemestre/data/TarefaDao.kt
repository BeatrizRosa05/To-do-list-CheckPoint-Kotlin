package com.github.beatrizrosa05.to_do_list_checkpoint01_2osemestre.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TarefaDao {

    @Query("""
        SELECT * FROM tarefas
        ORDER BY dataHora IS NULL, dataHora ASC, dataCriacao DESC
    """)
    fun listarTodas(): Flow<List<Tarefa>>

    @Insert
    suspend fun inserir(tarefa: Tarefa)

    @Update
    suspend fun atualizar(tarefa: Tarefa)

    @Delete
    suspend fun deletar(tarefa: Tarefa)
}
package com.github.beatrizrosa05.to_do_list_checkpoint01_2osemestre.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.beatrizrosa05.to_do_list_checkpoint01_2osemestre.data.Tarefa
import com.github.beatrizrosa05.to_do_list_checkpoint01_2osemestre.viewmodel.TarefaViewModel
import java.util.Calendar
import androidx.compose.ui.window.Dialog

@Composable
fun FormularioTarefaScreen(
    viewModel: TarefaViewModel,
    tarefaId: Int,
    onVoltar: () -> Unit
) {
    val tarefas by viewModel.tarefas.collectAsStateWithLifecycle()
    val tarefaExistente = remember(tarefas, tarefaId) {
        tarefas.find { it.id == tarefaId }
    }

    FormularioTarefaContent(
        isEdicao = tarefaId != 0,
        tituloInicial = tarefaExistente?.titulo ?: "",
        descricaoInicial = tarefaExistente?.descricao ?: "",
        dataHoraInicial = tarefaExistente?.dataHora,
        onSalvar = { titulo, descricao, dataHora ->
            if (tarefaId == 0) {
                viewModel.inserir(Tarefa(titulo = titulo, descricao = descricao, dataHora = dataHora))
            } else {
                tarefaExistente?.let {
                    viewModel.atualizar(it.copy(titulo = titulo, descricao = descricao, dataHora = dataHora))
                }
            }
            onVoltar()
        },
        onVoltar = onVoltar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioTarefaContent(
    isEdicao: Boolean,
    tituloInicial: String,
    descricaoInicial: String,
    dataHoraInicial: Long?,
    onSalvar: (titulo: String, descricao: String, dataHora: Long?) -> Unit,
    onVoltar: () -> Unit
) {
    var titulo by remember(tituloInicial) { mutableStateOf(tituloInicial) }
    var descricao by remember(descricaoInicial) { mutableStateOf(descricaoInicial) }
    var temDataHora by remember(dataHoraInicial) { mutableStateOf(dataHoraInicial != null) }

    val calendarioInicial = remember(dataHoraInicial) {
        dataHoraInicial?.let { Calendar.getInstance().apply { timeInMillis = it } }
    }
    var ano by remember { mutableStateOf(calendarioInicial?.get(Calendar.YEAR)) }
    var mes by remember { mutableStateOf(calendarioInicial?.get(Calendar.MONTH)) }
    var dia by remember { mutableStateOf(calendarioInicial?.get(Calendar.DAY_OF_MONTH)) }
    var hora by remember { mutableStateOf(calendarioInicial?.get(Calendar.HOUR_OF_DAY)) }
    var minuto by remember { mutableStateOf(calendarioInicial?.get(Calendar.MINUTE)) }

    var mostrarSeletorData by remember { mutableStateOf(false) }
    var mostrarSeletorHora by remember { mutableStateOf(false) }

    if (mostrarSeletorData) {
        val estadoDatePicker = rememberDatePickerState(
            initialSelectedDateMillis = if (ano != null) paraMillisUtcDoDatePicker(ano!!, mes!!, dia!!) else null
        )
        DatePickerDialog(
            onDismissRequest = { mostrarSeletorData = false },
            confirmButton = {
                TextButton(onClick = {
                    estadoDatePicker.selectedDateMillis?.let { millisUtc ->
                        val (a, m, d) = extrairDataDoDatePicker(millisUtc)
                        ano = a; mes = m; dia = d
                    }
                    mostrarSeletorData = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarSeletorData = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = estadoDatePicker)
        }
    }

    if (mostrarSeletorHora) {
        val estadoTimePicker = rememberTimePickerState(
            initialHour = hora ?: 12,
            initialMinute = minuto ?: 0,
            is24Hour = true
        )
        Dialog(onDismissRequest = { mostrarSeletorHora = false }) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TimePicker(state = estadoTimePicker)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { mostrarSeletorHora = false }) { Text("Cancelar") }
                    TextButton(onClick = {
                        hora = estadoTimePicker.hour
                        minuto = estadoTimePicker.minute
                        mostrarSeletorHora = false
                    }) { Text("OK") }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdicao) "Editar Tarefa" else "Nova Tarefa") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text("Definir data e horário", modifier = Modifier.weight(1f))
                Switch(checked = temDataHora, onCheckedChange = { temDataHora = it })
            }
            if (temDataHora) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = { mostrarSeletorData = true }, modifier = Modifier.weight(1f)) {
                        Text(if (ano != null) String.format("%02d/%02d/%04d", dia, mes!! + 1, ano) else "Selecionar data")
                    }
                    OutlinedButton(onClick = { mostrarSeletorHora = true }, modifier = Modifier.weight(1f)) {
                        Text(if (hora != null) String.format("%02d:%02d", hora, minuto) else "Selecionar hora")
                    }
                }
            }
            Button(
                onClick = {
                    val dataHora = if (temDataHora) combinarDataHora(ano!!, mes!!, dia!!, hora!!, minuto!!) else null
                    onSalvar(titulo.trim(), descricao.trim(), dataHora)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = titulo.isNotBlank() && (!temDataHora || (ano != null && hora != null))
            ) {
                Text("Salvar")
            }
        }
    }
}

// Funções auxiliares
fun paraMillisUtcDoDatePicker(ano: Int, mes: Int, dia: Int): Long {
    val cal = Calendar.getInstance()
    cal.set(ano, mes, dia, 0, 0, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

fun extrairDataDoDatePicker(millisUtc: Long): Triple<Int, Int, Int> {
    val cal = Calendar.getInstance().apply { timeInMillis = millisUtc }
    return Triple(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
}

fun combinarDataHora(ano: Int, mes: Int, dia: Int, hora: Int, minuto: Int): Long {
    val cal = Calendar.getInstance()
    cal.set(ano, mes, dia, hora, minuto, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

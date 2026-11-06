package com.example.ecoreport.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecoreport.data.remote.ReporteDTO

@Composable
fun PantallaDetalleRemoto(
    reporteDto: ReporteDTO?,
    actualizarReporteRemoto: (Int, String, String, String, String, String, String) -> Unit,
    eliminarReporteRemoto: (Int) -> Unit,
    volver: () -> Unit
) {
    if (reporteDto == null) {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Text("No se seleccionó ningún reporte remoto.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = volver) { Text("Volver") }
        }
        return
    }

    var titulo by remember { mutableStateOf(reporteDto.titulo) }
    var descripcion by remember { mutableStateOf(reporteDto.descripcion) }
    var categoria by remember { mutableStateOf(reporteDto.categoria) }
    var ubicacion by remember { mutableStateOf(reporteDto.ubicacion) }
    var estado by remember { mutableStateOf(reporteDto.estado) }
    var fecha by remember { mutableStateOf(reporteDto.fecha) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "Detalle Reporte Remoto #${reporteDto.id}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoría") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = ubicacion,
            onValueChange = { ubicacion = it },
            label = { Text("Ubicación") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = estado,
            onValueChange = { estado = it },
            label = { Text("Estado") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                actualizarReporteRemoto(reporteDto.id, titulo, descripcion, categoria, ubicacion, estado, fecha)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ACTUALIZAR EN SERVIDOR")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { eliminarReporteRemoto(reporteDto.id) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ELIMINAR REMOTO")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = volver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("VOLVER")
        }
    }
}
package com.example.ecoreport.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.viewmodel.ReporteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaReportes(
    viewModel: ReporteViewModel,
    seleccionarReporte: (ReporteEntity) -> Unit,
    onBusqueda: (String) -> Unit,
    volver: () -> Unit
) {
    // Recolectamos los StateFlow del ViewModel para los filtros solicitados
    val estadoFiltro by viewModel.estadoFiltro.collectAsState()
    val categoriaFiltro by viewModel.categoriaFiltro.collectAsState()

    // Estado local para el buscador por texto (título)
    var textoBusqueda by remember { mutableStateOf("") }

    // Estados para controlar el menú desplegable (Select de Estados)
    var expandedEstado by remember { mutableStateOf(false) }
    val estadosOpciones = listOf("TODOS", "PENDIENTE", "EN_PROCESO", "RESUELTO")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Reportes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // 1. INPUT DE BÚSQUEDA POR TÍTULO (Conservado)
        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = { nuevoTexto ->
                textoBusqueda = nuevoTexto
                onBusqueda(nuevoTexto)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar por título...") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // 2. SELECT PARA LOS ESTADOS (StateFlow)
        ExposedDropdownMenuBox(
            expanded = expandedEstado,
            onExpandedChange = { expandedEstado = !expandedEstado }
        ) {
            OutlinedTextField(
                value = if (estadoFiltro.isEmpty()) "TODOS" else estadoFiltro,
                onValueChange = {},
                readOnly = true,
                label = { Text("Filtrar por Estado") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstado) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenu(
                expanded = expandedEstado,
                onDismissRequest = { expandedEstado = false }
            ) {
                estadosOpciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            val valorSeleccionado = if (opcion == "TODOS") "" else opcion
                            viewModel.actualizarEstadoFiltro(valorSeleccionado)
                            expandedEstado = false
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // 3. INPUT PARA LAS CATEGORÍAS (StateFlow)
        OutlinedTextField(
            value = categoriaFiltro,
            onValueChange = { nuevoTexto ->
                viewModel.actualizarCategoriaFiltro(nuevoTexto)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Filtrar por Categoría") },
            placeholder = { Text("Ej. Ambiental, Agua, etc.") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // 4. BOTÓN PARA ENVIAR / APLICAR LOS FILTROS
        Button(
            onClick = {
                viewModel.aplicarFiltros()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "APLICAR FILTROS")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (viewModel.reportes.isEmpty()) {

            Text(
                text = "No existen reportes que coincidan."
            )

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = viewModel.reportes,
                    key = { reporte ->
                        reporte.id
                    }
                ) { reporte ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                seleccionarReporte(reporte)
                            },

                        shape = RoundedCornerShape(16.dp),

                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {

                            Text(
                                text = reporte.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = "Categoría: ${reporte.categoria}"
                            )

                            Text(
                                text = "Ubicación: ${reporte.ubicacion}"
                            )

                            Text(
                                text = "Estado: ${reporte.estado}"
                            )

                            Text(
                                text = "Fecha: ${reporte.fecha}"
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = "Presiona para ver el detalle",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedButton(
            onClick = volver,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "VOLVER"
            )
        }
    }
}
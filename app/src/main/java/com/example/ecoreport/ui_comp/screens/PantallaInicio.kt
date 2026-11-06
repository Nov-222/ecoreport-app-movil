package com.example.ecoreport.ui_comp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecoreport.data.ReporteEntity

@Composable
fun PantallaInicio(
    reportes: List<ReporteEntity>,
    irRegistro: () -> Unit,
    irRegistroRemoto: () -> Unit, // <-- Añadido para nuevo reporte remoto
    irReportes: () -> Unit,
    irEstadisticas: () -> Unit,
    irReportesApi: () -> Unit
) {
    val pendientes = reportes.count { it.estado == "PENDIENTE" }
    val resueltos = reportes.count { it.estado == "RESUELTO" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "EcoReport",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Reportes ambientales"
        )

        Spacer(Modifier.height(24.dp))

        Text("Total Locales: ${reportes.size}")
        Text("Pendientes Locales: $pendientes")
        Text("Resueltos Locales: $resueltos")

        Spacer(Modifier.height(24.dp))

        // Botón para reporte local
        Button(
            onClick = irRegistro,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("NUEVO REPORTE LOCAL")
        }

        // Botón para reporte remoto (API)
        Button(
            onClick = irRegistroRemoto,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("NUEVO REPORTE REMOTO")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = irReportes,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("VER REPORTES LOCALES")
        }

        Button(
            onClick = irReportesApi,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("VER REPORTES REMOTOS")
        }

        Button(
            onClick = irEstadisticas,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ESTADÍSTICAS")
        }
    }
}
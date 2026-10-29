package com.example.ecoreport.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.data.remote.ReporteApiService
import com.example.ecoreport.repository.ReporteRepository
import com.example.ecoreport.ui.screens.*
import com.example.ecoreport.ui_comp.screens.PantallaInicio
import com.example.ecoreport.viewmodel.ReporteViewModel
import com.example.ecoreport.viewmodel.ReporteViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    repository: ReporteRepository,
    api: ReporteApiService
) {

    val navController =
        rememberNavController()

    val viewModel: ReporteViewModel = viewModel(
        factory = ReporteViewModelFactory(repository)
    )
    val reportes =
        remember {
            mutableStateListOf<
                    ReporteEntity
                    >()
        }

    var reporteSeleccionado by remember {
        mutableStateOf<
                ReporteEntity?
                >(null)
    }

    val scope =
        rememberCoroutineScope()

    suspend fun cargarReportes() {

        val datos =
            repository.obtenerTodos()

        reportes.clear()

        reportes.addAll(
            datos
        )
    }

    suspend fun cargarReportesApi(){
        val datos = api.obtenerReportes()
    }

    LaunchedEffect(Unit) {

        cargarReportes()

    }

    NavHost(
        navController =
            navController,

        startDestination =
            "inicio"
    ) {

        composable(
            "inicio"
        ) {

            PantallaInicio (
                reportes =
                    viewModel.reportes,

                irRegistro = {
                    navController
                        .navigate(
                            "registro"
                        )
                },

                irReportes = {
                    navController
                        .navigate(
                            "reportes"
                        )
                },

                irReportesApi = {
                    navController
                        .navigate(
                            "reportes_api"
                        )
                },

                irEstadisticas = {
                    navController
                        .navigate(
                            "estadisticas"
                        )
                }
            )
        }
        composable(
            "registro"
        ) {

            PantallaRegistro(
                registrarReporte = { reporte ->

                    viewModel.insertar(reporte)

                    navController
                        .popBackStack()
                },

                volver = {

                    navController
                        .popBackStack()
                }
            )
        }
        composable(
            "reportes"
        ) {

            PantallaReportes(
                viewModel = viewModel,

                seleccionarReporte = { reporte ->

                    reporteSeleccionado =
                        reporte

                    navController.navigate(
                        "detalle"
                    )
                },

                onBusqueda = {texto ->
                    viewModel.buscarPorNombre(texto)
                },

                volver = {

                    navController
                        .popBackStack()
                }
            )
        }
        composable(
            "detalle"
        ) {

            PantallaDetalle(
                reporte =
                    reporteSeleccionado,

                actualizarReporte = {
                        reporteActualizado ->
                    viewModel.actualizar(reporteActualizado)
                    reporteSeleccionado = reporteActualizado
                },

                eliminarReporte = { reporte ->

                    viewModel.eliminar(reporte)

                    navController
                        .popBackStack()
                },

                volver = {
                    navController
                        .popBackStack()
                }
            )
        }
        composable(
            "estadisticas"
        ) {

            PantallaEstadisticas(
                reportes =
                    reportes,

                volver = {

                    navController
                        .popBackStack()
                }
            )
        }

        composable(
            "reportes_api"
        ){
            // Lógica añadida para mostrar los datos de la API
            val listaRemota by viewModel.reportesRemotesView.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.cargarReportesRemotos()
            }

            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Button(onClick = {
                    navController.popBackStack()
                }) {
                    Text("Volver")
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Reportes de la API", style = MaterialTheme.typography.headlineMedium)

                LazyColumn {
                    items(listaRemota) { item ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = item.titulo, style = MaterialTheme.typography.titleMedium)
                                Text(text = item.descripcion)
                            }
                        }
                    }
                }
            }
        }
    }
}
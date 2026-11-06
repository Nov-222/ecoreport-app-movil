package com.example.ecoreport.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.data.remote.ReporteApiService
import com.example.ecoreport.data.remote.ReporteDTO
import com.example.ecoreport.repository.ReporteRepository
import com.example.ecoreport.ui.screens.*
import com.example.ecoreport.ui_comp.screens.PantallaInicio
import com.example.ecoreport.viewmodel.ReporteViewModel
import com.example.ecoreport.viewmodel.ReporteViewModelFactory

@Composable
fun AppNavigation(
    repository: ReporteRepository,
    api: ReporteApiService
) {
    val navController = rememberNavController()

    val viewModel: ReporteViewModel = viewModel(
        factory = ReporteViewModelFactory(repository)
    )

    var reporteSeleccionado by remember { mutableStateOf<ReporteEntity?>(null) }
    var reporteRemotoSeleccionado by remember { mutableStateOf<ReporteDTO?>(null) }

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        // --- INICIO ---
        composable("inicio") {
            PantallaInicio(
                reportes = viewModel.reportes,
                irRegistro = { navController.navigate("registro") },
                irRegistroRemoto = { navController.navigate("registro_remoto") },
                irReportes = { navController.navigate("reportes") },
                irReportesApi = { navController.navigate("reportes_remoto") },
                irEstadisticas = { navController.navigate("estadisticas") }
            )
        }

        // --- RUTAS LOCALES (ROOM) ---
        composable("registro") {
            PantallaRegistro(
                registrarReporte = { reporte ->
                    viewModel.insertar(reporte)
                    navController.popBackStack()
                },
                volver = { navController.popBackStack() }
            )
        }

        composable("reportes") {
            PantallaReportes(
                viewModel = viewModel,
                seleccionarReporte = { reporte ->
                    reporteSeleccionado = reporte
                    navController.navigate("detalle")
                },
                onBusqueda = { texto ->
                    viewModel.buscarPorNombre(texto)
                },
                volver = { navController.popBackStack() }
            )
        }

        composable("detalle") {
            PantallaDetalle(
                reporte = reporteSeleccionado,
                actualizarReporte = { reporteActualizado ->
                    viewModel.actualizar(reporteActualizado)
                    reporteSeleccionado = reporteActualizado
                },
                eliminarReporte = { reporte ->
                    viewModel.eliminar(reporte)
                    navController.popBackStack()
                },
                volver = { navController.popBackStack() }
            )
        }

        composable("estadisticas") {
            PantallaEstadisticas(
                reportes = viewModel.reportes,
                volver = { navController.popBackStack() }
            )
        }

        // --- RUTAS REMOTAS (API RETROFIT) ---
        composable("reportes_remoto") {
            PantallaReportesRemotos(
                viewModel = viewModel,
                seleccionarRemoto = { dto ->
                    reporteRemotoSeleccionado = dto
                    navController.navigate("detalle_remoto")
                },
                volver = { navController.popBackStack() }
            )
        }

        composable("registro_remoto") {
            PantallaRegistroRemoto(
                registrarRemoto = { titulo, desc, cat, ub, est, fec ->
                    viewModel.crearNuevoReporteRemoto(titulo, desc, cat, ub, est, fec)
                    navController.popBackStack()
                },
                volver = { navController.popBackStack() }
            )
        }

        composable("detalle_remoto") {
            PantallaDetalleRemoto(
                reporteDto = reporteRemotoSeleccionado,
                actualizarReporteRemoto = { id, titulo, desc, cat, ub, est, fec ->
                    viewModel.editarReporteRemoto(id, titulo, desc, cat, ub, est, fec)
                    navController.popBackStack()
                },
                eliminarReporteRemoto = { id ->
                    viewModel.eliminarReporteRemoto(id)
                    navController.popBackStack()
                },
                volver = { navController.popBackStack() }
            )
        }
    }
}
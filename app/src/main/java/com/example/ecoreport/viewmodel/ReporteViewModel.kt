package com.example.ecoreport.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.data.remote.CrearReporteDto
import com.example.ecoreport.data.remote.ReporteDTO
import com.example.ecoreport.repository.ReporteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReporteViewModel(
    private val repository: ReporteRepository
) : ViewModel() {

    // --- DATOS LOCALES ---
    val reportes = mutableStateListOf<ReporteEntity>()

    private val _estadoFiltro = MutableStateFlow("")
    val estadoFiltro: StateFlow<String> = _estadoFiltro.asStateFlow()

    private val _categoriaFiltro = MutableStateFlow("")
    val categoriaFiltro: StateFlow<String> = _categoriaFiltro.asStateFlow()

    // --- DATOS REMOTOS ---
    private val _reportesRemotos = MutableStateFlow<List<ReporteDTO>>(emptyList())
    val reportesRemotos: StateFlow<List<ReporteDTO>> = _reportesRemotos.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargarReportes()
    }

    fun actualizarEstadoFiltro(estado: String) {
        _estadoFiltro.value = estado
    }

    fun actualizarCategoriaFiltro(categoria: String) {
        _categoriaFiltro.value = categoria
    }

    fun aplicarFiltros() {
        viewModelScope.launch {
            val resultados = repository.obtenerFiltrados(_estadoFiltro.value, _categoriaFiltro.value)
            reportes.clear()
            reportes.addAll(resultados)
        }
    }

    fun cargarReportes() {
        viewModelScope.launch {
            val datos = repository.obtenerTodos()
            reportes.clear()
            reportes.addAll(datos)
        }
    }

    fun insertar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.insertar(reporte)
            cargarReportes()
        }
    }

    fun eliminar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.eliminar(reporte)
            cargarReportes()
        }
    }

    fun actualizar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.actualizar(reporte)
            cargarReportes()
        }
    }

    fun buscarPorNombre(nombre: String) {
        viewModelScope.launch {
            val busqueda = if (nombre.isBlank()) {
                repository.obtenerTodos()
            } else {
                repository.buscarPorNombre(nombre)
            }
            reportes.clear()
            reportes.addAll(busqueda)
        }
    }

    // --- MÉTODOS REMOTOS ---
    fun cargarReportesRemotos() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            try {
                val datos = repository.obtenerReportesRemotos()
                _reportesRemotos.value = datos
            } catch (e: Exception) {
                _error.value = "No se pudieron cargar los reportes remotos: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun crearNuevoReporteRemoto(
        titulo: String,
        descripcion: String,
        categoria: String,
        ubicacion: String,
        estado: String,
        fecha: String
    ) {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            try {
                val reporte = CrearReporteDto(
                    titulo = titulo,
                    descripcion = descripcion,
                    categoria = categoria,
                    ubicacion = ubicacion,
                    estado = estado,
                    fecha = fecha
                )
                repository.crearReporte(reporte)
                cargarReportesRemotos()
            } catch (e: Exception) {
                _error.value = "Hubo un error al crear el registro remoto: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun editarReporteRemoto(
        id: Int,
        titulo: String,
        descripcion: String,
        categoria: String,
        ubicacion: String,
        estado: String,
        fecha: String
    ) {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            try {
                val reporteDto = CrearReporteDto(
                    titulo = titulo,
                    descripcion = descripcion,
                    categoria = categoria,
                    ubicacion = ubicacion,
                    estado = estado,
                    fecha = fecha
                )
                repository.editarReporte(id, reporteDto)
                cargarReportesRemotos()
            } catch (e: Exception) {
                _error.value = "Error al editar el reporte remoto: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun eliminarReporteRemoto(id: Int) {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            try {
                repository.eliminarReporte(id)
                cargarReportesRemotos()
            } catch (e: Exception) {
                _error.value = "Error al eliminar el reporte remoto: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }
}

class ReporteViewModelFactory(
    private val repository: ReporteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReporteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReporteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
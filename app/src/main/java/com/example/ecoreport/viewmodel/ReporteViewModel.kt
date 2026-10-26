package com.example.ecoreport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoreport.data.ReporteEntity
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModelProvider
import com.example.ecoreport.repository.ReporteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReporteViewModel (
    private val repository: ReporteRepository
): ViewModel() {
    val reportes = mutableStateListOf<ReporteEntity>()

    private val _estadoFiltro = MutableStateFlow("")
    val estadoFiltro: StateFlow<String> = _estadoFiltro.asStateFlow()

    private val _categoriaFiltro = MutableStateFlow("")
    val categoriaFiltro: StateFlow<String> = _categoriaFiltro.asStateFlow()

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

            reportes.clear()

            cargarReportes()
        }
    }

    fun buscarPorNombre(nombre : String) {
        viewModelScope.launch {
            val busqueda = if(nombre.isBlank())
            {
                repository.obtenerTodos()
            }
            else
            {
                repository.buscarPorNombre(nombre)
            }

            reportes.clear()

            reportes.addAll(busqueda)
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


}

class ReporteViewModelFactory(
    private val repository: ReporteRepository
) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReporteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReporteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
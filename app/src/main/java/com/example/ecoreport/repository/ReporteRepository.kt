package com.example.ecoreport.repository

import com.example.ecoreport.data.ReporteDao
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.data.remote.ReporteApiService
import com.example.ecoreport.data.remote.ReporteDTO

class ReporteRepository (
    private val reporteDao: ReporteDao,
    private val api: ReporteApiService
) {
    suspend fun obtenerTodos():
            List<ReporteEntity> {
        return reporteDao.obtenerTodos()
    }

    suspend fun insertar(reporte: ReporteEntity){
        reporteDao.insertar(reporte)
    }

    suspend fun actualizar(reporte: ReporteEntity) {
        reporteDao.actualizar(reporte)
    }

    suspend fun eliminar(reporte: ReporteEntity) {
        reporteDao.eliminar(reporte)
    }

    suspend fun buscarPorNombre(nombre : String) : List<ReporteEntity> {
        return reporteDao.obtenerPorNombre(nombre)
    }

    suspend fun obtenerFiltrados(estado: String, categoria: String): List<ReporteEntity> {
        return reporteDao.obtenerFiltrados(estado, categoria)
    }

    //llamada a las funciones remotas
    suspend fun obtenerReportesRemotos():
            List<ReporteDTO> {
        return api.obtenerReportes()
    }
}

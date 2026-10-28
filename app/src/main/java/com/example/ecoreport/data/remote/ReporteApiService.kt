package com.example.ecoreport.data.remote

import retrofit2.http.GET
import retrofit2.http.PUT

interface ReporteApiService {
    @GET("reportes")
    suspend fun obtenerReportes():
            List<ReporteDTO>
}
package com.example.ecoreport.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReporteApiService {

    @GET("reportes")//BASE_URL = http://api.ejemplo.com/reportes
    suspend fun obtenerReportes():
            List<ReporteDTO>

    @POST("reportes")
    suspend fun crearReporte(
        @Body reporte: CrearReporteDto
    ): ReporteDTO

    @PUT("reportes/{id}")
    suspend fun actualizarReporte(
        @Path("id") id: Int,
        @Body reporte: CrearReporteDto
    ): ReporteDTO

    @DELETE("reportes/{id}")
    suspend fun eliminarReporte(
        @Path("id") id: Int
    )
}
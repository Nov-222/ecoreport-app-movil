package com.example.ecoreport.data

import android.R
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReporteDao {

    @Insert
    suspend fun insertar(
        reporte: ReporteEntity
    )

    @Query(
        "SELECT * FROM reportes ORDER BY id DESC"
    )
    suspend fun obtenerTodos():
            List<ReporteEntity>

    @Update
    suspend fun actualizar(
        reporte: ReporteEntity
    )

    @Delete
    suspend fun eliminar(
        reporte: ReporteEntity
    )

    @Query(
        "SELECT * FROM reportes WHERE titulo LIKE '%' || :nombre || '%' ORDER BY id DESC"
    )
    suspend fun  obtenerPorNombre(nombre : String): List<ReporteEntity>

    @Query(
        "SELECT * FROM reportes WHERE (:estado = '' OR estado = :estado) AND (:categoria = '' OR categoria LIKE '%' || :categoria || '%') ORDER BY id DESC"
    )
    suspend fun obtenerFiltrados(estado: String, categoria: String): List<ReporteEntity>
}
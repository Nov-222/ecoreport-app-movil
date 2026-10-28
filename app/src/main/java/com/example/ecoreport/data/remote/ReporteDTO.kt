package com.example.ecoreport.data.remote

data class ReporteDTO(
    val id: Int,

    val titulo: String,

    val descripcion: String,

    val categoria: String,

    val ubicacion: String,

    val estado: String,

    val fecha: String
)
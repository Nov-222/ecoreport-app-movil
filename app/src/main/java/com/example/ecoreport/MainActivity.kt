package com.example.ecoreport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.ecoreport.data.AppDatabase
import com.example.ecoreport.data.remote.RetrofitClient
import com.example.ecoreport.navigation.AppNavigation
import com.example.ecoreport.repository.ReporteRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "ecoreport_database"
        ).build()

        val reporteDao = database.reporteDao()

        // Creamos la instancia de la API y el repositorio
        val apiService = RetrofitClient.api
        val repository = ReporteRepository(reporteDao, apiService)

        setContent {
            AppNavigation(
                repository = repository,
                api = apiService // <-- Pasamos la api requerida aquí
            )
        }
    }
}
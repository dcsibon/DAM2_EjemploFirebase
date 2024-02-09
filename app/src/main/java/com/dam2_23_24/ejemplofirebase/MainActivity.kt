package com.dam2_23_24.ejemplofirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dam2_23_24.ejemplofirebase.navigation.NavManager
import com.dam2_23_24.ejemplofirebase.ui.theme.EjemploFirebaseTheme
import com.dam2_23_24.ejemplofirebase.viewModels.LoginViewModel
import com.dam2_23_24.ejemplofirebase.viewModels.NotesViewModel

/**
 * Esta es la actividad principal de la aplicación, la cual configura y muestra la interfaz de usuario principal.
 * Utiliza Jetpack Compose para definir la UI de la aplicación y maneja la navegación entre distintas vistas a través
 * de un sistema de navegación basado en ViewModel.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginVM : LoginViewModel by viewModels()
        val notesVM : NotesViewModel by viewModels()

        setContent {
            EjemploFirebaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavManager(loginVM, notesVM)
                }
            }
        }
    }
}

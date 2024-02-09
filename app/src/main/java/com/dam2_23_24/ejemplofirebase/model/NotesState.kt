package com.dam2_23_24.ejemplofirebase.model

/**
 * Define el modelo de datos para una nota. Utilizado para gestionar las notas en la base de datos.
 */
data class NotesState(
    val emailUser: String = "",
    val title: String = "",
    val note: String = "",
    val date: String = "",
    val idDoc: String = ""
)

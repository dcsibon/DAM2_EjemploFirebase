package com.dam2_23_24.ejemplofirebase.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam2_23_24.ejemplofirebase.model.NotesState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * ViewModel responsable de gestionar la lógica y el estado de las notas de los usuarios.
 * Proporciona funciones para agregar, recuperar, actualizar y eliminar notas utilizando Firebase Firestore.
 * Mantiene el estado de la lista de notas y la nota actualmente seleccionada o editada.
 *
 * @property auth Instancia de FirebaseAuth utilizada para obtener el usuario actual.
 * @property firestore Instancia de FirebaseFirestore utilizada para operaciones en la base de datos.
 * @property _notesData Flujo de datos que mantiene la lista de notas, privado para el ViewModel.
 * @property notesData Estado público de la lista de notas, expuesto a la UI.
 * @property state Estado de la nota actualmente seleccionada o editada.
 * @property title Título de la nota actualmente seleccionada o editada.
 * @property note Contenido de la nota actualmente seleccionada o editada.
 */
class NotesViewModel:ViewModel() {
    // DCS - Definición de variables y funciones para manejar las operaciones sobre las notas.

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore

    private val _notesData = MutableStateFlow<List<NotesState>>(emptyList())
    val notesData: StateFlow<List<NotesState>> = _notesData

    var state by mutableStateOf(NotesState())
        private set
    var title by mutableStateOf("")
        private set
    var note by mutableStateOf("")
        private set

    /**
     * Actualiza el valor de un campo específico de la nota actualmente seleccionada o editada.
     *
     * @param value Nuevo valor para el campo especificado.
     * @param text Campo a actualizar, puede ser "title" o "note".
     */
    fun onValueChange(value:String, text: String) {
        when(text){
            "title" -> state = state.copy(title = value)
            "note" -> state = state.copy(note = value)
        }
    }

    /**
     * Recupera todas las notas del usuario actual desde Firestore y actualiza el estado de la lista de notas.
     */
    fun fetchNotes() {
        val email = auth.currentUser?.email

        // DCS - addSnapshotListener ya trae todas las funciones necesarias para la concurrencia
        // de datos y es asíncrono, por lo que no es necesario introducir el viewModelScope.
        // Ya lleva incluida todas las corrutinas necesarias...
        firestore.collection("Notes")
            .whereEqualTo("emailUser", email.toString())
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                val documents = mutableListOf<NotesState>()
                if (querySnapshot != null) {
                    for (document in querySnapshot) {
                        val myDocument = document.toObject(NotesState::class.java).copy(idDoc = document.id)
                        documents.add(myDocument)
                    }
                }
                _notesData.value = documents
            }
    }

    /**
     * Guarda una nueva nota en Firestore y ejecuta una acción de éxito una vez completada.
     *
     * @param onSuccess Acción a ejecutar una vez que la nota ha sido guardada con éxito.
     */
    fun saveNewNote(onSuccess:() -> Unit) {
        val email = auth.currentUser?.email

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newNote = hashMapOf(
                    "title" to title,
                    "note" to note,
                    "date" to formatDate(),
                    "emailUser" to email.toString()
                )
                // DCS - Utilizar la instancia de Firestore para agregar una nota
                firestore.collection("Notes")
                    .add(newNote)
                    .addOnSuccessListener {
                        onSuccess()
                        Log.d("GUARDAR OK", "Se guardó la nota correctamente en Firestore")
                    }
                    .addOnFailureListener {
                        Log.d("ERROR AL GUARDAR", "ERROR al guardar en Firestore")
                    }
                // DCS - Si se guarda con éxito limpiamos las variables
                resetInfoNote()
            } catch (e:Exception){
                Log.d("ERROR GUARDAR NOTA","Error al guardar ${e.localizedMessage} ")
            }
        }
    }

    /**
     * Formatea la fecha actual en un formato de cadena específico.
     * Utiliza el patrón "dd/MM/yyyy" para generar la representación de la fecha,
     * adecuado para el almacenamiento o visualización de fechas en la aplicación.
     *
     * @return Una cadena que representa la fecha actual en el formato "dd/MM/yyyy".
     */
    private fun formatDate(): String {
        val currentDate : Date = Calendar.getInstance().time
        val res = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return res.format(currentDate)
    }

    /**
     * Recupera los detalles de una nota específica por su ID desde Firestore y actualiza el estado de la nota seleccionada.
     *
     * @param documentId ID del documento de la nota a recuperar.
     */
    fun getNoteById(documentId: String) {
        // DCS - Utiliza la instancia de Firestore para obtener la info de una nota por su id
        firestore.collection("Notes")
            .document(documentId)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (querySnapshot != null){
                    val note = querySnapshot.toObject(NotesState::class.java)
                    state = state.copy(
                        title = note?.title ?: "",
                        note = note?.note ?: ""
                    )
                }
            }
    }

    /**
     * Actualiza una nota existente en Firestore con los valores actuales del estado y ejecuta una acción de éxito una vez completado.
     *
     * @param idDoc ID del documento de la nota a actualizar.
     * @param onSuccess Acción a ejecutar una vez que la nota ha sido actualizada con éxito.
     */
    fun updateNote(idDoc: String, onSuccess:() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val editNote = hashMapOf(
                    "title" to state.title,
                    "note" to state.note,
                )
                // DCS - Utiliza la instancia de Firestore para actualizar la info de una nota por su id
                firestore.collection("Notes").document(idDoc)
                    .update(editNote as Map<String, Any>)
                    .addOnSuccessListener {
                        onSuccess()
                        Log.d("ACTUALIZAR OK", "Se actualizó la nota correctamente en Firestore")
                    }
                    .addOnFailureListener {
                        Log.d("ERROR AL ACTUALIZAR", "ERROR al actualizar una nota en Firestore")
                    }
                // DCS - Si se guarda con éxito limpiamos las variables
                resetInfoNote()
            } catch (e:Exception) {
                Log.d("ERROR EDITAR NOTA","Error al editar ${e.localizedMessage} ")
            }
        }
    }

    /**
     * Elimina una nota específica de Firestore y ejecuta una acción de éxito una vez completado.
     *
     * @param idDoc ID del documento de la nota a eliminar.
     * @param onSuccess Acción a ejecutar una vez que la nota ha sido eliminada exitosamente.
     */
    fun deleteNote(idDoc: String, onSuccess:() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // DCS - Utiliza la instancia de Firestore para eliminar una nota por su id
                firestore.collection("Notes").document(idDoc)
                    .delete()
                    .addOnSuccessListener {
                        onSuccess()
                        Log.d("ELIMINAR OK", "Se eliminó la nota correctamente en Firestore")
                    }
                    .addOnFailureListener {
                        Log.d("ERROR AL ELIMINAR", "ERROR al eliminar una nota en Firestore")
                    }
            } catch (e:Exception) {
                Log.d("ERROR BORRAR NOTA","Error al eliminar ${e.localizedMessage} ")
            }
        }
    }

    /**
     * Cierra la sesión del usuario actual en Firebase Auth.
     */
    fun signOut() {
        auth.signOut()
    }

    /**
     * Limpia las variables que controlan desde el ViewModel la info de la nota.
     */
    private fun resetInfoNote() {
        title = ""
        note = ""
    }

    /**
     * Actualiza el título de la nota actualmente seleccionada o editada.
     *
     * @param title Nuevo título a establecer.
     */
    fun changeTitle(title: String) {
        this.title = title
    }

    /**
     * Actualiza el contenido de la nota actualmente seleccionada o editada.
     *
     * @param note Nuevo contenido a establecer.
     */
    fun changeNote(note: String) {
        this.note = note
    }
}
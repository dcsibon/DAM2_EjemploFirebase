package com.dam2_23_24.ejemplofirebase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dam2_23_24.ejemplofirebase.viewModels.LoginViewModel
import com.dam2_23_24.ejemplofirebase.viewModels.NotesViewModel
import com.dam2_23_24.ejemplofirebase.views.login.BlankView
import com.dam2_23_24.ejemplofirebase.views.notes.HomeView
import com.dam2_23_24.ejemplofirebase.views.login.TabsView
import com.dam2_23_24.ejemplofirebase.views.notes.AddNoteView
import com.dam2_23_24.ejemplofirebase.views.notes.EditNoteView

@Composable
fun NavManager(loginVM: LoginViewModel, notesVM: NotesViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Blank" ){
        composable("Blank"){
            BlankView(navController)
        }
        composable("Login"){
            TabsView(navController, loginVM)
        }
        composable("Home"){
            HomeView(navController, notesVM)
        }
        composable("AddNoteView"){
            AddNoteView(navController, notesVM)
        }
        composable("EditNoteView/{idDoc}", arguments = listOf(
            navArgument("idDoc") { type = NavType.StringType }
        )){
            val idDoc = it.arguments?.getString("idDoc") ?: ""
            EditNoteView(navController, notesVM, idDoc)
        }
    }
}
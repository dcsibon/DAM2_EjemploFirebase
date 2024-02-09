package com.dam2_23_24.ejemplofirebase.views.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

/**
 * Vista composable que sirve como punto de entrada o pantalla inicial,
 * redirigiendo al usuario a la pantalla de inicio de sesión o al home según si hay una sesión iniciada.
 */
@Composable
fun BlankView(navController: NavController){

    LaunchedEffect(Unit){
        if (!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){
            navController.navigate("Home")
        }else{
            navController.navigate("Login")
        }
    }

}
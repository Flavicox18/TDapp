package com.flavicox.tdapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPackageScreen(navController: NavController) {
    var query by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    LaunchedEffect(isSearching) {
        if (isSearching) {
            delay(2000) // Espera 2 segundos
            navController.navigate("details") // Navega a la pantalla de detalles
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFFE0E0E0)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Logo
        Box(
            modifier = Modifier
                .background(Color(0xFF006400))
                .fillMaxWidth()
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.td_logo),
                contentDescription = "Logo de la empresa",
                modifier = Modifier.height(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Mensaje principal de bienvenida
        Text(
            text = "Bienvenido",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mensaje secundario
        Text(
            text = if (isSearching) "Cargando búsqueda ..." else "Encuentra el paquete que desees al instante!",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Search Bar
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(2.dp),
                placeholder = { Text("Código", fontSize = 12.sp, lineHeight = 15.sp) },
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp, lineHeight = 15.sp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { isSearching = true } // Activar estado de búsqueda
                )
            )
            IconButton(
                onClick = { isSearching = true }, // Activar estado de búsqueda al presionar el ícono de búsqueda
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color(0xFF006400))
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Imagen de paquete o barra de carga
        if (isSearching) {
            CircularProgressIndicator(
                color = Color(0xFF006400),
                modifier = Modifier.size(200.dp)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.resource_package),
                contentDescription = "Paquete",
                modifier = Modifier.size(350.dp)
            )
        }
    }
}
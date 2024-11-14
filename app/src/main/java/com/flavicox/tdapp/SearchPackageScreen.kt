package com.flavicox.tdapp

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.flavicox.tdapp.entity.Encomienda
import com.flavicox.tdapp.service.EncomiendaService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPackageScreen(navController: NavController) {
    var query by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val encomiendaService = remember { ApiClient.retrofit.create(EncomiendaService::class.java) }
    val scope = rememberCoroutineScope()

    fun obtenerEncomienda(id_encomienda: Int) {
        isSearching = true
        errorMessage = null

        scope.launch(Dispatchers.IO) {
            try {
                val response = encomiendaService.obtenerEncomiendaPorId(id_encomienda).execute()

                withContext(Dispatchers.Main) {
                    isSearching = false

                    if (response.isSuccessful && response.body() != null) {
                        val encomienda = response.body()!!

                        scope.launch(Dispatchers.IO) {
                            val historialResponse = encomiendaService.listarHistorialPorEncomiendaId(id_encomienda).execute()

                            withContext(Dispatchers.Main) {
                                if (historialResponse.isSuccessful) {
                                    navController.navigate("details/${encomienda.id_encomienda}")
                                } else {
                                    errorMessage = "Error obteniendo historial: ${historialResponse.code()}"
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        errorMessage = if (response.code() == 404) "Encomienda no encontrada" else "Error del servidor: ${response.code()}"
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isSearching = false
                    errorMessage = when (e) {
                        is SocketTimeoutException -> "Tiempo de espera agotado"
                        is UnknownHostException -> "No se puede conectar al servidor. Verifica tu conexión."
                        else -> "Error de conexión: ${e.localizedMessage}"
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
                    onSearch = {
                        if (query.isNotEmpty()) obtenerEncomienda(query.toInt())
                    }
                )
            )
            IconButton(
                onClick = {
                    if (query.isNotEmpty()) obtenerEncomienda(query.toInt())
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color(0xFF006400))
            }
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
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

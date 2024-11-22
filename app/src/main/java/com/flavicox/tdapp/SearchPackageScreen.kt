package com.flavicox.tdapp

import android.Manifest
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                val spokenText = result.data
                    ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    ?.firstOrNull()
                    ?: ""
                query = processNumbers(spokenText) // Procesa los números hablados
            } else {
                Toast.makeText(context, "No se capturó voz", Toast.LENGTH_SHORT).show()
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            // Si el permiso es concedido, abre la interfaz de grabación de voz
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora")
            }
            speechRecognizerLauncher.launch(intent)
        } else {
            Toast.makeText(context, "Permiso de audio denegado", Toast.LENGTH_SHORT).show()
        }
    }

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
                modifier = Modifier.padding(end = 10.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color(0xFF006400))
            }

            IconButton (
                onClick = {
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                },
                modifier = Modifier.padding(end = 15.dp)
                    .size(30.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.microfono),
                    contentDescription = "Paquete"
                )
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

fun processNumbers(input: String): String {
    val numbersMap = mapOf(
        "cero" to "0", "uno" to "1", "dos" to "2", "tres" to "3",
        "cuatro" to "4", "cinco" to "5", "seis" to "6", "siete" to "7",
        "ocho" to "8", "nueve" to "9", "diez" to "10", "once" to "11",
        "doce" to "12", "trece" to "13", "catorce" to "14", "quince" to "15",
        "dieciséis" to "16", "diecisiete" to "17", "dieciocho" to "18", "diecinueve" to "19",
        "veinte" to "20", "veintiuno" to "21", "veintidós" to "22", "veintitrés" to "23",
        "veinticuatro" to "24", "veinticinco" to "25", "veintiséis" to "26", "veintisiete" to "27",
        "veintiocho" to "28", "veintinueve" to "29", "treinta" to "30", "treinta y uno" to "31",
        "treinta y dos" to "32", "treinta y tres" to "33", "treinta y cuatro" to "34",
        "treinta y cinco" to "35", "treinta y seis" to "36", "treinta y siete" to "37",
        "treinta y ocho" to "38", "treinta y nueve" to "39", "cuarenta" to "40"
    )
    val regex = Regex(numbersMap.keys.joinToString("|", "\\b(", ")\\b"))
    return regex.replace(input) { matchResult ->
        numbersMap[matchResult.value.lowercase()] ?: matchResult.value
    }
}
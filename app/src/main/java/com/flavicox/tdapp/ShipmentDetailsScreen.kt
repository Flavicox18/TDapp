@file:OptIn(ExperimentalMaterial3Api::class)

package com.flavicox.tdapp

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flavicox.tdapp.entity.Encomienda
import com.flavicox.tdapp.entity.HistorialEncomienda
import com.flavicox.tdapp.service.EncomiendaService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ShipmentDetailsScreen(id_encomienda: Int) {
    val context = LocalContext.current
    val encomiendaService = ApiClient.retrofit.create(EncomiendaService::class.java)
    var encomienda by remember { mutableStateOf<Encomienda?>(null) }
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    var ultimoEvento by remember { mutableStateOf<HistorialEncomienda?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Función para obtener detalles de la encomienda
    fun obtenerDetallesEncomienda(id_encomienda: Int) {
        encomiendaService.obtenerEncomiendaPorId(id_encomienda).enqueue(object : Callback<Encomienda> {
            override fun onResponse(call: Call<Encomienda>, response: Response<Encomienda>) {
                if (response.isSuccessful) {
                    encomienda = response.body()
                } else {
                    Toast.makeText(context, "Error al obtener detalles de la encomienda", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Encomienda>, t: Throwable) {
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Llamada inicial para obtener los detalles de la encomienda
    LaunchedEffect(Unit) {
        obtenerDetallesEncomienda(id_encomienda)
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = { HistoryModalContent(id_encomienda) },
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE0E0E0))
                    .verticalScroll(rememberScrollState()),
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

                // Mostrar datos de la encomienda si están disponibles
                encomienda?.let {
                    // Código y Estado
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Envío Código:",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = it.id_encomienda?.toString() ?: "No disponible",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = if(encomienda!!.estado == "Pendiente"){
                                Modifier
                                    .background(color = Color(0xFFF8BABB), shape = RoundedCornerShape(8.dp))
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            } else {
                                if(encomienda!!.estado == "Entregado"){
                                    Modifier
                                        .background(color = Color(0xFFD7F8BA), shape = RoundedCornerShape(8.dp))
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                } else {
                                    Modifier
                                        .background(Color(0xFFFFF9C4), shape = RoundedCornerShape(8.dp))
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                }
                            },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Estado:",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = it.estado ?: "Estado no disponible",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Ubicación Actual
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF006400), shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Column (horizontalAlignment = Alignment.CenterHorizontally){
                            scope.launch(Dispatchers.IO) {
                                try {
                                    val response = encomiendaService.obtenerUltimoHistorial(id_encomienda).execute()

                                    withContext(Dispatchers.Main){
                                        if (response.isSuccessful && response.body() != null){
                                            ultimoEvento = response.body()!!
                                        } else {
                                            errorMessage = if (response.code() == 404) "Encomienda no encontrada" else "Error del servidor: ${response.code()}"
                                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        errorMessage = when (e) {
                                            is SocketTimeoutException -> "Tiempo de espera agotado"
                                            is UnknownHostException -> "No se puede conectar al servidor. Verifica tu conexión."
                                            else -> "Error de conexión: ${e.localizedMessage}"
                                        }
                                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                            Text(
                                text = "Ubicación Actual:",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            (ultimoEvento?.lugar_actual ?: it.ciudad_origen)?.let { it1 ->
                                Text(
                                    text = it1,
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Detalles de la encomienda
                    Box(
                        modifier = Modifier
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .padding(16.dp)
                            .fillMaxWidth(0.9f)
                    ) {
                        Column {
                            Text("Descripción:", fontWeight = FontWeight.Bold)
                            Text(it.descripcion ?: "Descripción no disponible")

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Remitente:", fontWeight = FontWeight.Bold)
                            if(it.nombre_emisor != null && it.apellido_emisor != null && it.dni_emisor != null){
                                Text("${it.nombre_emisor} ${it.apellido_emisor}")
                                Text("DNI: ${it.dni_emisor}")
                            } else{
                                Text(it.razon_social_emisor ?: "Emisor no disponible")
                                Text("RUC: ${it.ruc_emisor ?: "Identificación no disponible"}")
                            }


                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Destinatario:", fontWeight = FontWeight.Bold)
                            if(it.nombre_receptor != null && it.apellido_receptor != null && it.dni_receptor != null){
                                Text("${it.nombre_receptor} ${it.apellido_receptor}")
                                Text("DNI: ${it.dni_receptor}")
                            } else{
                                Text(it.razon_social_receptor ?: "Receptor no disponible")
                                Text("RUC: ${it.ruc_receptor ?: "Identificación no disponible"}")
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Origen:", fontWeight = FontWeight.Bold)
                            Text(it.ciudad_origen ?: "Origen no disponible")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Destino:", fontWeight = FontWeight.Bold)
                            Text(it.ciudad_destino ?: "Destino no disponible")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Fecha de Envío:", fontWeight = FontWeight.Bold)
                            Text(it.fecha_envio ?: "Fecha de envío no disponible")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Fecha Entrega:", fontWeight = FontWeight.Bold)
                            Text(it.fecha_entrega ?: "Fecha de entrega no disponible")
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botón Ver Historial
                    Button(
                        onClick = {
                            scope.launch { scaffoldState.bottomSheetState.expand() }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400)),
                        enabled = it.estado != "Pendiente"
                    ) {
                        Text(text = "Ver Historial", color = Color.White)
                    }
                } ?: run {
                    Text(text = "Cargando detalles de la encomienda...", fontSize = 16.sp, color = Color.Gray)
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HistoryModalContent(id: Int) {
    val encomiendaService = ApiClient.retrofit.create(EncomiendaService::class.java)
    var historialEncomienda by remember { mutableStateOf<List<HistorialEncomienda>>(listOf()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(4.dp, Color(0xFF006400), shape = RoundedCornerShape(8.dp))
    ) {
        // Encabezado con fondo verde
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF006400))
                .padding(16.dp)
        ) {
            Text(
                text = "Historial",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Fondo blanco para el área de logs
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Lista de eventos del historial
            val scope = rememberCoroutineScope()
            scope.launch(Dispatchers.IO){
                try{
                    val response = encomiendaService.listarHistorialPorEncomiendaId(id).execute()

                    withContext(Dispatchers.Main) {
                        if(response.isSuccessful && response.body() != null) {
                            historialEncomienda = response.body()!!
                        }
                    }
                } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                errorMessage = when (e) {
                    is SocketTimeoutException -> "Tiempo de espera agotado"
                    is UnknownHostException -> "No se puede conectar al servidor. Verifica tu conexión."
                    else -> "Error de conexión: ${e.localizedMessage}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
            }
            LazyColumn {
                items(historialEncomienda){ evento ->
                    if(evento == historialEncomienda.first()){
                        HistoryEvent(evento, isCurrent = true)
                    }
                    else{
                        HistoryEvent(evento)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryEvent(historialEncomienda: HistorialEncomienda, isCurrent: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            painter = painterResource(id = if (isCurrent) R.drawable.current_status_icon else R.drawable.past_status_icon),
            contentDescription = null,
            tint = if (isCurrent) Color.Green else Color.Gray,
            modifier = Modifier.size(10.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            historialEncomienda.descripcion_evento?.let { Text(text = it, color = if (isCurrent) Color.Black else Color.Gray) }
            historialEncomienda.fecha_evento?.let { Text(text = it, fontSize = 12.sp, color = Color.LightGray) }
        }
    }
}

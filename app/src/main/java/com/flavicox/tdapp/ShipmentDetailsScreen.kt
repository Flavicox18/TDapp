@file:OptIn(ExperimentalMaterial3Api::class)

package com.flavicox.tdapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flavicox.tdapp.entity.Encomienda
import com.flavicox.tdapp.service.EncomiendaService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ShipmentDetailsScreen(id_encomienda: Int) {
    val context = LocalContext.current
    val encomiendaService = ApiClient.retrofit.create(EncomiendaService::class.java)
    var encomienda by remember { mutableStateOf<Encomienda?>(null) }
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

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
        sheetContent = { HistoryModalContent() },
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                            modifier = Modifier
                                .background(Color(0xFFFFF9C4), shape = RoundedCornerShape(8.dp))
                                .padding(horizontal = 16.dp, vertical = 8.dp),
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
                        Column {
                            Text(
                                text = "Ubicación Actual:",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Text(
                                text = "Camino a la entrega",
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
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
                            Text("${it.nombre_emisor ?: "Nombre no disponible"} ${it.apellido_emisor ?: ""}")
                            Text("DNI: ${it.dni_emisor ?: "DNI no disponible"}")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Destinatario:", fontWeight = FontWeight.Bold)
                            Text("${it.nombre_receptor ?: "Nombre no disponible"} ${it.apellido_receptor ?: ""}")
                            Text("DNI: ${it.dni_receptor ?: "DNI no disponible"}")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Origen:", fontWeight = FontWeight.Bold)
                            Text(it.ciudad_origen ?: "Origen no disponible")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Destino:", fontWeight = FontWeight.Bold)
                            Text(it.ciudad_destino ?: "Destino no disponible")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Fecha de Envío:", fontWeight = FontWeight.Bold)
                            Text(it.fecha_envio?.toString() ?: "Fecha de envío no disponible")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Fecha Entrega:", fontWeight = FontWeight.Bold)
                            Text(it.fecha_entrega?.toString() ?: "Fecha de entrega no disponible")
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botón Ver Historial
                    Button(
                        onClick = {
                            scope.launch { scaffoldState.bottomSheetState.expand() }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400))
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

@Composable
fun HistoryModalContent() {
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
            HistoryEvent("La encomienda fue entregada.", "2 de Noviembre 2024", isCurrent = true)
            HistoryEvent("La encomienda salió a reparto", "2 de Noviembre 2024")
            HistoryEvent("La encomienda llegó a Trujillo", "2 de Noviembre 2024")
            HistoryEvent("Salió con destino Trujillo", "2 de Noviembre 2024")
            HistoryEvent("La encomienda llegó a Lima", "2 de Noviembre 2024")
        }
    }
}

@Composable
fun HistoryEvent(description: String, date: String, isCurrent: Boolean = false) {
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
            Text(text = description, color = if (isCurrent) Color.Black else Color.Gray)
            Text(text = date, fontSize = 12.sp, color = Color.LightGray)
        }
    }
}

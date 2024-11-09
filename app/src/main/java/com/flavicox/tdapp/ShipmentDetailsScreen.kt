@file:OptIn(ExperimentalMaterial3Api::class)

package com.flavicox.tdapp

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShipmentDetailsScreen() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            HistoryModalContent()
        },
        sheetPeekHeight = 0.dp, // Oculta el modal hasta que se expanda
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

                // Envío Código y Estado en la misma fila
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
                            text = "ABC12345",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Estado con ícono
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
                                text = "En tránsito",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.shipping),
                                contentDescription = "Ícono de estado",
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
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
                        Text("abcd abcd abcd abcd abcd abcd abcd abcd abcd abcd")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Remitente:", fontWeight = FontWeight.Bold)
                        Text("Flavio Villacorta Cordova")
                        Text("DNI: 12345678")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Destinatario:", fontWeight = FontWeight.Bold)
                        Text("Frank Cortez Bayona")
                        Text("DNI: 87654321")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Origen: ", fontWeight = FontWeight.Bold)
                        Text("Trujillo")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Destino: ", fontWeight = FontWeight.Bold)
                        Text("Lima")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Fecha de Envío:", fontWeight = FontWeight.Bold)
                        Text("07/11/2024")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Fecha Entrega*:", fontWeight = FontWeight.Bold)
                        Text("08/11/2024")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Cantidad de Paquetes:", fontWeight = FontWeight.Bold)
                        Text("10")
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

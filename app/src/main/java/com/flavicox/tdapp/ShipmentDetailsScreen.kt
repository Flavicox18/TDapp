package com.flavicox.tdapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShipmentDetailsScreen() {
    var showModal by remember { mutableStateOf(false) }

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
                            painter = painterResource(id = R.drawable.shipping), // Cambia a tu ícono real
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
                        text = "Entregado en el destino",
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
                    .fillMaxWidth(0.9f) // Ajuste para no ocupar todo el ancho
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
                    Text("17/10/2024")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Fecha Entrega*:", fontWeight = FontWeight.Bold)
                    Text("18/10/2024")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Cantidad de Paquetes:", fontWeight = FontWeight.Bold)
                    Text("10")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón Ver Historial
            Button(
                onClick = { showModal = true }, // Mostrar el modal al hacer clic
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400))
            ) {
                Text(text = "Ver Historial", color = Color.White)
            }
        }

        // Modal simulado
        if (showModal) {
            // Fondo oscuro limitado a la parte superior
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { showModal = false } // Cerrar el modal al hacer clic en el fondo,
                    .padding(bottom = 400.dp) // Limita el fondo oscuro al área superior
            ) {}

            // Contenido del modal en la parte inferior
            AnimatedVisibility(
                visible = showModal,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter) // Mantiene el modal pegado al fondo
                        .background(Color.White, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .padding(16.dp)
                        .height(400.dp)
                ) {
                    HistoryModalContent(onClose = { showModal = false })
                }
            }
        }
    }
}

@Composable
fun HistoryModalContent(onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(16.dp)
            .heightIn(max = 400.dp)
            .verticalScroll(rememberScrollState()), // Scroll para el contenido del historial
        verticalArrangement = Arrangement.Bottom // Alineación del contenido desde abajo hacia arriba
    ) {
        // Encabezado con botón de cierre
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF006400), shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Historial",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            IconButton(
                onClick = onClose,
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
            ) {
                Text(text = "X", color = Color.White, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de logs con estilo de línea de tiempo
        val logs = listOf(
            "La encomienda fue entregada.",
            "La encomienda salió a reparto.",
            "La encomienda llegó a Trujillo.",
            "Salió con destino Trujillo.",
            "La encomienda llegó a Lima."
        )

        Column(modifier = Modifier.padding(start = 16.dp)) {
            logs.forEachIndexed { index, log ->
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    // Indicador de color y línea de tiempo
                    Column(
                        modifier = Modifier.width(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    color = if (index == logs.size - 1) Color(0xFF006400) else Color.Gray,
                                    shape = CircleShape
                                )
                        )
                        if (index < logs.size - 1) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(24.dp)
                                    .background(Color.Gray)
                            )
                        }
                    }
                    // Texto del log
                    Column {
                        Text(
                            text = log,
                            color = if (index == logs.size - 1) Color(0xFF006400) else Color.Black,
                            fontWeight = if (index == logs.size - 1) FontWeight.Bold else FontWeight.Normal
                        )
                        Text(
                            text = "2 de Noviembre 2024",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}


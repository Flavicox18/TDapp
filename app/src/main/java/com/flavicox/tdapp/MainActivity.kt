package com.flavicox.tdapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flavicox.tdapp.ui.theme.TDappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TDappTheme {
                SearchPackageScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPackageScreen() {
    var query by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) } // Estado para controlar la búsqueda

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
                placeholder = { Text("Codigo", fontSize = 14.sp, lineHeight = 15.sp) },
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
                modifier = Modifier.size(160.dp)
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TDappTheme {
        SearchPackageScreen()
    }
}

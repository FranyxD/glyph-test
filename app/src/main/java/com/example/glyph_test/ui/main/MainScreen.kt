package com.example.glyph_test.ui.main

// ============================================================
// IMPORTS
// ============================================================

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.glyph_test.ui.theme.GlyphtestTheme

// ============================================================
// MAIN SCREEN (INTERFAZ GRÁFICA EN COMPOSE CON POP-UP DE ICONO)
// ============================================================
//
// Esta pantalla ofrece:
//   1. Botón "Mostrar Sol" (Carga la imagen `sol.png`).
//   2. Botón "Elegir Icono" (Abre un Pop-up / AlertDialog para introducir un icono/emoji).
//   3. Botón "Adjuntar foto" (Selecciona imagen de galería).
//   4. Botón "Apagar Matrix".
//   5. Vista previa de la foto/icono activo en pantalla.
//
@Composable
fun MainScreen(
    patronActualNombre: String,
    imagenSeleccionada: Bitmap?,
    onMostrarSolClick: () -> Unit,
    onConfirmarIconoClick: (String) -> Unit,
    onAdjuntarFotoClick: () -> Unit,
    onApagarClick: () -> Unit
) {
    // Estado para controlar la visibilidad del Pop-up de selección de icono
    var mostrarDialogoIcono by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValores ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValores)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ================================================
            // TÍTULO Y SUBTÍTULO
            // ================================================
            Text(
                text = "Nothing Phone (3)",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Controlador de Glyph Matrix",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Indicador del estado activo
            Text(
                text = "Patrón activo: $patronActualNombre",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ================================================
            // BOTÓN 1: MOSTRAR SOL (DIBUJO DE SOL.PNG)
            // ================================================
            Button(
                onClick = onMostrarSolClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "☀️ Mostrar Sol (sol.png)", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ================================================
            // BOTÓN 2: ELEGIR ICONO (ABRE POP-UP)
            // ================================================
            Button(
                onClick = { mostrarDialogoIcono = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "✨ Elegir Icono (Pop-up)", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ================================================
            // BOTÓN 3: ADJUNTAR FOTO
            // ================================================
            Button(
                onClick = onAdjuntarFotoClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "📷 Adjuntar foto", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ================================================
            // BOTÓN 4: APAGAR MATRIZ
            // ================================================
            OutlinedButton(
                onClick = onApagarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "🌙 Apagar Matrix", fontSize = 16.sp)
            }

            // ================================================
            // PREVISUALIZACIÓN EN PANTALLA
            // ================================================
            imagenSeleccionada?.let { bitmap ->
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Vista previa en Glyph:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Vista previa de la imagen",
                    modifier = Modifier.size(140.dp)
                )
            }
        }
    }

    // ============================================================
    // POP-UP / DIÁLOGO PARA ELEGIR UN ICONO
    // ============================================================
    if (mostrarDialogoIcono) {
        DialogoElegirIcono(
            onDismiss = { mostrarDialogoIcono = false },
            onConfirmar = { iconoSeleccionado ->
                mostrarDialogoIcono = false
                onConfirmarIconoClick(iconoSeleccionado)
            }
        )
    }
}

// ============================================================
// COMPOSABLE: DIÁLOGO / POP-UP PARA SELECCIONAR ICONO
// ============================================================
@Composable
fun DialogoElegirIcono(
    onDismiss: () -> Unit,
    onConfirmar: (String) -> Unit
) {
    var textoIcono by remember { mutableStateOf("⚡") }

    // Lista de iconos / emojis predefinidos de selección rápida
    val opcionesIconos = listOf("⚡", "🔥", "⭐", "🚀", "💀", "🎯", "🔔", "⚠️", "👑", "❤️")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Elegir icono para Glyph Matrix",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "Selecciona un icono o escribe el emoji/símbolo que quieras mostrar:",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Cuadrícula de iconos predefinidos
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier.height(110.dp)
                ) {
                    items(opcionesIconos) { icono ->
                        Card(
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable { textoIcono = icono },
                            colors = CardDefaults.cardColors(
                                containerColor = if (textoIcono == icono)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(
                                text = icono,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de texto personalizado
                OutlinedTextField(
                    value = textoIcono,
                    onValueChange = { nuevoTexto ->
                        // Limitamos a 2 caracteres máximo para mantener legibilidad
                        if (nuevoTexto.length <= 2) {
                            textoIcono = nuevoTexto
                        }
                    },
                    label = { Text("Escribir emoji o símbolo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (textoIcono.isNotBlank()) {
                        onConfirmar(textoIcono)
                    }
                }
            ) {
                Text("Aplicar en Glyph")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

// ============================================================
// PREVIEW
// ============================================================
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    GlyphtestTheme {
        MainScreen(
            patronActualNombre = "Sol (sol.png)",
            imagenSeleccionada = null,
            onMostrarSolClick = {},
            onConfirmarIconoClick = {},
            onAdjuntarFotoClick = {},
            onApagarClick = {}
        )
    }
}

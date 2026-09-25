package com.example.glyph_test

// ============================================================
// IMPORTS
// ============================================================

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.glyph_test.glyph.GlyphController
import com.example.glyph_test.ui.main.MainScreen
import com.example.glyph_test.ui.theme.GlyphtestTheme

// ============================================================
// MAIN ACTIVITY (PUNTO DE ENTRADA DE LA APLICACIÓN)
// ============================================================
//
// Esta es la pantalla principal de nuestra aplicación.
//
// En Angular podríamos pensar en ella como un Component principal / Bootstrap.
//
// Coordina:
//   1. El controlador del Glyph (GlyphController).
//   2. El selector de imágenes de Android (PickVisualMedia).
//   3. La carga de la foto `sol.png` desde los recursos del proyecto.
//   4. La generación de iconos/emojis mediante un Pop-up.
//   5. La pantalla construida con Jetpack Compose (MainScreen).
//
class MainActivity : ComponentActivity() {

    // ========================================================
    // GLYPH CONTROLLER
    // ========================================================
    //
    // Este objeto nos permite comunicarnos con la Glyph Matrix.
    //
    private lateinit var glyphController: GlyphController

    // ========================================================
    // ESTADOS REACTIVOS
    // ========================================================
    //
    // Guardan el nombre del patrón activo y la foto/icono activo.
    // Compose actualizará la pantalla automáticamente al cambiar su valor.
    //
    private var patronActualNombre by mutableStateOf("Conectando...")
    private var imagenSeleccionada: Bitmap? by mutableStateOf(null)

    // ========================================================
    // SELECTOR DE FOTOS DE GALERÍA
    // ========================================================
    //
    private val selectorDeFotos =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri != null) {
                // El usuario ha elegido una foto, procedemos a cargarla
                cargarImagen(uri)
            }
        }

    // ========================================================
    // ON CREATE
    // ========================================================
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilitar modo pantalla completa en la interfaz
        enableEdgeToEdge()

        // Inicializar el controlador del Glyph
        glyphController = GlyphController(this)

        // Conectar con el servicio Glyph de Nothing
        glyphController.conectar {
            // Al conectar con éxito, cargamos y mostramos sol.png por defecto
            mostrarSolPng()
        }

        // Crear la interfaz con Jetpack Compose
        setContent {
            GlyphtestTheme {
                MainScreen(
                    patronActualNombre = patronActualNombre,
                    imagenSeleccionada = imagenSeleccionada,
                    onMostrarSolClick = {
                        mostrarSolPng()
                    },
                    onConfirmarIconoClick = { iconoTexto ->
                        mostrarIconoEnGlyph(iconoTexto)
                    },
                    onAdjuntarFotoClick = {
                        seleccionarFoto()
                    },
                    onApagarClick = {
                        glyphController.apagar()
                        imagenSeleccionada = null
                        patronActualNombre = "Apagado"
                    }
                )
            }
        }
    }

    // ========================================================
    // CARGAR Y MOSTRAR SOL.PNG
    // ========================================================
    //
    // Carga la imagen `sol.png` almacenada en res/drawable/sol.png
    // y la envía a la Glyph Matrix a brillo máximo.
    //
    private fun mostrarSolPng() {
        val bitmapSol = BitmapFactory.decodeResource(resources, R.drawable.sol)
        if (bitmapSol != null) {
            imagenSeleccionada = bitmapSol
            patronActualNombre = "Sol (sol.png)"
            glyphController.enviarImagenBitmap(bitmapSol)
        }
    }

    // ========================================================
    // MOSTRAR ICONO / EMOJI GENERADO DESDE POP-UP
    // ========================================================
    //
    // Genera un Bitmap de 25x25 con el icono/emoji proporcionado
    // y lo envía a la Glyph Matrix.
    //
    private fun mostrarIconoEnGlyph(iconoTexto: String) {
        val bitmapIcono = glyphController.crearBitmapDesdeIcono(iconoTexto)
        imagenSeleccionada = bitmapIcono
        patronActualNombre = "Icono ($iconoTexto)"
        glyphController.enviarImagenBitmap(bitmapIcono)
    }

    // ========================================================
    // SELECCIONAR FOTO DE GALERÍA
    // ========================================================
    //
    private fun seleccionarFoto() {
        selectorDeFotos.launch(
            PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    }

    // ========================================================
    // CARGAR IMAGEN DESDE URI (GALERÍA)
    // ========================================================
    //
    private fun cargarImagen(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = inputStream?.use { stream ->
            BitmapFactory.decodeStream(stream)
        }

        if (bitmap != null) {
            imagenSeleccionada = bitmap
            patronActualNombre = "Foto adjuntada"
            glyphController.enviarImagenBitmap(bitmap)
        }
    }

    // ========================================================
    // ON DESTROY (LIMPIEZA DE RECURSOS)
    // ========================================================
    //
    override fun onDestroy() {
        glyphController.apagarYDesconectar()
        super.onDestroy()
    }
}

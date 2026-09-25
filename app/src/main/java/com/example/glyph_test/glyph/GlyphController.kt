package com.example.glyph_test.glyph

// ============================================================
// IMPORTS
// ============================================================

import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.nothing.ketchum.Common
import com.nothing.ketchum.Glyph
import com.nothing.ketchum.GlyphMatrixFrame
import com.nothing.ketchum.GlyphMatrixManager
import com.nothing.ketchum.GlyphMatrixObject

// ============================================================
// GLYPH CONTROLLER (CONTROLADOR DEL SDK DE NOTHING)
// ============================================================
//
// Esta clase encapsula TODA la lógica de comunicación con
// el SDK de Nothing para la Glyph Matrix.
//
// Soporta:
//   1. Enviar imágenes/fotos cargadas desde recursos o galería (Bitmaps).
//   2. Generar y enviar un icono/emoji dibujado dinámicamente en 25x25.
//   3. Enviar patrones de texto o matriz manual '0' y '1'.
//
// Equivalente en Web / Angular:
//   Un Service de Angular `@Injectable({ providedIn: 'root' })`
//   que maneja las peticiones HTTP o la comunicación con un hardware/API.
//
class GlyphController(private val context: Context) {

    // ========================================================
    // INSTANCIA DEL MANAGER DEL SDK
    // ========================================================
    //
    // Pedimos al SDK de Nothing el objeto encargado de
    // comunicarse con el servicio del sistema del teléfono.
    //
    private val glyphManager: GlyphMatrixManager =
        GlyphMatrixManager.getInstance(context.applicationContext)

    // Guarda el estado de si ya estamos conectados
    var estaConectado: Boolean = false
        private set

    // ========================================================
    // CONECTAR E INICIALIZAR EL SERVICIO
    // ========================================================
    //
    // Inicia la conexión con el servicio de Nothing Glyph.
    // Recibe una función callback (`onConectadoExito`) que se ejecutará
    // automáticamente en cuanto la conexión esté lista.
    //
    fun conectar(onConectadoExito: () -> Unit) {
        val callback = object : GlyphMatrixManager.Callback {

            override fun onServiceConnected(componentName: ComponentName) {
                // Indicamos a Nothing que queremos controlar un Phone (3)
                glyphManager.register(Glyph.DEVICE_23112)
                estaConectado = true

                // Notificamos que la conexión ha tenido éxito
                onConectadoExito()
            }

            override fun onServiceDisconnected(componentName: ComponentName) {
                estaConectado = false
            }
        }

        // Iniciamos el proceso de conexión
        glyphManager.init(callback)
    }

    // ========================================================
    // ENVIAR IMAGEN BITMAP (SOL.PNG O FOTO DE GALERÍA)
    // ========================================================
    //
    // Flujo:
    //   Foto (Bitmap) -> Redimensionar a 25x25 -> GlyphMatrixObject -> GlyphMatrixFrame -> Glyph
    //
    // Esta es la ruta que consigue el brillo máximo al mostrar fotos e imágenes.
    //
    fun enviarImagenBitmap(bitmapOriginal: Bitmap) {
        if (!estaConectado) return

        // 1. Tamaño de la matriz (25x25 para Phone 3)
        val tamanno = Common.getDeviceMatrixLength()

        // 2. Redimensionar la imagen a 25x25
        val bitmap25x25 = Bitmap.createScaledBitmap(
            bitmapOriginal,
            tamanno,
            tamanno,
            true
        )

        // 3. Crear el objeto de Nothing con brillo máximo (255)
        val objeto = GlyphMatrixObject.Builder()
            .setImageSource(bitmap25x25)
            .setBrightness(255)
            .build()

        // 4. Crear el Frame añadiendo el objeto a la capa superior (addTop)
        val frame = GlyphMatrixFrame.Builder()
            .addTop(objeto)
            .build(context)

        // 5. Enviar el Frame a la Glyph Matrix
        glyphManager.setAppMatrixFrame(frame)
    }

    // ========================================================
    // CREAR BITMAP DESDE ICONO O EMOJI
    // ========================================================
    //
    // Crea una imagen de 25x25 con un icono, emoji o símbolo dibujado
    // centrado y ocupando casi todo el tamaño de la matriz.
    //
    fun crearBitmapDesdeIcono(iconoTexto: String): Bitmap {
        val tamanno = Common.getDeviceMatrixLength() // 25
        val bitmap = Bitmap.createBitmap(tamanno, tamanno, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Fondo negro (LEDs apagados)
        canvas.drawColor(Color.BLACK)

        // Pintura en blanco (LEDs a brillo máximo)
        val paint = Paint().apply {
            color = Color.WHITE
            textSize = 20f // Ocupa prácticamente toda la matriz de 25x25
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        // Centrar vertical y horizontalmente el icono/emoji
        val x = tamanno / 2f
        val y = (tamanno / 2f) - ((paint.descent() + paint.ascent()) / 2f)

        // Dibujar el icono en el lienzo de 25x25
        canvas.drawText(iconoTexto, x, y, paint)

        return bitmap
    }

    // ========================================================
    // ENVIAR UN PATRÓN DE DIBUJO MANUAL (LÍNEAS '0' Y '1')
    // ========================================================
    //
    fun enviarPatron(patronLineas: List<String>) {
        if (!estaConectado) return

        val tamanno = Common.getDeviceMatrixLength()
        val matriz = IntArray(tamanno * tamanno)

        for (fila in patronLineas.indices) {
            val linea = patronLineas[fila]
            for (columna in linea.indices) {
                val posicion = fila * tamanno + columna
                matriz[posicion] = if (linea[columna] == '1') 255 else 0
            }
        }

        glyphManager.setAppMatrixFrame(matriz)
    }

    // ========================================================
    // APAGAR LA MATRIZ
    // ========================================================
    //
    fun apagar() {
        if (estaConectado) {
            glyphManager.closeAppMatrix()
        }
    }

    // ========================================================
    // DESCONECTAR Y LIBERAR RECURSOS
    // ========================================================
    //
    fun apagarYDesconectar() {
        if (estaConectado) {
            glyphManager.closeAppMatrix()
            glyphManager.unInit()
            estaConectado = false
        }
    }
}

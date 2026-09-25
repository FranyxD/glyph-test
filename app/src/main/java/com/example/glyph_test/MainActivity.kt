// ============================================================
// PACKAGE
// ============================================================
//
// Indica a qué paquete pertenece nuestra clase.
//
// Es parecido a la organización de carpetas/módulos que puedes
// tener en un proyecto de Angular/TypeScript.
//
// NO necesitas modificar esto.
// ============================================================

package com.example.glyph_test


// ============================================================
// IMPORTS
// ============================================================
//
// Los imports nos permiten utilizar clases que están definidas
// en Android, Jetpack Compose y el SDK de Nothing.
//
// En TypeScript sería parecido a:
//
// import { X } from "..."
//
// ============================================================


// -------------------------
// Android
// -------------------------

// ComponentName lo necesitamos para el callback del servicio Glyph.
import android.content.ComponentName

// Bundle lo utiliza Android cuando crea nuestra Activity.
import android.os.Bundle


// -------------------------
// Bitmap
// -------------------------

// Bitmap representa una imagen en memoria.
// Lo utilizaremos para cargar y redimensionar nuestro PNG.
import android.graphics.Bitmap

// BitmapFactory permite convertir un recurso PNG/JPG/etc.
// en un objeto Bitmap.
import android.graphics.BitmapFactory


// -------------------------
// Android Activity
// -------------------------

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge


// -------------------------
// Jetpack Compose
// -------------------------

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


// -------------------------
// Tema de nuestra aplicación
// -------------------------

import com.example.glyph_test.ui.theme.GlyphtestTheme


// ============================================================
// IMPORTS DEL GLYPH MATRIX
// ============================================================
//
// Estas clases vienen del archivo:
//
// glyph-matrix-sdk-2.0.aar
//
// que hemos colocado anteriormente en:
//
// app/libs/
//
// Gracias a estos imports podemos utilizar la API de Nothing.
// ============================================================

import com.nothing.ketchum.Common
import com.nothing.ketchum.Glyph
import com.nothing.ketchum.GlyphMatrixManager
import com.nothing.ketchum.GlyphMatrixFrame
import com.nothing.ketchum.GlyphMatrixObject


// ============================================================
// MAIN ACTIVITY
// ============================================================
//
// Una Activity es básicamente una pantalla de Android.
//
// Si vienes de Angular:
//
// Angular → Component
// Android → Activity
//
// Nuestra MainActivity es la pantalla principal de la app.
// ============================================================

class MainActivity : ComponentActivity() {


    // ========================================================
    // GLYPH MANAGER
    // ========================================================
    //
    // glyphManager será nuestro "mando" para comunicarnos
    // con el sistema Glyph del Nothing Phone.
    //
    // "lateinit" significa:
    //
    // "Todavía no tengo el valor, pero prometo que lo
    // inicializaré antes de utilizarlo."
    //
    // En TypeScript sería conceptualmente parecido a declarar
    // una propiedad que inicializaremos posteriormente.
    // ========================================================

    private lateinit var glyphManager: GlyphMatrixManager


    // ========================================================
    // ON CREATE
    // ========================================================
    //
    // Android llama automáticamente a onCreate() cuando
    // nuestra Activity se crea.
    //
    // Es lo más parecido a ngOnInit() de Angular,
    // aunque el ciclo de vida de Android es bastante más amplio.
    // ========================================================

    override fun onCreate(savedInstanceState: Bundle?) {

        // Llamamos al onCreate() de ComponentActivity.
        //
        // Como MainActivity hereda de ComponentActivity,
        // debemos dejar que la clase padre haga su trabajo.
        super.onCreate(savedInstanceState)


        // ====================================================
        // EDGE TO EDGE
        // ====================================================
        //
        // Permite que nuestra interfaz pueda utilizar
        // toda la pantalla.
        //
        // Esto NO tiene relación con Glyph Matrix.
        // Es simplemente una configuración de Android.
        // ====================================================

        enableEdgeToEdge()


        // ====================================================
        // INICIAR GLYPH
        // ====================================================
        //
        // Iniciamos la conexión con el servicio Glyph.
        //
        // IMPORTANTE:
        //
        // No llamamos a mostrarMatriz() directamente aquí.
        //
        // Primero tenemos que esperar a que Nothing nos diga
        // que el servicio Glyph está conectado.
        // ====================================================

        iniciarGlyph()


        // ====================================================
        // INTERFAZ DE NUESTRA APP
        // ====================================================
        //
        // setContent() pertenece a Jetpack Compose.
        //
        // Aquí definimos qué aparece en la pantalla de nuestra
        // aplicación.
        // ====================================================

        setContent {

            GlyphtestTheme {

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }


    // ========================================================
    // INICIAR GLYPH
    // ========================================================
    //
    // Esta función es nuestra.
    //
    // Kotlin permite crear nuestras propias funciones utilizando:
    //
    // private fun nombreFuncion()
    //
    // "private" significa que solamente podemos utilizar esta
    // función desde esta clase.
    // ========================================================

    private fun iniciarGlyph() {


        // ====================================================
        // OBTENER GLYPH MATRIX MANAGER
        // ====================================================
        //
        // Pedimos al SDK de Nothing el objeto que nos permitirá
        // comunicarnos con Glyph Matrix.
        //
        // applicationContext representa el contexto general
        // de nuestra aplicación.
        // ====================================================

        glyphManager =
            GlyphMatrixManager.getInstance(applicationContext)


        // ====================================================
        // CREAR CALLBACK
        // ====================================================
        //
        // Un callback es básicamente:
        //
        // "Cuando ocurra X, ejecuta este código."
        //
        // Aquí estamos esperando a que el servicio Glyph
        // termine de conectarse.
        // ====================================================

        val callback = object : GlyphMatrixManager.Callback {


            // =================================================
            // SERVICIO CONECTADO
            // =================================================
            //
            // Android/Nothing llamará automáticamente a esta
            // función cuando la conexión con el servicio Glyph
            // se haya realizado correctamente.
            // =================================================

            override fun onServiceConnected(
                componentName: ComponentName
            ) {


                // =============================================
                // REGISTRAR EL DISPOSITIVO
                // =============================================
                //
                // Le decimos al SDK:
                //
                // "Estoy trabajando con un Nothing Phone (3)".
                //
                // DEVICE_23112 es el identificador del Phone (3).
                //
                // Nothing documenta este identificador oficialmente.
                // =============================================

                glyphManager.register(
                    Glyph.DEVICE_23112
                )


                // =============================================
                // MOSTRAR LA IMAGEN
                // =============================================
                //
                // Ahora que estamos conectados y registrados,
                // ya podemos enviar contenido al Glyph Matrix.
                // =============================================

                mostrarMatriz()
            }


            // =================================================
            // SERVICIO DESCONECTADO
            // =================================================
            //
            // Esta función se ejecutaría si perdiéramos la
            // conexión con el servicio Glyph.
            //
            // De momento no necesitamos hacer nada aquí.
            // =================================================

            override fun onServiceDisconnected(
                componentName: ComponentName
            ) {

                // Más adelante podríamos implementar aquí
                // una reconexión o mostrar un mensaje de error.
            }
        }


        // ====================================================
        // INICIALIZAR SERVICIO GLYPH
        // ====================================================
        //
        // Le decimos al GlyphMatrixManager:
        //
        // "Empieza a conectarte."
        //
        // Cuando termine correctamente, se ejecutará:
        //
        // onServiceConnected()
        // ====================================================

        glyphManager.init(callback)
    }


    // ========================================================
    // MOSTRAR MATRIZ
    // ========================================================
    //
    // Esta función:
    //
    // 1. Carga nuestro PNG.
    // 2. Lo convierte a Bitmap.
    // 3. Lo redimensiona a 25 × 25.
    // 4. Crea un GlyphMatrixObject.
    // 5. Le pone brillo 255.
    // 6. Crea un GlyphMatrixFrame.
    // 7. Lo envía al teléfono.
    //
    // Esta es la parte importante del proyecto.
    // ========================================================

    private fun mostrarMatriz() {


        // ====================================================
        // 1. CARGAR PNG
        // ====================================================
        //
        // Nuestro archivo debe estar aquí:
        //
        // app/src/main/res/drawable/sol.png
        //
        // Android genera automáticamente:
        //
        // R.drawable.sol
        //
        // para poder acceder al archivo desde Kotlin.
        // ====================================================

        val bitmapOriginal = BitmapFactory.decodeResource(
            resources,
            R.drawable.sol
        )


        // ====================================================
        // 2. OBTENER TAMAÑO DE LA MATRIZ
        // ====================================================
        //
        // Nothing proporciona esta función para obtener
        // dinámicamente el tamaño de la matriz.
        //
        // En Phone (3):
        //
        // 25
        //
        // Por tanto:
        //
        // 25 × 25 = 625 LEDs
        //
        // No obstante, utilizamos la función para no tener
        // que escribir "25" por todo nuestro código.
        // ====================================================

        val tamaño = Common.getDeviceMatrixLength()


        // ====================================================
        // 3. REDIMENSIONAR IMAGEN
        // ====================================================
        //
        // El Glyph Matrix del Phone (3) es 25 × 25.
        //
        // Convertimos nuestro PNG a exactamente ese tamaño.
        //
        // El "true" indica que Android utiliza filtrado al
        // realizar el escalado.
        // ====================================================

        val bitmap = Bitmap.createScaledBitmap(
            bitmapOriginal,
            tamaño,
            tamaño,
            true
        )


        // ====================================================
        // 4. CREAR GLYPH MATRIX OBJECT
        // ====================================================
        //
        // GlyphMatrixObject representa un objeto que queremos
        // mostrar en la matriz.
        //
        // Puede contener una imagen, texto, posición,
        // escala, orientación, brillo, etc.
        // ====================================================

        val objeto = GlyphMatrixObject.Builder()


            // =================================================
            // IMAGEN
            // =================================================
            //
            // Le pasamos directamente nuestro Bitmap.
            //
            // El SDK de Nothing espera una imagen 1:1.
            //
            // Como ya la hemos convertido a 25 × 25,
            // cumplimos esa condición.
            // =================================================

            .setImageSource(bitmap)


            // =================================================
            // BRILLO
            // =================================================
            //
            // 0   = apagado
            // 255 = máximo
            //
            // Hemos comprobado físicamente en tu Phone (3)
            // que esta ruta consigue el brillo máximo.
            // =================================================

            .setBrightness(255)


            // =================================================
            // CONSTRUIR OBJETO
            // =================================================
            //
            // Builder se utiliza para configurar un objeto
            // paso a paso y finalmente crear el objeto real.
            // =================================================

            .build()


        // ====================================================
        // 5. CREAR GLYPH MATRIX FRAME
        // ====================================================
        //
        // Un Frame representa lo que queremos mostrar
        // en la matriz en un determinado momento.
        //
        // Podemos tener hasta tres objetos:
        //
        // TOP
        // MID
        // LOW
        //
        // De momento solo necesitamos uno.
        // ====================================================

        val frame = GlyphMatrixFrame.Builder()


            // =================================================
            // AÑADIR OBJETO
            // =================================================
            //
            // Ponemos nuestra imagen en la capa superior.
            // =================================================

            .addTop(objeto)


            // =================================================
            // CONSTRUIR FRAME
            // =================================================
            //
            // "this" hace referencia a nuestra MainActivity.
            //
            // Android necesita el Context para construir
            // correctamente el Frame.
            // =================================================

            .build(this)


        // ====================================================
        // 6. ENVIAR FRAME AL GLYPH MATRIX
        // ====================================================
        //
        // Utilizamos:
        //
        // setAppMatrixFrame()
        //
        // porque estamos controlando Glyph Matrix desde
        // nuestra aplicación normal.
        //
        // Nothing recomienda utilizar esta variante para
        // aplicaciones.
        // ====================================================

        glyphManager.setAppMatrixFrame(frame)
    }


    // ========================================================
    // ON DESTROY
    // ========================================================
    //
    // Android llama a onDestroy() cuando nuestra Activity
    // se destruye.
    //
    // Aquí debemos liberar los recursos que hemos utilizado.
    // ========================================================

    override fun onDestroy() {


        // ====================================================
        // APAGAR MATRIZ DE NUESTRA APP
        // ====================================================
        //
        // Le decimos al sistema que deje de mostrar nuestro
        // contenido en Glyph Matrix.
        //
        // Esto evita que nuestro dibujo se quede encendido
        // después de cerrar la aplicación.
        // ====================================================

        glyphManager.closeAppMatrix()


        // ====================================================
        // DESCONECTAR SERVICIO
        // ====================================================
        //
        // Ya no necesitamos comunicarnos con el servicio Glyph,
        // así que liberamos la conexión.
        // ====================================================

        glyphManager.unInit()


        // ====================================================
        // LLAMAR AL onDestroy() PADRE
        // ====================================================
        //
        // Igual que hicimos con super.onCreate(),
        // dejamos que ComponentActivity termine correctamente
        // su proceso de destrucción.
        // ====================================================

        super.onDestroy()
    }
}


// ============================================================
// COMPOSABLE GREETING
// ============================================================
//
// Esta parte pertenece a Jetpack Compose.
//
// @Composable indica que esta función puede utilizarse para
// construir parte de la interfaz gráfica.
// ============================================================

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier
) {

    // Mostramos un texto sencillo en la pantalla.
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


// ============================================================
// PREVIEW
// ============================================================
//
// @Preview permite que Android Studio muestre una vista previa
// de nuestra interfaz sin ejecutar la aplicación.
// ============================================================

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

    GlyphtestTheme {

        Greeting("Android")
    }
}
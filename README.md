# 📱 Glyph Matrix Test

Una aplicación Android Open Source desarrollada con **Kotlin**, **Jetpack Compose** y el **SDK 2.0 de Nothing** para controlar y mostrar contenido personalizado en la matriz de LEDs del **Nothing Phone (3)**.

---

## ✨ Características

* ☀️ **Imagen de Sol (`sol.png`)**: Carga de imagen desde recursos locales redimensionada a $25 \times 25$ LEDs con brillo máximo.
* ✨ **Pop-up de Iconos y Emojis**: Diálogo emergente interactivo para seleccionar o escribir cualquier emoji/símbolo, generado dinámicamente en un lienzo de $25 \times 25$.
* 📷 **Adjuntar foto de la galería**: Selector de fotos nativo (`PickVisualMedia`) para convertir y enviar cualquier imagen a la Glyph Matrix.
* 🖼️ **Vista previa en tiempo real**: La interfaz en Jetpack Compose muestra exactamente qué imagen o icono está activo en la matriz.
* 🏗️ **Arquitectura limpia (MVVM / SRP)**: Separación clara de responsabilidades entre la interfaz visual (`ui`), el controlador del hardware (`glyph`) y los datos (`data`).

---

## 🛠️ Requisitos e Instalación

### Requisitos de Hardware y Software
* **Dispositivo:** Nothing Phone (3) (o dispositivo Nothing con soporte para Glyph Matrix SDK 2.0).
* **Entorno:** Android Studio Ladybug / Meerkat o superior.
* **SDK Mínimo:** Android 15 (API 35).
* **SDK Objetivo:** Android 16 (API 37).

### Compilación y Ejecución
1. Clona el repositorio:
   ```bash
   git clone https://github.com/FranyxD/glyph-test.git
   ```
2. Abre el proyecto en **Android Studio**.
3. Sincroniza Gradle (`Sync Project with Gradle Files`).
4. Conecta tu Nothing Phone (3) con la depuración USB activada.
5. Haz clic en **Run** (`Shift + F10`).

---

## 📂 Estructura del Proyecto

```text
app/src/main/java/com/example/glyph_test/
│
├── data/
│   └── GlyphPatterns.kt        # Plantillas y datos de patrones en píxel art
│
├── glyph/
│   └── GlyphController.kt      # Servicio de comunicación con Nothing Glyph SDK 2.0
│
├── ui/
│   ├── main/
│   │   └── MainScreen.kt       # Pantalla e interfaz gráfica en Jetpack Compose (Pop-ups, botones)
│   └── theme/                  # Tema, colores y tipografía de Compose
│
└── MainActivity.kt             # Punto de entrada de la app y coordinador de selectores
```

---

## ⚙️ Integración del SDK de Nothing

El proyecto incluye la librería oficial `glyph-matrix-sdk-2.0.aar` localizada en `app/libs/`.

* Permiso requerido en `AndroidManifest.xml`:
  ```xml
  <uses-permission android:name="com.nothing.ketchum.permission.ENABLE"/>
  ```
* Identificador del Nothing Phone (3): `Glyph.DEVICE_23112`
* Tamaño de matriz para Phone (3): $25 \times 25$ LEDs ($625$ píxeles).

---

## 📄 Licencia

Este proyecto es Open Source bajo la licencia [MIT](LICENSE).

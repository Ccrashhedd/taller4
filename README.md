# Taller 4 - Android Studio

Aplicación en Kotlin para calcular suma, resta, multiplicación y división entre dos números, mostrando los resultados en texto y en un gráfico de barras con MPAndroidChart.

## Requisitos
- Android Studio
- SDK mínimo 24
- Conexión a internet en la primera sincronización de Gradle

## Librería usada
- MPAndroidChart `v3.1.0`

## Funcionalidades
- Entrada de dos números enteros o decimales.
- Validación de campos vacíos e inválidos.
- Validación para evitar división entre cero.
- Visualización numérica de las cuatro operaciones.
- Gráfico comparativo actualizado en cada cálculo.
- Botón para limpiar los campos y reiniciar la vista.

## Cómo ejecutar
1. Abrir la carpeta del proyecto en Android Studio.
2. Esperar la sincronización de Gradle.
3. Ejecutar en emulador o dispositivo físico.

## Estructura principal
- `MainActivity.kt`: lógica de interfaz, validación y gráfico.
- `Acciones.kt`: operaciones matemáticas.
- `activity_main.xml`: interfaz principal.

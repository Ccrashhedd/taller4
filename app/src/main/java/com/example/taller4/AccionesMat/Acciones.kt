package com.example.taller4.AccionesMat

data class ResultadosMatematicos(
    val suma: Double,
    val resta: Double,
    val multiplicacion: Double,
    val division: Double
)

class Acciones {

    fun sumar(a: Double, b: Double): Double = a + b

    fun restar(a: Double, b: Double): Double = a - b

    fun multiplicacion(a: Double, b: Double): Double = a * b

    fun division(a: Double, b: Double): Double = a / b

    fun calcularTodo(a: Double, b: Double): ResultadosMatematicos {
        return ResultadosMatematicos(
            suma = sumar(a, b),
            resta = restar(a, b),
            multiplicacion = multiplicacion(a, b),
            division = division(a, b)
        )
    }
}

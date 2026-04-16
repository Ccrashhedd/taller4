package com.example.taller4

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taller4.AccionesMat.Acciones
import com.example.taller4.AccionesMat.ResultadosMatematicos
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private lateinit var tilNumberA: TextInputLayout
    private lateinit var tilNumberB: TextInputLayout
    private lateinit var etNumberA: TextInputEditText
    private lateinit var etNumberB: TextInputEditText
    private lateinit var btnCalcular: MaterialButton
    private lateinit var btnLimpiar: MaterialButton

    private lateinit var tvSuma: TextView
    private lateinit var tvResta: TextView
    private lateinit var tvMultiplicacion: TextView
    private lateinit var tvDivision: TextView
    private lateinit var tvEstado: TextView

    private lateinit var barChart: BarChart

    private val acciones = Acciones()
    private val decimalFormat = DecimalFormat("#,##0.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        configurarGrafico()
        configurarEventos()
        mostrarResultadosIniciales()
    }

    private fun initViews() {
        tilNumberA = findViewById(R.id.tilNumberA)
        tilNumberB = findViewById(R.id.tilNumberB)
        etNumberA = findViewById(R.id.etNumberA)
        etNumberB = findViewById(R.id.etNumberB)
        btnCalcular = findViewById(R.id.btnCalcular)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        tvSuma = findViewById(R.id.tvSuma)
        tvResta = findViewById(R.id.tvResta)
        tvMultiplicacion = findViewById(R.id.tvMultiplicacion)
        tvDivision = findViewById(R.id.tvDivision)
        tvEstado = findViewById(R.id.tvEstado)

        barChart = findViewById(R.id.barChart)
    }

    private fun configurarEventos() {
        btnCalcular.setOnClickListener {
            calcularOperaciones()
        }

        btnLimpiar.setOnClickListener {
            limpiarFormulario()
        }

        etNumberA.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) tilNumberA.error = null
        }

        etNumberB.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) tilNumberB.error = null
        }
    }

    private fun calcularOperaciones() {
        val numeroA = validarNumero(tilNumberA, etNumberA, getString(R.string.error_numero_a)) ?: return
        val numeroB = validarNumero(tilNumberB, etNumberB, getString(R.string.error_numero_b)) ?: return

        if (numeroB == 0.0) {
            tilNumberB.error = getString(R.string.error_division_cero)
            Toast.makeText(this, getString(R.string.error_division_cero), Toast.LENGTH_SHORT).show()
            return
        }

        val resultados = acciones.calcularTodo(numeroA, numeroB)
        mostrarResultados(resultados)
        actualizarGrafico(resultados)
        tvEstado.text = getString(R.string.estado_actualizado)
    }

    private fun validarNumero(
        contenedor: TextInputLayout,
        campo: TextInputEditText,
        mensajeError: String
    ): Double? {
        val texto = campo.text?.toString()?.trim()?.replace(',', '.') ?: ""

        if (texto.isEmpty()) {
            contenedor.error = getString(R.string.error_campo_requerido)
            Toast.makeText(this, mensajeError, Toast.LENGTH_SHORT).show()
            return null
        }

        val valor = texto.toDoubleOrNull()
        if (valor == null) {
            contenedor.error = getString(R.string.error_numero_invalido)
            Toast.makeText(this, mensajeError, Toast.LENGTH_SHORT).show()
            return null
        }

        contenedor.error = null
        return valor
    }

    private fun mostrarResultados(resultados: ResultadosMatematicos) {
        tvSuma.text = formatear(resultados.suma)
        tvResta.text = formatear(resultados.resta)
        tvMultiplicacion.text = formatear(resultados.multiplicacion)
        tvDivision.text = formatear(resultados.division)
    }

    private fun mostrarResultadosIniciales() {
        val resultadosIniciales = ResultadosMatematicos(0.0, 0.0, 0.0, 0.0)
        mostrarResultados(resultadosIniciales)
        actualizarGrafico(resultadosIniciales)
        tvEstado.text = getString(R.string.estado_inicial)
    }

    private fun limpiarFormulario() {
        etNumberA.text?.clear()
        etNumberB.text?.clear()
        tilNumberA.error = null
        tilNumberB.error = null
        mostrarResultadosIniciales()
        etNumberA.requestFocus()
    }

    private fun formatear(valor: Double): String = decimalFormat.format(valor)

    private fun configurarGrafico() {
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setFitBars(true)
        barChart.setDrawGridBackground(false)
        barChart.setNoDataText(getString(R.string.grafico_sin_datos))
        barChart.axisRight.isEnabled = false
        barChart.setScaleEnabled(false)
        barChart.setPinchZoom(false)

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(
            listOf(
                getString(R.string.label_suma),
                getString(R.string.label_resta),
                getString(R.string.label_multiplicacion),
                getString(R.string.label_division)
            )
        )

        barChart.axisLeft.setDrawGridLines(true)
    }

    private fun actualizarGrafico(resultados: ResultadosMatematicos) {
        val valores = listOf(
            resultados.suma.toFloat(),
            resultados.resta.toFloat(),
            resultados.multiplicacion.toFloat(),
            resultados.division.toFloat()
        )

        val entradas = arrayListOf(
            BarEntry(0f, valores[0]),
            BarEntry(1f, valores[1]),
            BarEntry(2f, valores[2]),
            BarEntry(3f, valores[3])
        )

        val dataSet = BarDataSet(entradas, getString(R.string.grafico_leyenda)).apply {
            colors = listOf(
                getColor(R.color.result_green),
                getColor(R.color.result_blue),
                getColor(R.color.result_orange),
                getColor(R.color.result_purple)
            )
            valueTextSize = 12f
            valueFormatter = object : ValueFormatter() {
                override fun getBarLabel(barEntry: BarEntry?): String {
                    return barEntry?.y?.let { formatear(it.toDouble()) } ?: "0"
                }
            }
        }

        val data = BarData(dataSet).apply {
            barWidth = 0.56f
        }

        val minValue = valores.minOrNull() ?: 0f
        val maxValue = valores.maxOrNull() ?: 0f
        val margen = max(abs(minValue), abs(maxValue)) * 0.15f + 1f

        barChart.axisLeft.axisMinimum = minOf(0f, minValue - margen)
        barChart.axisLeft.axisMaximum = maxOf(0f, maxValue + margen)
        barChart.xAxis.axisMinimum = -0.5f
        barChart.xAxis.axisMaximum = 3.5f

        barChart.data = data
        barChart.animateY(600)
        barChart.invalidate()
    }
}

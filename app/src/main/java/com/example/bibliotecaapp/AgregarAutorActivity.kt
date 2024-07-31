package com.example.bibliotecaapp

import android.app.DatePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AgregarAutorActivity : AppCompatActivity() {
    private lateinit var etNombres: EditText
    private lateinit var etApellidos: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var spinnerPais: Spinner
    private lateinit var etAutorId: EditText // Campo para el ID del autor
    private lateinit var btnGuardarAutor: Button
    private lateinit var btnListarAutor: Button
    private lateinit var btnActualizarAutor: Button
    private lateinit var btnBorrarAutor: Button
    private lateinit var listaMensaje: TextView
    private lateinit var btnRegresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_autor)

        etNombres = findViewById(R.id.etNombres)
        etApellidos = findViewById(R.id.etApellidos)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        spinnerPais = findViewById(R.id.spinnerPais)
        etAutorId = findViewById(R.id.codigo) // Asegúrate de tener este campo en tu layout
        btnGuardarAutor = findViewById(R.id.btnGuardarAutor)
        btnListarAutor = findViewById(R.id.btnListarAutor)
        btnActualizarAutor = findViewById(R.id.btnActualizarAutor) // Asegúrate de tener este botón en tu layout
        btnBorrarAutor = findViewById(R.id.btnBorrarAutor) // Asegúrate de tener este botón en tu layout
        listaMensaje = findViewById(R.id.listaMensaje)
        btnRegresar = findViewById(R.id.btnRegresar)

        // Configurar el Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.array_paises,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPais.adapter = adapter

        // Configurar los botones
        btnGuardarAutor.setOnClickListener { guardarAutor() }
        btnListarAutor.setOnClickListener { listarAutores() }
        btnActualizarAutor.setOnClickListener { actualizarAutor() }
        btnBorrarAutor.setOnClickListener { borrarAutor() }

        // Configurar el botón para que termine la actividad cuando se presione
        btnRegresar.setOnClickListener {
            finish()  // Termina la actividad y regresa a la actividad anterior
        }

        // Configurar el EditText de fecha de nacimiento para mostrar el DatePickerDialog
        etFechaNacimiento.setOnClickListener { showDatePickerDialog() }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // El mes empieza desde 0, así que debemos sumar 1 para obtener el mes correcto
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                etFechaNacimiento.setText(selectedDate)
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun guardarAutor() {
        val nombres = etNombres.text.toString()
        val apellidos = etApellidos.text.toString()
        val fechaNacimiento = etFechaNacimiento.text.toString()
        val pais = spinnerPais.selectedItem.toString()  // Obtener el país seleccionado

        if (nombres.isEmpty() || apellidos.isEmpty() || fechaNacimiento.isEmpty() || pais.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (!validarTextoSoloLetras(nombres)) {
            mostrarMensajeError("Nombres debe contener solo letras")
            return
        }

        if (!validarTextoSoloLetras(apellidos)) {
            mostrarMensajeError("Apellidos debe contener solo letras")
            return
        }

        if (!validarFecha(fechaNacimiento)) {
            mostrarMensajeError("Fecha de nacimiento debe estar en formato dd/MM/yyyy")
            return
        }



        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("nombres", nombres)
            put("apellidos", apellidos)
            put("fecha_nacimiento", fechaNacimiento)
            put("pais", pais)
        }

        val newRowId = db.insert("autor", null, values)

        if (newRowId != -1L) {
            //aqui se aplica el campio de limpieza al moneto del ingreso sin problemas
            Toast.makeText(this, "Autor guardado exitosamente", Toast.LENGTH_SHORT).show()
            etNombres.text.clear()
            etApellidos.text.clear()
            etFechaNacimiento.text.clear()
            spinnerPais.setSelection(0)
            // No es necesario limpiar el campo del país, ya que ahora es un Spinner
        } else {
            Toast.makeText(this, "Error al guardar el autor", Toast.LENGTH_SHORT).show()
        }

        db.close()
    }

    private fun listarAutores() {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            "autor",
            arrayOf("autor_id", "nombres", "apellidos", "fecha_nacimiento", "pais"),
            null, null, null, null, null
        )

        val autores = StringBuilder()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("autor_id"))
                val nombres = getString(getColumnIndexOrThrow("nombres"))
                val apellidos = getString(getColumnIndexOrThrow("apellidos"))
                val fechaNacimiento = getString(getColumnIndexOrThrow("fecha_nacimiento"))
                val pais = getString(getColumnIndexOrThrow("pais"))

                autores.append("ID: $id\n")
                autores.append("Nombres: $nombres\n")
                autores.append("Apellidos: $apellidos\n")
                autores.append("Fecha de Nacimiento: $fechaNacimiento\n")
                autores.append("País: $pais\n\n")
            }
        }
        cursor.close()

        listaMensaje.text = autores.toString()
    }

    private fun actualizarAutor() {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val id = etAutorId.text.toString()
        val nombres = etNombres.text.toString()
        val apellidos = etApellidos.text.toString()
        val fechaNacimiento = etFechaNacimiento.text.toString()
        val pais = spinnerPais.selectedItem.toString()  // Obtener el país seleccionado

        if (id.isNotEmpty() && nombres.isNotEmpty() && apellidos.isNotEmpty() && fechaNacimiento.isNotEmpty() && pais.isNotEmpty()) {
            if (!validarTextoSoloLetras(nombres)) {
                mostrarMensajeError("Nombres debe contener solo letras")
                return
            }

            if (!validarTextoSoloLetras(apellidos)) {
                mostrarMensajeError("Apellidos debe contener solo letras")
                return
            }

            if (!validarFecha(fechaNacimiento)) {
                mostrarMensajeError("Fecha de nacimiento debe estar en formato dd/MM/yyyy")
                return
            }

            if (!validarTextoSoloLetras(pais)) {
                mostrarMensajeError("País debe contener solo letras")
                return
            }

            val values = ContentValues().apply {
                put("nombres", nombres)
                put("apellidos", apellidos)
                put("fecha_nacimiento", fechaNacimiento)
                put("pais", pais)
            }

            val rowsAffected = db.update(
                "autor",
                values,
                "autor_id = ?",
                arrayOf(id)
            )

            if (rowsAffected > 0) {
                Toast.makeText(this, "Autor actualizado correctamente.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al actualizar el autor.", Toast.LENGTH_SHORT).show()
            }

            db.close()
        } else {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun borrarAutor() {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val id = etAutorId.text.toString()

        if (id.isNotEmpty()) {
            val rowsDeleted = db.delete(
                "autor",
                "autor_id = ?",
                arrayOf(id)
            )

            if (rowsDeleted > 0) {
                Toast.makeText(this, "Autor eliminado correctamente.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al eliminar el autor.", Toast.LENGTH_SHORT).show()
            }

            db.close()
        } else {
            Toast.makeText(this, "Por favor, ingrese el ID del autor a eliminar.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarMensajeError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun validarTextoSoloLetras(texto: String): Boolean {
        return texto.matches(Regex("^[a-zA-Z]+$"))
    }

    private fun validarFecha(fecha: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(fecha)
            true
        } catch (e: Exception) {
            false
        }
    }
}

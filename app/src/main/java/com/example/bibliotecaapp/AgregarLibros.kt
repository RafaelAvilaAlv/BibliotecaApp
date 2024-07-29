package com.example.bibliotecaapp

//
//class AgregarLibros : AppCompatActivity() {
//
//    private lateinit var databaseHelper: DatabaseHelper
//    private lateinit var spinnerAutor: Spinner
//    private lateinit var spinnerGenero: Spinner
//    private lateinit var editTextTitulo: EditText
//
//    // Variable para almacenar la URI de la imagen seleccionada
//    private var imageUri: Uri? = null
//
//    // Registro del resultado de la selección de imagen
//    private val imagenSeleccionada =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            if (result.resultCode == RESULT_OK) {
//                result.data?.data?.let { uri ->
//                    imageUri = uri
//                    // Aquí puedes manejar la URI de la imagen si es necesario
//                }
//            }
//        }
//
//    private val requestPermissionsLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//            if (isGranted) {
//                // Permiso concedido
//            } else {
//                // Permiso denegado
//                Toast.makeText(this, "Permiso de almacenamiento requerido", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_agregar_libros)
//
//        // Inicializar el DatabaseHelper
//        databaseHelper = DatabaseHelper(this)
//
//        // Encontrar los views por ID
//        spinnerAutor = findViewById(R.id.spinnerAutor)
//        spinnerGenero = findViewById(R.id.spinnerGenero)
//        editTextTitulo = findViewById(R.id.editTextTitulo)
//
//        // Solicitar permisos
//        requestPermissionsLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
//
//
//        // Rellenar los spinners con datos
//        populateSpinners()
//
//        // Configurar el botón para seleccionar la imagen
//        val buttonSeleccionarImagen: Button = findViewById(R.id.buttonSeleccionarImagen)
//        buttonSeleccionarImagen.setOnClickListener {
//            abrirSelectorDeImagen()
//        }
//
//        // Configurar el botón de guardar
//        val buttonGuardar = findViewById<Button>(R.id.buttonGuardarLibro)
//        buttonGuardar.setOnClickListener {
//            guardarLibro()
//        }
//    }
//
//    private fun populateSpinners() {
//        // Obtener datos para los spinners desde la base de datos
//        val autores = mutableListOf("Escoge un autor")
//        autores.addAll(databaseHelper.getAllAutores())
//
//        val generos = mutableListOf("Escoge un género")
//        generos.addAll(databaseHelper.getAllGeneros())
//
//        // Configurar el adaptador para el spinner de autores
//        val autorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, autores)
//        autorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerAutor.adapter = autorAdapter
//
//        // Configurar el adaptador para el spinner de géneros
//        val generoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, generos)
//        generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerGenero.adapter = generoAdapter
//    }
//
//    private fun guardarLibro() {
//        val titulo = editTextTitulo.text.toString()
//        val autor = spinnerAutor.selectedItem.toString()
//        val genero = spinnerGenero.selectedItem.toString()
//
//        // Asegúrate de que los parámetros no sean vacíos
//        val autorId = if (autor.isNotEmpty() && autor != "Escoge un autor") databaseHelper.getAutorIdByName(autor) else null
//        val generoId = if (genero.isNotEmpty() && genero != "Escoge un género") databaseHelper.getGeneroIdByName(genero) else null
//
//        if (titulo.isNotEmpty() && autorId != null && generoId != null) {
//            val imagePath = saveImageToInternalStorage() // Guardar la imagen en almacenamiento interno
//            val libro = Libros(id_libro = 0, titulo = titulo, autor_id = autorId, genero_id = generoId, imagePath = imagePath)
//            databaseHelper.insertLibro(libro)
//
//            // Mostrar un mensaje de confirmación usando Toast
//            Toast.makeText(this, "Libro guardado exitosamente", Toast.LENGTH_SHORT).show()
//        } else {
//            // Mostrar un mensaje de error usando Toast
//            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun abrirSelectorDeImagen() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        if (intent.resolveActivity(packageManager) != null) {
//            imagenSeleccionada.launch(intent)
//        } else {
//            Toast.makeText(this, "No hay aplicación para seleccionar imágenes", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//    private fun saveImageToInternalStorage(): String? {
//        imageUri?.let { uri ->
//            val inputStream = contentResolver.openInputStream(uri)
//            val file = File(filesDir, "libro_imagen_${System.currentTimeMillis()}.jpg")
//            val outputStream = FileOutputStream(file)
//            inputStream?.copyTo(outputStream)
//            inputStream?.close()
//            outputStream.close()
//            return file.absolutePath
//        }
//        return null
//    }
//}
//
//
//
//class AgregarLibros : AppCompatActivity() {
//
//    private lateinit var databaseHelper: DatabaseHelper
//    private lateinit var spinnerAutor: Spinner
//    private lateinit var spinnerGenero: Spinner
//    private lateinit var editTextTitulo: EditText
//
//    private var imageUri: Uri? = null
//
//    private val imagenSeleccionada =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            if (result.resultCode == RESULT_OK) {
//                result.data?.data?.let { uri ->
//                    imageUri = uri
//                    // Aquí puedes manejar la URI de la imagen si es necesario
//                }
//            }
//        }
//
//    private val requestPermissionsLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//            if (isGranted) {
//                // Permiso concedido
//                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
//            } else {
//                // Permiso denegado
//                Toast.makeText(this, "Permiso de almacenamiento requerido", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_agregar_libros)
//
//        requestPermissionsLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
//
//        databaseHelper = DatabaseHelper(this)
//
//        spinnerAutor = findViewById(R.id.spinnerAutor)
//        spinnerGenero = findViewById(R.id.spinnerGenero)
//        editTextTitulo = findViewById(R.id.editTextTitulo)
//
//        populateSpinners()
//
//        val buttonSeleccionarImagen: Button = findViewById(R.id.buttonSeleccionarImagen)
//        buttonSeleccionarImagen.setOnClickListener {
//            abrirSelectorDeImagen()
//        }
//
//        val buttonGuardar = findViewById<Button>(R.id.buttonGuardarLibro)
//        buttonGuardar.setOnClickListener {
//            guardarLibro()
//        }
//    }
//
//    private fun populateSpinners() {
//        val autores = mutableListOf("Escoge un autor")
//        autores.addAll(databaseHelper.getAllAutores())
//
//        val generos = mutableListOf("Escoge un género")
//        generos.addAll(databaseHelper.getAllGeneros())
//
//        val autorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, autores)
//        autorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerAutor.adapter = autorAdapter
//
//        val generoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, generos)
//        generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerGenero.adapter = generoAdapter
//    }
//
//    private fun guardarLibro() {
//        val titulo = editTextTitulo.text.toString()
//        val autor = spinnerAutor.selectedItem.toString()
//        val genero = spinnerGenero.selectedItem.toString()
//
//        val autorId = if (autor != "Escoge un autor") databaseHelper.getAutorIdByName(autor) else null
//        val generoId = if (genero != "Escoge un género") databaseHelper.getGeneroIdByName(genero) else null
//
//        if (titulo.isNotEmpty() && autorId != null && generoId != null) {
//            val imagePath = saveImageToInternalStorage()
//            val libro = Libros(id_libro = 0, titulo = titulo, autor_id = autorId, genero_id = generoId, imagePath = imagePath)
//            databaseHelper.insertLibro(libro)
//
//            Toast.makeText(this, "Libro guardado exitosamente", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun abrirSelectorDeImagen() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        if (intent.resolveActivity(packageManager) != null) {
//            imagenSeleccionada.launch(intent)
//        } else {
//            Toast.makeText(this, "No hay aplicación para seleccionar imágenes", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun saveImageToInternalStorage(): String? {
//        imageUri?.let { uri ->
//            try {
//                val inputStream = contentResolver.openInputStream(uri)
//                val file = File(filesDir, "libro_imagen_${System.currentTimeMillis()}.jpg")
//                FileOutputStream(file).use { output ->
//                    inputStream?.use { input ->
//                        input.copyTo(output)
//                    }
//                }
//                return file.absolutePath
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
//            }
//        }
//        return null
//    }
//
//
//}

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

class AgregarLibros : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var spinnerAutor: Spinner
    private lateinit var spinnerGenero: Spinner
    private lateinit var editTextTitulo: EditText

    // Variable para almacenar la URI de la imagen seleccionada
    private var imageUri: Uri? = null

    // Registro del resultado de la selección de imagen
    private val imagenSeleccionada =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    imageUri = uri
                    // Aquí puedes manejar la URI de la imagen si es necesario
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_libros)



        // Inicializar el DatabaseHelper
        databaseHelper = DatabaseHelper(this)

        // Encontrar los views por ID
        spinnerAutor = findViewById(R.id.spinnerAutor)
        spinnerGenero = findViewById(R.id.spinnerGenero)
        editTextTitulo = findViewById(R.id.editTextTitulo)

        // Rellenar los spinners con datos
        populateSpinners()

        // Configurar el botón para seleccionar la imagen
        val buttonSeleccionarImagen: Button = findViewById(R.id.buttonSeleccionarImagen)
        buttonSeleccionarImagen.setOnClickListener {
            abrirSelectorDeImagen()
        }

        // Configurar el botón de guardar
        val buttonGuardar = findViewById<Button>(R.id.buttonGuardarLibro)
        buttonGuardar.setOnClickListener {
            guardarLibro()
        }

        val buttonGenerarReporte = findViewById<Button>(R.id.buttonGenerarReporte)
        buttonGenerarReporte.setOnClickListener {
            val intent = Intent(this, ReporteLibrosActivity::class.java)
            startActivity(intent)
        }

        // Verificar y solicitar permisos de almacenamiento
        checkAndRequestStoragePermission()
    }

    private fun populateSpinners() {
        // Obtener datos para los spinners desde la base de datos
        val autores = mutableListOf("Escoge un autor")
        autores.addAll(databaseHelper.getAllAutores())

        val generos = mutableListOf("Escoge un género")
        generos.addAll(databaseHelper.getAllGeneros())

        // Configurar el adaptador para el spinner de autores
        val autorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, autores)
        autorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAutor.adapter = autorAdapter

        // Configurar el adaptador para el spinner de géneros
        val generoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, generos)
        generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenero.adapter = generoAdapter
    }

    private fun guardarLibro() {
        val titulo = editTextTitulo.text.toString()
        val autor = spinnerAutor.selectedItem.toString()
        val genero = spinnerGenero.selectedItem.toString()

        // Asegúrate de que los parámetros no sean vacíos
        val autorId = if (autor.isNotEmpty() && autor != "Escoge un autor") databaseHelper.getAutorIdByName(autor) else null
        val generoId = if (genero.isNotEmpty() && genero != "Escoge un género") databaseHelper.getGeneroIdByName(genero) else null

        if (titulo.isNotEmpty() && autorId != null && generoId != null) {
            val imagePath = saveImageToInternalStorage() // Guardar la imagen en almacenamiento interno
            val libro = Libros(id_libro = 0, titulo = titulo, autor_id = autorId, genero_id = generoId, imagePath = imagePath)
            databaseHelper.insertLibro(libro)

            // Mostrar un mensaje de confirmación usando Toast
            Toast.makeText(this, "Libro guardado exitosamente", Toast.LENGTH_SHORT).show()
        } else {
            // Mostrar un mensaje de error usando Toast
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun abrirSelectorDeImagen() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagenSeleccionada.launch(intent)
    }

    private fun saveImageToInternalStorage(): String? {
        imageUri?.let { uri ->
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(filesDir, "libro_imagen_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return file.absolutePath
        }
        return null
    }

    private fun checkAndRequestStoragePermission() {
        val readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val permissionsToRequest = mutableListOf<String>()

        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), REQUEST_CODE_STORAGE_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso concedido
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
                } else {
                    // Permiso denegado
                    Toast.makeText(this, "Permiso de almacenamiento requerido", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_STORAGE_PERMISSION = 100
    }
}



package com.example.bibliotecaapp

//import android.Manifest
//import android.content.ContentValues
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.database.sqlite.SQLiteDatabase
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.activity.result.ActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import java.io.File
//import java.io.FileOutputStream
//
//class RegisterActivity : AppCompatActivity() {
//    private lateinit var etNombre: EditText
//    private lateinit var etApellido: EditText
//    private lateinit var etCedula: EditText
//    private lateinit var etCorreo: EditText
//    private lateinit var etDireccion: EditText
//    private lateinit var ettelefono: EditText
//    private lateinit var etUsuario: EditText
//    private lateinit var etClave: EditText
//    private lateinit var btnRegistrar: Button
//    private lateinit var btnRegresar: Button
//    private lateinit var btnSelectImage: Button
//    private lateinit var imageView: ImageView
//
//    private var imageUri: Uri? = null
//
//    // Registro del resultado de la selección de imagen
//    private val imagenSeleccionada =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            if (result.resultCode == RESULT_OK) {
//                result.data?.data?.let { uri ->
//                    imageUri = uri
//                    imageView.setImageURI(uri)
//                }
//            }
//        }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register)
//
//        etNombre = findViewById(R.id.etNombre)
//        etApellido = findViewById(R.id.etApellido)
//        etCedula = findViewById(R.id.etCedula)
//        etCorreo = findViewById(R.id.etCorreo)
//        etDireccion = findViewById(R.id.etDireccion)
//        ettelefono = findViewById(R.id.ettelefono)
//        etUsuario = findViewById(R.id.etUsuario)
//        etClave = findViewById(R.id.etClave)
//        imageView = findViewById(R.id.imageView)
//
//        btnRegistrar = findViewById(R.id.btnRegistrar)
//        btnRegresar = findViewById(R.id.btnRegresar)
//        btnSelectImage = findViewById(R.id.btnSelectImage)
//
//        btnRegistrar.setOnClickListener { registrarUsuario() }
//        btnRegresar.setOnClickListener { regresar() }
//        btnSelectImage.setOnClickListener { selectImage() }
//
//        // Verificar y solicitar permisos de almacenamiento
//        checkAndRequestStoragePermission()
//    }
//
//    private fun selectImage() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        imagenSeleccionada.launch(intent)
//    }
//
//    private fun registrarUsuario() {
//        val nombre = etNombre.text.toString()
//        val apellido = etApellido.text.toString()
//        val cedula = etCedula.text.toString()
//        val correo = etCorreo.text.toString()
//        val direccion = etDireccion.text.toString()
//        val telefono = ettelefono.text.toString()
//        val usuario = etUsuario.text.toString()
//        val clave = etClave.text.toString()
//
//        if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty() || correo.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || usuario.isEmpty() || clave.isEmpty() || imageUri == null) {
//            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val dbHelper = DatabaseHelper(this)
//        val db = dbHelper.readableDatabase
//
//        if (!validarCedula(cedula)) {
//            mostrarMensajeError("Cédula inválida o no ecuatoriana")
//        } else if (!validarTextoSoloLetras(nombre)) {
//            mostrarMensajeError("Nombre debe contener solo letras")
//        } else if (!validarTextoSoloLetras(apellido)) {
//            mostrarMensajeError("Apellido debe contener solo letras")
//        } else if (!validarTextoSoloLetras(direccion)) {
//            mostrarMensajeError("Dirección debe contener solo letras")
//        } else if (!validarTelefono(telefono)) {
//            mostrarMensajeError("Teléfono inválido, debe tener 10 dígitos y comenzar con 0")
//        } else if (!validarEmail(correo)) {
//            mostrarMensajeError("Email inválido, debe contener @")
//        } else if (usuarioExiste(db, usuario)) {
//            mostrarMensajeError("Usuario ya existe")
//        } else if (cedulaExiste(db, cedula)) {
//            mostrarMensajeError("Ya existe una persona registrada con ese número de cédula")
//        } else {
//            val imagePath = saveImageToInternalStorage() // Guardar la imagen en almacenamiento interno
//
//            val values = ContentValues().apply {
//                put("nombre", nombre)
//                put("apellido", apellido)
//                put("cedula", cedula)
//                put("correo", correo)
//                put("direccion", direccion)
//                put("telefono", telefono)
//                put("usuario", usuario)
//                put("clave", clave)
//                put("foto", imagePath) // Guardar la ruta de la imagen
//            }
//
//            val newRowId = db.insert("usuarios", null, values)
//
//            if (newRowId != -1L) {
//                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
//                finish()
//            } else {
//                Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show()
//            }
//
//            db.close()
//        }
//    }
//
//    private fun saveImageToInternalStorage(): String? {
//        imageUri?.let { uri ->
//            val inputStream = contentResolver.openInputStream(uri)
//            val file = File(filesDir, "usuario_imagen_${System.currentTimeMillis()}.jpg")
//            val outputStream = FileOutputStream(file)
//            inputStream?.copyTo(outputStream)
//            inputStream?.close()
//            outputStream.close()
//            return file.absolutePath
//        }
//        return null
//    }
//
//    private fun checkAndRequestStoragePermission() {
//        val readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//        val writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//
//        val permissionsToRequest = mutableListOf<String>()
//
//        if (readPermission != PackageManager.PERMISSION_GRANTED) {
//            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
//        }
//        if (writePermission != PackageManager.PERMISSION_GRANTED) {
//            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        }
//
//        if (permissionsToRequest.isNotEmpty()) {
//            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), REQUEST_CODE_STORAGE_PERMISSION)
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            REQUEST_CODE_STORAGE_PERMISSION -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "Permiso de almacenamiento requerido", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    private fun usuarioExiste(db: SQLiteDatabase, usuario: String): Boolean {
//        val cursor = db.query(
//            "usuarios",
//            arrayOf("usuario"),
//            "usuario = ?",
//            arrayOf(usuario),
//            null,
//            null,
//            null
//        )
//        val exists = cursor.count > 0
//        cursor.close()
//        return exists
//    }
//
//    private fun cedulaExiste(db: SQLiteDatabase, cedula: String): Boolean {
//        val cursor = db.query(
//            "usuarios",
//            arrayOf("cedula"),
//            "cedula = ?",
//            arrayOf(cedula),
//            null,
//            null,
//            null
//        )
//        val exists = cursor.count > 0
//        cursor.close()
//        return exists
//    }
//
//    private fun validarCedula(cedula: String): Boolean {
//        if (cedula.length != 10) return false
//        val coeficientes = intArrayOf(2, 1, 2, 1, 2, 1, 2, 1, 2)
//        val digitos = cedula.map { it.toString().toInt() }
//        val suma = coeficientes.zip(digitos).map { (coef, digito) ->
//            val producto = coef * digito
//            if (producto >= 10) producto - 9 else producto
//        }.sum()
//        val digitoVerificador = (10 - (suma % 10)) % 10
//        return digitoVerificador == digitos.last()
//    }
//
//    private fun validarTextoSoloLetras(texto: String): Boolean {
//        return texto.all { it.isLetter() }
//    }
//
//    private fun validarTelefono(telefono: String): Boolean {
//        return telefono.length == 10 && telefono.startsWith("0")
//    }
//
//    private fun validarEmail(email: String): Boolean {
//        return email.contains("@")
//    }
//
//    private fun mostrarMensajeError(mensaje: String) {
//        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
//    }
//
//    private fun regresar() {
//        finish()
//    }
//
//    companion object {
//        private const val REQUEST_CODE_STORAGE_PERMISSION = 1
//    }
//}


import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.widget.ImageView
import android.database.sqlite.SQLiteDatabase
import android.database.Cursor

class RegisterActivity : AppCompatActivity() {
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etCedula: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etDireccion: EditText
    private lateinit var ettelefono: EditText
    private lateinit var etUsuario: EditText
    private lateinit var etClave: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnRegresar: Button
    private lateinit var btnSelectImage: Button
    private lateinit var imageView: ImageView

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etCedula = findViewById(R.id.etCedula)
        etCorreo = findViewById(R.id.etCorreo)
        etDireccion = findViewById(R.id.etDireccion)
        ettelefono = findViewById(R.id.ettelefono)
        etUsuario = findViewById(R.id.etUsuario)
        etClave = findViewById(R.id.etClave)
        imageView = findViewById(R.id.imageView)

        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnRegresar = findViewById(R.id.btnRegresar)
        btnSelectImage = findViewById(R.id.btnSelectImage)

        btnRegistrar.setOnClickListener { registrarUsuario() }
        btnRegresar.setOnClickListener { regresar() }
        btnSelectImage.setOnClickListener { selectImage() }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

//    private fun registrarUsuario() {
//        val nombre = etNombre.text.toString()
//        val apellido = etApellido.text.toString()
//        val cedula = etCedula.text.toString()
//        val correo = etCorreo.text.toString()
//        val direccion = etDireccion.text.toString()
//        val telefono = ettelefono.text.toString()
//        val usuario = etUsuario.text.toString()
//        val clave = etClave.text.toString()
//
//        if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty() || correo.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || usuario.isEmpty() || clave.isEmpty() || imageUri == null) {
//            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val dbHelper = DatabaseHelper(this)
//        val db = dbHelper.readableDatabase
//
//        if (!validarCedula(cedula)) {
//            mostrarMensajeError("Cédula inválida o no ecuatoriana")
//        } else if (!validarTextoSoloLetras(nombre)) {
//            mostrarMensajeError("Nombre debe contener solo letras")
//        } else if (!validarTextoSoloLetras(apellido)) {
//            mostrarMensajeError("Apellido debe contener solo letras")
//        } else if (!validarTextoSoloLetras(direccion)) {
//            mostrarMensajeError("Dirección debe contener solo letras")
//        } else if (!validarTelefono(telefono)) {
//            mostrarMensajeError("Teléfono inválido, debe tener 10 dígitos y comenzar con 0")
//        } else if (!validarEmail(correo)) {
//            mostrarMensajeError("Email inválido, debe contener @")
//        } else if (usuarioExiste(db, usuario)) {
//            mostrarMensajeError("Usuario ya existe")
//        } else if (cedulaExiste(db, cedula)) {
//            mostrarMensajeError("Ya existe una persona registrada con ese número de cédula")
//        } else {
//            val values = ContentValues().apply {
//                put("nombre", nombre)
//                put("apellido", apellido)
//                put("cedula", cedula)
//                put("correo", correo)
//                put("direccion", direccion)
//                put("telefono", telefono)
//                put("usuario", usuario)
//                put("clave", clave)
//                put("foto", imageUri.toString())
//            }
//
//            val newRowId = db.insert("usuarios", null, values)
//
//            if (newRowId != -1L) {
//                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
//                finish()
//            } else {
//                Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show()
//            }
//
//            db.close()
//        }
//    }

    private fun registrarUsuario() {
        val nombre = etNombre.text.toString()
        val apellido = etApellido.text.toString()
        val cedula = etCedula.text.toString()
        val correo = etCorreo.text.toString()
        val direccion = etDireccion.text.toString()
        val telefono = ettelefono.text.toString()
        val usuario = etUsuario.text.toString()
        val clave = etClave.text.toString()

        if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty() || correo.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || usuario.isEmpty() || clave.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase

        if (!validarCedula(cedula)) {
            mostrarMensajeError("Cédula inválida o no ecuatoriana")
        } else if (!validarTextoSoloLetras(nombre)) {
            mostrarMensajeError("Nombre debe contener solo letras")
        } else if (!validarTextoSoloLetras(apellido)) {
            mostrarMensajeError("Apellido debe contener solo letras")
        } else if (!validarTextoSoloLetras(direccion)) {
            mostrarMensajeError("Dirección debe contener solo letras")
        } else if (!validarTelefono(telefono)) {
            mostrarMensajeError("Teléfono inválido, debe tener 10 dígitos y comenzar con 0")
        } else if (!validarEmail(correo)) {
            mostrarMensajeError("Email inválido, debe contener @")
        } else if (usuarioExiste(db, usuario)) {
            mostrarMensajeError("Usuario ya existe")
        } else if (cedulaExiste(db, cedula)) {
            mostrarMensajeError("Ya existe una persona registrada con ese número de cédula")
        } else {
            val values = ContentValues().apply {
                put("nombre", nombre)
                put("apellido", apellido)
                put("cedula", cedula)
                put("correo", correo)
                put("direccion", direccion)
                put("telefono", telefono)
                put("usuario", usuario)
                put("clave", clave)
                put("foto", imageUri.toString())
            }

            val newRowId = db.insert("usuarios", null, values)

            if (newRowId != -1L) {
                // Guardar correo en SharedPreferences
                val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("user_email", correo)
                    apply()
                }
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show()
            }

            db.close()
        }
    }




    private fun usuarioExiste(db: SQLiteDatabase, usuario: String): Boolean {
        val cursor = db.query(
            "usuarios",
            arrayOf("usuario"),
            "usuario = ?",
            arrayOf(usuario),
            null,
            null,
            null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    private fun cedulaExiste(db: SQLiteDatabase, cedula: String): Boolean {
        val cursor = db.query(
            "usuarios",
            arrayOf("cedula"),
            "cedula = ?",
            arrayOf(cedula),
            null,
            null,
            null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    private fun validarCedula(cedula: String): Boolean {
        if (cedula.length != 10) return false
        val coeficientes = intArrayOf(2, 1, 2, 1, 2, 1, 2, 1, 2)
        val digitos = cedula.map { it.toString().toInt() }
        val suma = coeficientes.zip(digitos).map { (coef, digito) ->
            val producto = coef * digito
            if (producto >= 10) producto - 9 else producto
        }.sum()
        val digitoVerificador = (10 - (suma % 10)) % 10
        return digitoVerificador == digitos.last()
    }

    private fun validarTextoSoloLetras(texto: String): Boolean {
        return texto.all { it.isLetter() || it.isWhitespace() }
    }

    private fun validarTelefono(telefono: String): Boolean {
        return telefono.matches(Regex("^0\\d{9}$"))
    }

    private fun validarEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun mostrarMensajeError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun regresar() {
        finish()
    }
}
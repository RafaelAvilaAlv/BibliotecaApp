package com.example.bibliotecaapp
import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReporteUsuariosActivity : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_usuarios)

        tableLayout = findViewById(R.id.tableLayoutReport)

        findViewById<Button>(R.id.buttonDownloadPdf).setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_WRITE_EXTERNAL_STORAGE)
            } else {
                generatePdf()
            }
        }

        loadUsuarios()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        findViewById<TextView>(R.id.textViewReportDate).text = "Fecha del Reporte: $currentDate"
    }

    private fun loadUsuarios() {
        val dbHelper = DatabaseHelper(this)
        val usuarios = dbHelper.getAllUsuarios()

        val headerRow = TableRow(this)
        val headers = arrayOf("Nombre", "Apellido", "Cédula", "Correo", "Dirección", "Teléfono", "Usuario", "Clave")
        headers.forEach { headerText ->
            val header = TextView(this).apply {
                text = headerText
                setPadding(8, 8, 8, 8)
                setBackgroundColor(resources.getColor(R.color.tableHeaderBackground, null))
            }
            headerRow.addView(header)
        }
        tableLayout.addView(headerRow)

        for (usuario in usuarios) {
            val row = TableRow(this)
            val data = arrayOf(
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getCedula(),
                usuario.getCorreo(),
                usuario.getDireccion(),
                usuario.getTelefono(),
                usuario.getUsuario(),
                usuario.getClave()
            )

            data.forEach { cellText ->
                val cell = TextView(this).apply {
                    text = cellText
                    setPadding(8, 8, 8, 8)
                }
                row.addView(cell)
            }
            tableLayout.addView(row)
        }
    }
    private fun generatePdf() {
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val titlePaint = Paint()
        val pageInfo = PdfDocument.PageInfo.Builder(1500, 2000, 1).create() // Aumenta el tamaño de la página si es necesario
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val logoBitmap = BitmapFactory.decodeResource(resources, R.drawable.logo2)
        val scaledLogo = Bitmap.createScaledBitmap(logoBitmap, 150, 150, false)

        // Establecer color y tamaño del texto
        titlePaint.apply {
            color = Color.BLACK
            textSize = 50f
            textAlign = Paint.Align.CENTER
        }
        paint.apply {
            color = Color.BLACK
            textSize = 18f
            textAlign = Paint.Align.LEFT
        }

        // Dibuja el título
        val titleY = 100f
        canvas.drawText("Reporte de Usuarios", canvas.width / 2f, titleY, titlePaint)

        // Dibuja el logo a la derecha del título
        val logoX = canvas.width - 200f
        canvas.drawBitmap(scaledLogo, logoX, titleY - scaledLogo.height / 2, paint)

        // Ajusta la posición vertical para la fecha del reporte
        var yPos = titleY.toInt() + 80 // Espacio adicional entre el título y la fecha

        // Fecha del reporte
        paint.textSize = 16f
        canvas.drawText("Fecha del Reporte: ${findViewById<TextView>(R.id.textViewReportDate).text}", 100f, yPos.toFloat(), paint)
        yPos += 40

        // Encabezado de las columnas
        paint.textSize = 18f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val columnWidths = intArrayOf(150, 150, 150, 300, 150, 150, 150, 150)
        val headers = arrayOf("Nombre", "Apellido", "Cédula", "Correo", "Dirección", "Teléfono", "Usuario", "Clave")

        var xPos = 100
        for (i in headers.indices) {
            canvas.drawText(headers[i], xPos.toFloat(), yPos.toFloat(), paint)
            xPos += columnWidths[i]
        }
        yPos += 60 // Espacio adicional entre el encabezado y los datos de la tabla

        // Datos de los usuarios
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val rowHeight = 40
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as TableRow
            xPos = 100
            for (j in 0 until row.childCount) {
                val cell = row.getChildAt(j) as TextView
                canvas.drawText(cell.text.toString(), xPos.toFloat(), yPos.toFloat(), paint)
                xPos += columnWidths[j]
            }
            yPos += rowHeight
        }

        pdfDocument.finishPage(page)

        // Guardar el PDF en la carpeta de Descargas del dispositivo
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDirectory, "ReporteUsuarios.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(this, "PDF guardado en ${file.absolutePath}", Toast.LENGTH_LONG).show()

            // Abrir el PDF después de guardarlo
            openPdf(file)

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar PDF", Toast.LENGTH_LONG).show()
        }

        pdfDocument.close()
    }




    private fun openPdf(file: File) {
        val uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", file)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
        }

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No hay ninguna aplicación disponible para abrir PDF", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            generatePdf()
        } else {
            Toast.makeText(this, "Permiso de almacenamiento requerido para guardar el PDF", Toast.LENGTH_LONG).show()
        }
    }
}

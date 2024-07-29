package com.example.bibliotecaapp



import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.example.bibliotecaapp.adapter.LibrosAdapter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReporteLibrosActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var librosAdapter: LibrosAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var tableLayout: TableLayout

    private val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_libros)

        // Inicializar DatabaseHelper
        databaseHelper = DatabaseHelper(this)

        // Configurar TableLayout
        tableLayout = findViewById(R.id.tableLayoutReport)

        // Inicializar y configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView) // Asegúrate de que este ID esté en el XML
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.setHasFixedSize(true)

        // Configurar el adaptador
        librosAdapter = LibrosAdapter(emptyList())
        recyclerView.adapter = librosAdapter

        // Configurar botón de descarga
        findViewById<Button>(R.id.buttonDownloadPdf).setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_WRITE_EXTERNAL_STORAGE)
            } else {
                generatePdf()
            }
        }

        // Cargar datos
        loadLibros()

        // Establecer fecha del reporte
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        findViewById<TextView>(R.id.textViewReportDate).text = "Fecha del Reporte: $currentDate"
    }

    private fun loadLibros() {
        val libros = databaseHelper.getAllBooks()

        // Agregar encabezado a la tabla
        val headerRow = TableRow(this)
        val titleHeader = TextView(this).apply {
            text = "Título"
            setPadding(8, 8, 8, 8)
            setBackgroundColor(resources.getColor(R.color.tableHeaderBackground, null))
        }
        val authorHeader = TextView(this).apply {
            text = "Autor"
            setPadding(8, 8, 8, 8)
            setBackgroundColor(resources.getColor(R.color.tableHeaderBackground, null))
        }
        val genreHeader = TextView(this).apply {
            text = "Género"
            setPadding(8, 8, 8, 8)
            setBackgroundColor(resources.getColor(R.color.tableHeaderBackground, null))
        }

        headerRow.addView(titleHeader)
        headerRow.addView(authorHeader)
        headerRow.addView(genreHeader)
        tableLayout.addView(headerRow)

        // Agregar filas con los datos de los libros
        for (libro in libros) {
            val row = TableRow(this)
            val titleCell = TextView(this).apply {
                text = libro.titulo
                setPadding(8, 8, 8, 8)
            }
            val authorCell = TextView(this).apply {
                text = libro.autorNombre
                setPadding(8, 8, 8, 8)
            }
            val genreCell = TextView(this).apply {
                text = libro.generoNombre
                setPadding(8, 8, 8, 8)
            }

            row.addView(titleCell)
            row.addView(authorCell)
            row.addView(genreCell)
            tableLayout.addView(row)
        }
    }

    private fun generatePdf() {
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val pageInfo = PdfDocument.PageInfo.Builder(1000, 1500, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        paint.apply {
            color = Color.BLACK
            textSize = 16f
        }

        var yPos = 20
        canvas.drawText("Reporte de Libros", 100f, yPos.toFloat(), paint)

        yPos += 40

        // Agregar datos de la tabla al PDF
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as TableRow
            for (j in 0 until row.childCount) {
                val cell = row.getChildAt(j) as TextView
                canvas.drawText(cell.text.toString(), 100f + j * 200, yPos.toFloat(), paint)
            }
            yPos += 40
        }

        pdfDocument.finishPage(page)

        // Guardar el PDF en la carpeta de Descargas
        val downloadsDirectory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(downloadsDirectory, "ReporteLibros.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(this, "PDF guardado en ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar PDF", Toast.LENGTH_LONG).show()
        }

        pdfDocument.close()
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            generatePdf()
        } else {
            Toast.makeText(this, "Permiso de almacenamiento requerido para guardar el PDF", Toast.LENGTH_LONG).show()
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(System.currentTimeMillis())
    }
}

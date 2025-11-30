package pe.edu.pe.certiweb_android.public.pages.cardetail

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarPdfViewer(urlOrData: String, onBack: () -> Unit = {}) {
    var bitmaps by remember { mutableStateOf<List<Bitmap>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Use the global state if urlOrData is empty
    val dataToUse = urlOrData.ifBlank { pe.edu.pe.certiweb_android.public.pages.pdf.PdfState.data }

    LaunchedEffect(dataToUse) {
        if (dataToUse.isNullOrBlank()) {
            error = "PDF no disponible"
            loading = false
            return@LaunchedEffect
        }

        withContext(Dispatchers.IO) {
            try {
                // 1. Decode Base64
                val safeData = dataToUse!!
                val pureBase64 = if (safeData.contains(",")) safeData.split(",").last() else safeData
                val pdfBytes = Base64.decode(pureBase64, Base64.DEFAULT)

                // 2. Save to temp file
                val tempFile = File(context.cacheDir, "temp_cert_${System.currentTimeMillis()}.pdf")
                FileOutputStream(tempFile).use { it.write(pdfBytes) }

                // 3. Render pages
                val fileDescriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
                val renderer = PdfRenderer(fileDescriptor)
                val renderedBitmaps = mutableListOf<Bitmap>()

                for (i in 0 until renderer.pageCount) {
                    val page = renderer.openPage(i)
                    val bitmap = Bitmap.createBitmap(
                        page.width * 2, // High quality
                        page.height * 2,
                        Bitmap.Config.ARGB_8888
                    )
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    renderedBitmaps.add(bitmap)
                    page.close()
                }

                renderer.close()
                fileDescriptor.close()
                bitmaps = renderedBitmaps
            } catch (e: Exception) {
                error = "Error al cargar el PDF: ${e.localizedMessage}"
            } finally {
                loading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Certificación PDF", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E4D2B)
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFEEEEEE))
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF1E4D2B)
                )
            } else if (error != null) {
                Text(
                    text = error!!,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(bitmaps.size) { index ->
                        val bitmap = bitmaps[index]
                        Card(
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Page ${index + 1}",
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }
                }
            }
        }
    }
}

package pe.edu.pe.certiweb_android.public.pages.cardetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import pe.edu.pe.certiweb_android.certifications.services.CarService
import android.content.Intent
import android.net.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailScreen(carId: String, onViewCertificate: (String) -> Unit, onBack: () -> Unit = {}) {
    val service = remember { CarService() }
    var car by remember { mutableStateOf<Map<String, Any?>?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var loadingPdf by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(carId) {
        try {
            val res = service.getCarById(carId)
            car = if (res?.containsKey("data") == true) res["data"] as? Map<String, Any?> else res
            
            // Check if PDF needs to be loaded (logic from Flutter)
            val hasPdf = (car?.get("hasPdfCertification") as? Boolean) == true
            if (hasPdf) {
                loadingPdf = true
                try {
                    val pdfRes = service.getCarPdf(carId)
                    val cert = pdfRes["pdfCertification"]
                    val base64 = if (cert is Map<*, *>) cert["base64Data"]?.toString() else cert?.toString()
                    
                    if (!base64.isNullOrEmpty()) {
                        car = car?.toMutableMap()?.apply {
                            put("pdfBase64", base64)
                        }
                    }
                } catch (e: Exception) {
                    // Ignore PDF load error or handle it
                } finally {
                    loadingPdf = false
                }
            }
        } catch (e: Exception) {
            error = "No se pudo cargar el auto"
        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del vehículo", color = Color.White) },
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
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
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
            } else if (car == null) {
                Text(
                    text = "Vehículo no encontrado",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                val c = car!!
                val title = (c["title"] ?: "").toString()
                val brand = (c["brand"] ?: "").toString()
                val model = (c["model"] ?: "").toString()
                val year = (c["year"] ?: "").toString()
                val price = c["price"]
                val description = (c["description"] ?: "Sin descripción").toString()
                val imageUrl = (c["imageUrl"] ?: "").toString()
                val owner = (c["owner"] ?: "").toString()
                val licensePlate = (c["licensePlate"] ?: c["placa"] ?: "").toString()
                val hasPdf = (c["hasPdfCertification"] as? Boolean) == true
                val pdfBase64 = c["pdfBase64"] as? String
                val sellerEmail = (c["sellerEmail"] ?: c["ownerEmail"] ?: c["email"] ?: "").toString()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .background(Color.LightGray)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = if (imageUrl.isBlank()) "https://via.placeholder.com/600x400?text=No+Image" else imageUrl
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    // Title
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF2c3e50)
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tags Box
                    Row(
                        modifier = Modifier
                            .background(
                                color = Color(0xFF007bff).copy(alpha = 0.07f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = Color(0xFF007bff).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TagItem(icon = Icons.Filled.Sell, text = "Marca $brand")
                        TagItem(icon = Icons.Filled.DirectionsCar, text = "Modelo $model")
                        TagItem(icon = Icons.Filled.CalendarToday, text = "Año $year")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Price
                    Text(
                        text = formatCurrency(price),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF28a745)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Owner Info Box
                    DetailBox(title = "Información del propietario") {
                        Text("Propietario $owner")
                        Text("Placa $licensePlate")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description Box
                    DetailBox(title = "Descripción") {
                        Text(
                            text = description,
                            color = Color(0xFF444444)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // PDF Section
                    if (hasPdf) {
                        DetailBox(title = "Certificación técnica (PDF)") {
                            if (loadingPdf) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            } else if (pdfBase64 != null) {
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Button(
                                        onClick = { onViewCertificate(pdfBase64) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E4D2B))
                                    ) {
                                        Text("Ver")
                                    }
                                    
                                    Button(
                                        onClick = {
                                            if (sellerEmail.isNotBlank()) {
                                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                                    data = Uri.parse("mailto:$sellerEmail")
                                                    putExtra(Intent.EXTRA_SUBJECT, "Interés en vehículo $brand $model")
                                                    putExtra(Intent.EXTRA_TEXT, "Hola, me interesa el auto.\nMarca: $brand\nModelo: $model\nPlaca: $licensePlate")
                                                }
                                                try {
                                                    context.startActivity(intent)
                                                } catch (e: Exception) {
                                                    // Handle error
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E4D2B))
                                    ) {
                                        Text("Contactar")
                                    }
                                }
                            } else {
                                Text("Error al cargar la certificación PDF")
                            }
                        }
                    } else {
                        DetailBox(title = "Certificación técnica (PDF)") {
                            Text("Este vehículo no tiene certificación PDF")
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Back Button
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Button(
                            onClick = onBack,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E4D2B))
                        ) {
                            Text("Volver")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun TagItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF007bff),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Black)
        )
    }
}

@Composable
fun DetailBox(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF34495e)
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

fun formatCurrency(value: Any?): String {
    val numValue = when (value) {
        is String -> value.toDoubleOrNull()
        is Number -> value.toDouble()
        else -> null
    }
    return if (numValue != null) "S/ %.2f".format(numValue) else (value?.toString() ?: "")
}

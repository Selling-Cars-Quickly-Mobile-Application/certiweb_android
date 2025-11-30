package pe.edu.pe.certiweb_android.public.pages.carlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import pe.edu.pe.certiweb_android.certifications.services.CarService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarListScreen(
    brand: String? = null,
    model: String? = null,
    onBack: () -> Unit, 
    onSelect: (String) -> Unit
) {
    val service = remember { CarService() }
    var cars by remember { mutableStateOf<List<Map<String, Any?>>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val allCars = service.getAllCars()
            cars = allCars.filter { car ->
                val carBrand = (car["brand"] as? String)?.lowercase()
                val carModel = (car["model"] as? String)?.lowercase()
                
                val matchesBrand = brand?.let { carBrand == it.lowercase() } ?: true
                val matchesModel = model?.let { carModel == it.lowercase() } ?: true
                
                matchesBrand && matchesModel
            }
        } catch (e: Exception) {
            error = "No se pudo cargar la lista de autos"
        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vehículos certificados", color = Color.White) },
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
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color(0xFF1E4D2B))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Cargando...")
                }
            } else if (error != null) {
                Text(
                    text = error!!,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            } else if (cars.isEmpty()) {
                Text(
                    text = "No hay autos disponibles",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 300.dp),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(cars) { car ->
                        CarCard(car = car, onSelect = onSelect)
                    }
                }
            }
        }
    }
}

@Composable
fun CarCard(car: Map<String, Any?>, onSelect: (String) -> Unit) {
    val id = (car["id"] ?: "").toString()
    val title = (car["title"] ?: "").toString()
    val brand = (car["brand"] ?: "").toString()
    val model = (car["model"] ?: "").toString()
    val year = (car["year"] ?: "").toString()
    val price = car["price"]
    val description = (car["description"] ?: "").toString()
    val imageUrl = (car["imageUrl"] ?: "").toString()

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (id.isNotBlank()) onSelect(id) }
    ) {
        Column {
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

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2c3e50)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                
                // Brand - Model
                Text(
                    text = "$brand - $model",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E4D2B)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Year
                Text(
                    text = "Año $year",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF6c757d)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Description
                Text(
                    text = if (description.isBlank()) "Sin descripción" else description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF495057)
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.heightIn(min = 60.dp) // Ensure some height for consistency
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                // Price and Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatCurrency(price),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF28a745)
                        )
                    )
                    
                    Button(
                        onClick = { if (id.isNotBlank()) onSelect(id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E4D2B),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Ver detalles")
                    }
                }
            }
        }
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

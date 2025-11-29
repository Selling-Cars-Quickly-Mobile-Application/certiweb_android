package pe.edu.pe.certiweb_android.public.pages.cardetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import pe.edu.pe.certiweb_android.certifications.services.CarService

@Composable
fun CarDetailScreen(carId: String, onViewCertificate: (String) -> Unit) {
    val service = remember { CarService() }
    var car by remember { mutableStateOf<Map<String, Any?>?>(null) }
    LaunchedEffect(carId) {
        car = service.getCarById(carId)
    }
    val title = (car?.get("title") ?: "").toString()
    val brand = (car?.get("brand") ?: "").toString()
    val model = (car?.get("model") ?: "").toString()
    val year = (car?.get("year") ?: "").toString()
    val description = (car?.get("description") ?: "").toString()
    val imageUrl = (car?.get("imageUrl") ?: "").toString()
    val pdfCertification = (car?.get("pdfCertification") ?: "").toString()

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, style = MaterialTheme.typography.headlineSmall)
        if (imageUrl.isNotBlank()) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )
        }
        Text("Marca: $brand", style = MaterialTheme.typography.bodyMedium)
        Text("Modelo: $model", style = MaterialTheme.typography.bodyMedium)
        Text("Año: $year", style = MaterialTheme.typography.bodyMedium)
        Card { Text(description, modifier = Modifier.padding(12.dp)) }
        if (pdfCertification.isNotBlank()) {
            Button(onClick = { onViewCertificate(pdfCertification) }) {
                Text("Ver Certificado PDF")
            }
        }
    }
}

package pe.edu.pe.certiweb_android.public.pages.carlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import pe.edu.pe.certiweb_android.certifications.services.CarService

@Composable
fun CarListScreen(onBack: () -> Unit, onSelect: (String) -> Unit) {
    val service = remember { CarService() }
    var cars by remember { mutableStateOf<List<Map<String, Any?>>>(emptyList()) }
    LaunchedEffect(Unit) {
        cars = service.getAllCars()
    }
    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        item { Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Volver") } }
        items(cars) { car ->
            val id = (car["id"] ?: "").toString()
            val title = (car["title"] ?: "").toString()
            val brand = (car["brand"] ?: "").toString()
            val model = (car["model"] ?: "").toString()
            val imageUrl = (car["imageUrl"] ?: "").toString()
            Card(modifier = Modifier.padding(bottom = 12.dp).fillMaxWidth().clickable { if (id.isNotBlank()) onSelect(id) }) {
                Row(Modifier.padding(12.dp)) {
                    if (imageUrl.isNotBlank()) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = null,
                            modifier = Modifier.size(96.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(12.dp))
                    }
                    Column(Modifier.weight(1f)) {
                        Text(title, style = MaterialTheme.typography.titleMedium)
                        Text("$brand $model", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

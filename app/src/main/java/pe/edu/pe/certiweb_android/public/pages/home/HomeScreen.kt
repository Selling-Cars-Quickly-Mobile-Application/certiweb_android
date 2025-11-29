package pe.edu.pe.certiweb_android.public.pages.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import pe.edu.pe.certiweb_android.certifications.services.CarService

@Composable
fun HomeScreen(onBack: () -> Unit, onSelect: (String) -> Unit) {
    val service = remember { CarService() }
    var cars by remember { mutableStateOf<List<Map<String, Any?>>>(emptyList()) }
    LaunchedEffect(Unit) {
        cars = service.getAllCars()
    }
    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        item { Text("Inicio", style = MaterialTheme.typography.headlineMedium) }
        item { Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Volver") } }
        items(cars) { car ->
            val id = (car["id"] ?: "").toString()
            val title = (car["title"] ?: "").toString()
            val imageUrl = (car["imageUrl"] ?: "").toString()
            Card(modifier = Modifier.padding(bottom = 12.dp).fillMaxWidth().clickable { if (id.isNotBlank()) onSelect(id) }) {
                Column(Modifier.padding(12.dp)) {
                    if (imageUrl.isNotBlank()) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(180.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                    Text(title, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

package pe.edu.pe.certiweb_android.certifications.components.reservation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.edu.pe.certiweb_android.certifications.services.ReservationService

@Composable
fun ReservationListScreen(onBack: () -> Unit, onCreateNew: () -> Unit) {
    val service = remember { ReservationService() }
    var reservations by remember { mutableStateOf<List<Map<String, Any?>>>(emptyList()) }
    LaunchedEffect(Unit) {
        reservations = service.getReservations()
    }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Volver") }
            Button(onClick = onCreateNew, modifier = Modifier.weight(1f)) { Text("Nueva reserva") }
        }
        Text("Reservas", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(vertical = 12.dp))
        LazyColumn(Modifier.fillMaxSize()) {
            items(reservations) { r ->
                val name = (r["reservationName"] ?: "").toString()
                val email = (r["reservationEmail"] ?: "").toString()
                val brand = (r["brand"] ?: "").toString()
                val model = (r["model"] ?: "").toString()
                val license = (r["licensePlate"] ?: "").toString()
                val status = (r["status"] ?: "").toString()
                Card(Modifier.padding(bottom = 12.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text(name, style = MaterialTheme.typography.titleMedium)
                        Text(email, style = MaterialTheme.typography.bodyMedium)
                        Text(brand + " " + model, style = MaterialTheme.typography.bodyMedium)
                        Text(license, style = MaterialTheme.typography.bodySmall)
                        Text("Estado: " + status, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

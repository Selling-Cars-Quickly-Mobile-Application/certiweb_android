package pe.edu.pe.certiweb_android.certifications.components.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pe.edu.pe.certiweb_android.certifications.services.ReservationService

@Composable
fun AdminCertificationScreen() {
    val service = remember { ReservationService() }
    var reservations by remember { mutableStateOf<List<Map<String, Any?>>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        reservations = service.getReservations()
    }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        items(reservations) { r ->
            val id = (r["id"] ?: "").toString()
            val name = (r["reservationName"] ?: "").toString()
            val status = (r["status"] ?: "").toString()
            Card(Modifier.padding(bottom = 12.dp)) {
                Column(Modifier.padding(12.dp)) {
                    Text("Reserva: $name")
                    Text("Estado: $status")
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = {
                            if (id.isNotBlank()) scope.launch {
                                service.updateReservationStatus(id, "accepted")
                                reservations = service.getReservations()
                            }
                        }) { Text("Aceptar") }
                        Button(onClick = {
                            if (id.isNotBlank()) scope.launch {
                                service.updateReservationStatus(id, "pending")
                                reservations = service.getReservations()
                            }
                        }) { Text("Pendiente") }
                    }
                }
            }
        }
    }
}

package pe.edu.pe.certiweb_android.certifications.components.reservation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pe.edu.pe.certiweb_android.certifications.services.ReservationService

@Composable
fun ReservationScreen(onCreated: () -> Unit) {
    val service = remember { ReservationService() }
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var license by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Nueva Reserva")
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Marca") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("Modelo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = license, onValueChange = { license = it }, label = { Text("Placa") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            scope.launch {
                val data = mapOf(
                    "reservationName" to name,
                    "reservationEmail" to email,
                    "brand" to brand,
                    "model" to model,
                    "licensePlate" to license,
                    "price" to price,
                    "status" to "pending"
                )
                val res = service.createReservation(data)
                message = if (res != null) "Reserva creada" else "Error al crear"
                if (res != null) onCreated()
            }
        }) { Text("Crear") }
        if (message.isNotBlank()) Text(message)
    }
}

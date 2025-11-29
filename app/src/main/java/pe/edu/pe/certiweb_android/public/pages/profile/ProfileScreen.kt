package pe.edu.pe.certiweb_android.public.pages.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import pe.edu.pe.certiweb_android.certifications.services.UserService

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    val ctx = LocalContext.current
    val service = remember { UserService(ctx) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    androidx.compose.runtime.LaunchedEffect(Unit) {
        val res = service.findUserBySession()
        res.onSuccess { user ->
            name = (user["name"] ?: "").toString()
            email = (user["email"] ?: "").toString()
        }
    }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Perfil")
        Text("Nombre: $name")
        Text("Correo: $email")
        Button(onClick = { service.logout(); onLogout() }) { Text("Cerrar sesión") }
    }
}

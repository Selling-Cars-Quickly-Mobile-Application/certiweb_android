package pe.edu.pe.certiweb_android.public.pages.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SupportScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Soporte",)
        Text("Para soporte, contáctanos en soporte@certiweb.com")
        Text("Horarios: L-V 9:00-18:00")
    }
}

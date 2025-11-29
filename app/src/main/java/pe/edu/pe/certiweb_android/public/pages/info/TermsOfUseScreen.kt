package pe.edu.pe.certiweb_android.public.pages.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TermsOfUseScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Términos de Uso")
        Text("Esta aplicación es para fines educativos y demostrativos.")
    }
}

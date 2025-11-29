package pe.edu.pe.certiweb_android.certifications.components.dashboard.toolbar

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material3.ExperimentalMaterial3Api

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DashboardToolbar(
    onOpenHome: () -> Unit,
    onOpenAds: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenReservation: () -> Unit,
    onOpenSupport: () -> Unit,
    onOpenTerms: () -> Unit
) {
    TopAppBar(
        title = { Text("Certiweb") },
        actions = {
            ToolbarAction(Icons.Filled.Home, "Inicio", onOpenHome)
            ToolbarAction(Icons.Outlined.DirectionsCar, "Anuncios", onOpenAds)
            ToolbarAction(Icons.Filled.Person, "Perfil", onOpenProfile)
            ToolbarAction(Icons.Outlined.EventNote, "Reservas", onOpenReservation)
            ToolbarAction(Icons.Outlined.Help, "Soporte", onOpenSupport)
            ToolbarAction(Icons.Filled.Info, "Términos", onOpenTerms)
        },
        colors = TopAppBarDefaults.topAppBarColors()
    )
}

@Composable
private fun ToolbarAction(icon: ImageVector, contentDescription: String, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}

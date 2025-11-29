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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.graphics.Color

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DashboardToolbar(
    onOpenMenu: (() -> Unit)? = null,
    onOpenHome: () -> Unit,
    onOpenAds: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenReservation: () -> Unit,
    onOpenSupport: () -> Unit,
    onOpenTerms: () -> Unit
) {
    TopAppBar(
        title = { Text("CertiWeb") },
        navigationIcon = {
            IconButton(onClick = { onOpenMenu?.invoke() }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        actions = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF1E4D2B),
            titleContentColor = Color.White,
            actionIconContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Composable
private fun ToolbarAction(icon: ImageVector, contentDescription: String, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}

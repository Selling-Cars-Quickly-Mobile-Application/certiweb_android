package pe.edu.pe.certiweb_android.certifications.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import pe.edu.pe.certiweb_android.certifications.components.dashboard.toolbar.DashboardToolbar
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    onBack: () -> Unit,
    onOpenAds: () -> Unit,
    onOpenHome: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenReservation: () -> Unit,
    onOpenSupport: () -> Unit,
    onOpenTerms: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFF5F0E1), Color(0xFFEDE4D1))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(modifier = Modifier.padding(24.dp).fillMaxWidth(0.9f)) {
            Column(modifier = Modifier.padding(24.dp)) {
                DashboardToolbar(
                    onOpenHome = onOpenHome,
                    onOpenAds = onOpenAds,
                    onOpenProfile = onOpenProfile,
                    onOpenReservation = onOpenReservation,
                    onOpenSupport = onOpenSupport,
                    onOpenTerms = onOpenTerms
                )
                Text("Dashboard", style = MaterialTheme.typography.headlineMedium)
                Surface(color = Color(0xFF16A34A).copy(alpha = 0.1f)) {
                    Text("Redirección exitosa", color = Color(0xFF16A34A), modifier = Modifier.padding(16.dp))
                }
                Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Volver") }
            }
        }
    }
}

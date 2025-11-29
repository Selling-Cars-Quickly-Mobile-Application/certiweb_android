package pe.edu.pe.certiweb_android.certifications.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import pe.edu.pe.certiweb_android.certifications.components.dashboard.toolbar.DashboardToolbar
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.Icon
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

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
    val lang = remember { mutableStateOf("EN") }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = false,
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                    onClick = {
                        onOpenHome()
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Anuncios") },
                    selected = false,
                    icon = { Icon(Icons.Outlined.DirectionsCar, contentDescription = null) },
                    onClick = {
                        onOpenAds()
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Perfil") },
                    selected = false,
                    icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    onClick = {
                        onOpenProfile()
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Reservas") },
                    selected = false,
                    icon = { Icon(Icons.Outlined.EventNote, contentDescription = null) },
                    onClick = {
                        onOpenReservation()
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Soporte") },
                    selected = false,
                    icon = { Icon(Icons.Outlined.Help, contentDescription = null) },
                    onClick = {
                        onOpenSupport()
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Términos") },
                    selected = false,
                    icon = { Icon(Icons.Filled.Info, contentDescription = null) },
                    onClick = {
                        onOpenTerms()
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                DashboardToolbar(
                    onOpenMenu = { scope.launch { drawerState.open() } },
                    onOpenHome = onOpenHome,
                    onOpenAds = onOpenAds,
                    onOpenProfile = onOpenProfile,
                    onOpenReservation = onOpenReservation,
                    onOpenSupport = onOpenSupport,
                    onOpenTerms = onOpenTerms
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFF5F0E1), Color(0xFFEDE4D1))
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(
                                if (lang.value == "EN") "Welcome back!" else "¡Bienvenido de nuevo!",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color(0xFF1E293B),
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                            Surface(color = Color(0xFFFEF9C3), tonalElevation = 0.dp, shadowElevation = 0.dp) {
                                Text(
                                    if (lang.value == "EN") "We know your car is important" else "Sabemos que tu auto es importante",
                                    color = Color(0xFF854D0E),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                OutlinedButton(
                                    onClick = { lang.value = "EN" },
                                    modifier = Modifier.weight(1f),
                                    border = BorderStroke(if (lang.value == "EN") 3.dp else 2.dp, Color(0xFF1E4D2B))
                                ) {
                                    Icon(Icons.Outlined.Language, contentDescription = null)
                                    Text(" English", color = Color(0xFF1E4D2B))
                                }
                                OutlinedButton(
                                    onClick = { lang.value = "ES" },
                                    modifier = Modifier.weight(1f),
                                    border = BorderStroke(if (lang.value == "ES") 3.dp else 2.dp, Color(0xFF1E4D2B))
                                ) {
                                    Icon(Icons.Outlined.Language, contentDescription = null)
                                    Text(" Spanish", color = Color(0xFF1E4D2B))
                                }
                            }
                            Surface(color = Color(0xFF16A34A).copy(alpha = 0.1f)) {
                                Text(
                                    if (lang.value == "EN") "Redirected successfully" else "Redirección exitosa",
                                    color = Color(0xFF16A34A),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text(if (lang.value == "EN") "Back" else "Volver") }
                        }
                    }
                }
            }
        }
    }
}

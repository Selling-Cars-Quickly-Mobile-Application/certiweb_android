package pe.edu.pe.certiweb_android.public.pages.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.pe.certiweb_android.certifications.services.UserService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onLogout: () -> Unit, onBack: () -> Unit = {}) {
    val ctx = LocalContext.current
    val service = remember { UserService(ctx) }
    var user by remember { mutableStateOf<Map<String, Any?>>(emptyMap()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val res = service.findUserBySession()
        res.onSuccess { u ->
            user = u
        }
        loading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E4D2B))
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFF5F0E1), Color(0xFFEDE4D1)),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF1E4D2B)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier.widthIn(max = 500.dp),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column {
                            // Header
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(Color(0xFF1E4D2B), Color(0xFF2D6B3F)),
                                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                            end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                                        )
                                    )
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Perfil",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                )
                            }

                            // Body
                            Column(
                                modifier = Modifier.padding(24.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                ProfileRow("Nombre", (user["name"] ?: "No disponible").toString())
                                ProfileRow("Email", (user["email"] ?: "No disponible").toString())
                                ProfileRow("Plan", (user["plan"] ?: "No disponible").toString())

                                Spacer(modifier = Modifier.height(12.dp))

                                // Actions
                                TextButton(
                                    onClick = onBack,
                                    modifier = Modifier.align(Alignment.Start)
                                ) {
                                    Text("Volver al inicio", color = Color(0xFF1E4D2B))
                                }
                                
                                Button(
                                    onClick = {
                                        service.logout()
                                        onLogout()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E4D2B)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Cerrar sesión")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9FAFB), RoundedCornerShape(12.dp))
            .border(2.dp, Color(0xFF1E4D2B).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            Icons.Default.Person, 
            contentDescription = null, 
            tint = Color(0xFF1E4D2B), 
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = label,
            color = Color(0xFF1E4D2B),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        Text(
            text = value,
            color = Color(0xFF495057),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End
        )
    }
}

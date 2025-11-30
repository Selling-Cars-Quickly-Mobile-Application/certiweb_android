package pe.edu.pe.certiweb_android.public.pages.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.pe.certiweb_android.certifications.services.ReservationService
import pe.edu.pe.certiweb_android.certifications.services.UserService
import pe.edu.pe.certiweb_android.public.pages.history.history_item.HistoryItemWidget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val reservationService = remember { ReservationService() }
    val userService = remember { UserService(context) }
    
    var reservations by remember { mutableStateOf<List<Map<String, Any?>>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val userResult = userService.findUserBySession()
            userResult.onSuccess { user ->
                val userIdStr = user["id"]?.toString() ?: user["userId"]?.toString()
                // Convert string ID to Int safely
                val userId = userIdStr?.toDoubleOrNull()?.toInt() 
                
                if (userId != null) {
                    val result = reservationService.getReservationsByUserId(userId)
                    reservations = result
                } else {
                    error = "No se pudo verificar el usuario."
                }
            }.onFailure {
                error = "No se pudo verificar el usuario. Por favor, inicie sesión."
            }
        } catch (e: Exception) {
            error = "No se pudo cargar el historial. Inténtalo más tarde."
        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
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
                        colors = listOf(Color(0xFFF8F9FA), Color(0xFFFFFFFF)),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Title with Gradient Text (Simulated with colored Text as Compose ShaderMask is complex for simple text)
                // Or better, just use the color from the Flutter code
                Text(
                    text = "Historial de Reservas",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontSize = 32.sp, // Adjusted size
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E4D2B), // Fallback solid color
                        letterSpacing = (-0.5).sp
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Underline
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(4.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF1E4D2B), Color(0xFF2D6B3F))
                            ),
                            RoundedCornerShape(2.dp)
                        )
                )

                Spacer(modifier = Modifier.height(48.dp))

                when {
                    loading -> {
                        LoadingState()
                    }
                    error != null -> {
                        ErrorState(error!!)
                    }
                    reservations.isEmpty() -> {
                        EmptyState()
                    }
                    else -> {
                        reservations.forEach { reservation ->
                            HistoryItemWidget(reservation = reservation)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingState() {
    Column(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color.Black.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = Color(0xFF1E4D2B),
            strokeWidth = 4.dp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Cargando historial...", color = Color(0xFF495057))
    }
}

@Composable
fun ErrorState(msg: String) {
    Column(
        modifier = Modifier
            .background(
                Brush.linearGradient(
                    listOf(Color.Red.copy(alpha = 0.05f), Color.Red.copy(alpha = 0.02f))
                ),
                RoundedCornerShape(16.dp)
            )
            .border(1.dp, Color.Red.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Filled.ErrorOutline, contentDescription = null, tint = Color.Red, modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = msg,
            color = Color(0xFF2C3E50),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .background(
                Brush.linearGradient(
                    listOf(Color.Blue.copy(alpha = 0.05f), Color.Blue.copy(alpha = 0.02f))
                ),
                RoundedCornerShape(16.dp)
            )
            .border(1.dp, Color.Blue.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Filled.Inbox, contentDescription = null, tint = Color(0xFF3B82F6), modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No hay reservas en tu historial",
            color = Color(0xFF2C3E50),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}

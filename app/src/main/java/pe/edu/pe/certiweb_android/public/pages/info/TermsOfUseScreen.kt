package pe.edu.pe.certiweb_android.public.pages.info

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfUseScreen(
    mode: String = "view", // "view" or "accept"
    onAccept: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var showRejectDialog by remember { mutableStateOf(false) }

    if (showRejectDialog) {
        AlertDialog(
            onDismissRequest = { showRejectDialog = false },
            title = { Text("Salir de la aplicación") },
            text = { Text("Es necesario aceptar los términos de uso para utilizar CertiWeb. ¿Estás seguro de que deseas salir?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRejectDialog = false
                        (context as? Activity)?.finish()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Salir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Términos de Uso", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF654321)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFF5F0E1), Color(0xFFF0E6D2))
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 900.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Header
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(Color(0xFF654321), Color(0xFF8B4513))
                                    )
                                )
                                .padding(horizontal = 24.dp, vertical = 20.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Términos de Uso",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 22.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Actualizado recientemente",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color.White.copy(alpha = 0.7f)
                                        )
                                    )
                                }
                            }
                        }

                        // Content
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Section(1, "Aceptación de Términos", "Al acceder y utilizar la aplicación CertiWeb, aceptas completamente estos términos de uso. Si no estás de acuerdo con alguna parte, no debes usar la aplicación. Nos reservamos el derecho de modificar estos términos en cualquier momento. El uso continuado de la aplicación tras cambios constituye aceptación de los nuevos términos.")
                            Section(2, "Privacidad y Protección de Datos", "CertiWeb protege tu información personal conforme a la normativa de protección de datos vigente. Recopilamos solo datos necesarios para el funcionamiento de la aplicación. Tus datos no serán compartidos con terceros sin tu consentimiento. Utilizamos cifrado y medidas de seguridad para proteger tu información. Puedes solicitar acceso, corrección o eliminación de tus datos en cualquier momento.")
                            Section(3, "Responsabilidades del Usuario", "Eres responsable de mantener la confidencialidad de tu contraseña y credenciales. No compartas tu cuenta con terceros. Garantizas que la información proporcionada es veraz y completa. No utilizarás la aplicación para actividades ilegales o fraudulentas. Asumes toda responsabilidad por las acciones realizadas en tu cuenta.")
                            Section(4, "Uso Prohibido", "Está prohibido: enviar virus, malware o código malicioso; acceder sin autorización a sistemas de CertiWeb; interferir con el funcionamiento de la aplicación; usar datos de otros usuarios; realizar actividades discriminatorias, abusivas o ilegales; violar derechos de propiedad intelectual; intentar eludir medidas de seguridad.")
                            Section(5, "Limitación de Responsabilidad", "CertiWeb se proporciona \"tal como está\" sin garantías implícitas. No nos hacemos responsables por daños indirectos, incidentales, especiales o consecuentes derivados del uso de la aplicación. Nos esforzamos por mantener la disponibilidad del servicio, pero no garantizamos funcionamiento ininterrumpido. Eres responsable de mantener copias de seguridad de tus datos.")
                            Section(6, "Modificaciones del Servicio", "Nos reservamos el derecho de modificar, suspender o discontinuar partes de la aplicación en cualquier momento. También podemos cambiar requisitos técnicos, características o funcionalidades. Notificaremos cambios significativos cuando sea posible. El uso continuado implica aceptación de los cambios.")
                            Section(7, "Terminación de Acceso", "Podemos suspender o terminar tu acceso a CertiWeb si incumples estos términos, realizas actividades ilegales, abusas del servicio o por razones de seguridad. Puedes eliminar tu cuenta en cualquier momento a través de la configuración de la aplicación. La eliminación de cuenta implicará pérdida de acceso a tus datos.")
                            Section(8, "Ley Aplicable", "Estos términos se rigen por la ley aplicable en la jurisdicción donde opera CertiWeb. Cualquier disputa será resuelta en los tribunales competentes de dicha jurisdicción. Aceptas someterte a la jurisdicción exclusiva de estos tribunales.")
                        }

                        // Buttons
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                        ) {
                            if (mode == "accept") {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Para continuar utilizando la aplicación, debes aceptar los términos de uso.",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color(0xFF5D4E37),
                                            fontStyle = FontStyle.Italic
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        OutlinedButton(
                                            onClick = { showRejectDialog = true },
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = Color.Red
                                            ),
                                            border = BorderStroke(1.dp, Color.Red),
                                            shape = RoundedCornerShape(30.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(end = 8.dp)
                                        ) {
                                            Text("Rechazar y Salir")
                                        }
                                        
                                        Button(
                                            onClick = onAccept,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF654321),
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(30.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(start = 8.dp)
                                        ) {
                                            Text("Aceptar")
                                        }
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Button(
                                        onClick = onBack,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF654321),
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(30.dp),
                                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                                    ) {
                                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Volver")
                                    }
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
fun Section(number: Int, title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .background(Color.White, RoundedCornerShape(15.dp))
            .border(1.dp, Color(0xFFCD853F).copy(alpha = 0.1f), RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFF8F5F0), Color(0xFFF0E6D2))
                    )
                )
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFCD853F), Color(0xFFDAA520))
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = number.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFF654321),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                )
            }
        }
        
        Box(modifier = Modifier.padding(20.dp)) {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF5D4E37)
                )
            )
        }
    }
}

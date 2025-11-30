package pe.edu.pe.certiweb_android.public.pages.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import pe.edu.pe.certiweb_android.public.services.AuthService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoggedIn: () -> Unit, onAdminLoggedIn: () -> Unit, onNavigateRegister: () -> Unit) {
    val ctx = LocalContext.current
    val auth = remember { AuthService(ctx) }
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf<String?>(null) }
    var rememberMe by remember { mutableStateOf(false) }

    val bgGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFF5F0E1), Color(0xFFEDE4D1))
    )
    val greenGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF1E4D2B), Color(0xFF2D6B3F))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .widthIn(max = 500.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(greenGradient)
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Iniciar sesión",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 28.sp,
                                letterSpacing = (-0.5).sp
                            ),
                            color = Color.White
                        )
                    }

                    // Body
                    Column(
                        modifier = Modifier.padding(32.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Email Input
                        CustomTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Correo electrónico",
                            hint = "correo@ejemplo.com",
                            icon = Icons.Outlined.Email
                        )

                        // Password Input
                        CustomTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Contraseña",
                            hint = "••••••••",
                            icon = Icons.Outlined.Lock,
                            isPassword = true
                        )

                        // Remember Me & Forgot Password
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = rememberMe,
                                    onCheckedChange = { rememberMe = it },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color(0xFF1E4D2B),
                                        uncheckedColor = Color(0xFF6B7280)
                                    )
                                )
                                Text(
                                    text = "Recordarme",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                            
                            TextButton(
                                onClick = { /* TODO */ },
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                            ) {
                                Text(
                                    text = "¿Olvidaste tu contraseña?",
                                    color = Color(0xFF1E4D2B),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Error Message
                        if (error != null) {
                            StatusMessage(
                                message = error!!,
                                color = Color(0xFFFF5252),
                                icon = Icons.Outlined.Info
                            )
                        }

                        // Success Message
                        if (success != null) {
                            StatusMessage(
                                message = success!!,
                                color = Color(0xFF4CAF50),
                                icon = Icons.Outlined.CheckCircle
                            )
                        }

                        // Login Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(greenGradient, shape = RoundedCornerShape(12.dp))
                        ) {
                            Button(
                                onClick = {
                                    loading = true
                                    error = null
                                    success = null
                                    if (email.isBlank() || password.isBlank() || !Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(email)) {
                                        error = "Por favor, completa los campos con datos válidos."
                                        loading = false
                                    } else {
                                        scope.launch {
                                            try {
                                                val userRes = auth.login(email.trim(), password, rememberMe)
                                                if (userRes["success"] as? Boolean == true) {
                                                    success = "Inicio de sesión exitoso"
                                                    onLoggedIn()
                                                } else {
                                                    val adminRes = auth.loginAdmin(email.trim(), password, rememberMe)
                                                    if (adminRes["success"] as? Boolean == true) {
                                                        success = "Inicio de sesión de administrador"
                                                        onAdminLoggedIn()
                                                    } else {
                                                        error = "Credenciales incorrectas. Verifica correo y contraseña."
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                error = "Error de conexión. Intenta de nuevo."
                                            } finally {
                                                loading = false
                                            }
                                        }
                                    }
                                },
                                enabled = !loading,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().height(56.dp)
                            ) {
                                Text(
                                    text = if (loading) "Iniciando sesión..." else "Entrar",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            }
                        }

                        // Register Link
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "¿No tienes cuenta?",
                                color = Color(0xFF6B7280),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(
                                onClick = onNavigateRegister,
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = Color(0xFF1E4D2B).copy(alpha = 0.1f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Regístrate",
                                    color = Color(0xFF1E4D2B),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    hint: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isPassword: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151),
                fontSize = 14.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = hint, color = Color(0xFF9CA3AF)) },
            leadingIcon = { Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF6B7280)) },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF9FAFB),
                unfocusedContainerColor = Color(0xFFF9FAFB),
                focusedBorderColor = Color(0xFF1E4D2B),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                cursorColor = Color(0xFF1E4D2B)
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun StatusMessage(message: String, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = message, color = color, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}

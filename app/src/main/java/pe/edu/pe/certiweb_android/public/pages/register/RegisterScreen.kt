package pe.edu.pe.certiweb_android.public.pages.register

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import pe.edu.pe.certiweb_android.public.services.AuthService

@Composable
fun RegisterScreen(onRegistered: () -> Unit, onNavigateLogin: () -> Unit) {
    val ctx = LocalContext.current
    val auth = remember { AuthService(ctx) }
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf<String?>(null) }
    var step by remember { mutableStateOf(0) }
    var selectedPlan by remember { mutableStateOf<String?>(null) }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val primaryGreen = Color(0xFF1E4D2B)
    val primaryGreenDark = Color(0xFF2D6B3F)
    val bgGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFF5F0E1), Color(0xFFEDE4D1))
    )
    val greenGradient = Brush.linearGradient(
        colors = listOf(primaryGreen, primaryGreenDark)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header mimicking SliverAppBar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(greenGradient),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Crear Cuenta",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Completa tu registro en 3 sencillos pasos",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .offset(y = (-32).dp) // Overlap slightly like the Sliver effect
            ) {
                // Step Indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val steps = listOf("Datos personales", "Plan", "Pago")
                    steps.forEachIndexed { index, label ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = if (index <= step) primaryGreen else Color.White,
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = 2.dp,
                                        color = if (index <= step) primaryGreen else Color(0xFFE5E7EB),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (index < step) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Text(
                                        text = "${index + 1}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (index <= step) Color.White else Color(0xFF64748B)
                                        )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = label,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = if (index <= step) FontWeight.Bold else FontWeight.Medium,
                                    color = if (index <= step) Color(0xFF1E293B) else Color(0xFF64748B)
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Main Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        when (step) {
                            0 -> {
                                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                    Text(
                                        text = "Datos Personales",
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1E293B)
                                        )
                                    )
                                    CustomTextField(
                                        value = name,
                                        onValueChange = { name = it },
                                        label = "Nombre Completo",
                                        hint = "Juan Pérez",
                                        icon = Icons.Outlined.Person
                                    )
                                    CustomTextField(
                                        value = email,
                                        onValueChange = { email = it },
                                        label = "Correo Electrónico",
                                        hint = "correo@ejemplo.com",
                                        icon = Icons.Outlined.Email
                                    )
                                    CustomTextField(
                                        value = password,
                                        onValueChange = { password = it },
                                        label = "Contraseña",
                                        hint = "••••••••",
                                        icon = Icons.Outlined.Lock,
                                        isPassword = true
                                    )
                                }
                            }
                            1 -> {
                                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                    Text(
                                        text = "Selecciona un Plan",
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1E293B)
                                        )
                                    )
                                    PlanCard(
                                        title = "Free",
                                        price = "S/.0.00",
                                        period = "Siempre",
                                        icon = Icons.Outlined.Person,
                                        features = listOf("Contacta con compradores", "Ver catálogo"),
                                        disabledFeatures = listOf("Sin reservas de auto", "Características limitadas"),
                                        isSelected = selectedPlan == "Free",
                                        onTap = { selectedPlan = "Free" }
                                    )
                                    PlanCard(
                                        title = "Mensual",
                                        price = "S/.50",
                                        period = "/mes",
                                        icon = Icons.Outlined.DateRange,
                                        features = listOf("Acceso completo 30 días", "Soporte técnico", "Actualizaciones incluidas"),
                                        isSelected = selectedPlan == "Mensual",
                                        onTap = { selectedPlan = "Mensual" }
                                    )
                                    PlanCard(
                                        title = "Anual",
                                        price = "S/.250.00",
                                        period = "/año",
                                        icon = Icons.Outlined.Edit, // Using Edit as approximation for Note
                                        features = listOf("Acceso completo 365 días", "Soporte prioritario", "Actualizaciones incluidas", "Características exclusivas"),
                                        isSelected = selectedPlan == "Anual",
                                        onTap = { selectedPlan = "Anual" },
                                        isBest = true
                                    )
                                }
                            }
                            2 -> {
                                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                    Text(
                                        text = "Información de Pago",
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1E293B)
                                        )
                                    )
                                    if (selectedPlan == "Free") {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0xFF16A34A).copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                                .border(1.dp, Color(0xFF16A34A).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                                .padding(16.dp)
                                        ) {
                                            Text(
                                                text = "Este plan es gratuito, no se requiere información de pago",
                                                color = Color(0xFF16A34A),
                                                fontWeight = FontWeight.SemiBold,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    } else {
                                        CustomTextField(
                                            value = cardNumber,
                                            onValueChange = { cardNumber = it },
                                            label = "Número de Tarjeta",
                                            hint = "4532 1234 5678 9010",
                                            icon = Icons.Outlined.CreditCard
                                        )
                                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                            Box(modifier = Modifier.weight(1f)) {
                                                CustomTextField(
                                                    value = expiryDate,
                                                    onValueChange = { expiryDate = it },
                                                    label = "Fecha de Expiración",
                                                    hint = "MM/YY",
                                                    icon = Icons.Outlined.DateRange
                                                )
                                            }
                                            Box(modifier = Modifier.weight(1f)) {
                                                CustomTextField(
                                                    value = cvv,
                                                    onValueChange = { cvv = it },
                                                    label = "CVV",
                                                    hint = "123",
                                                    icon = Icons.Outlined.Lock,
                                                    isPassword = true
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (error != null) {
                            StatusMessage(message = error!!, color = Color(0xFFDC2626), icon = Icons.Outlined.Info)
                        }
                        if (success != null) {
                            StatusMessage(message = success!!, color = Color(0xFF16A34A), icon = Icons.Outlined.CheckCircle)
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            if (step > 0) {
                                OutlinedButton(
                                    onClick = {
                                        error = null
                                        success = null
                                        step--
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFE5E7EB))
                                ) {
                                    Icon(Icons.Outlined.ArrowBack, contentDescription = null, tint = Color(0xFF374151), modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Anterior", color = Color(0xFF374151), fontWeight = FontWeight.Bold)
                                }
                            }
                            Button(
                                onClick = {
                                    error = null
                                    success = null
                                    if (step == 0) {
                                        if (name.isBlank() || email.isBlank() || password.isBlank() || !Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(email)) {
                                            error = "Completa los datos correctamente"
                                            return@Button
                                        }
                                        step++
                                    } else if (step == 1) {
                                        if (selectedPlan == null) {
                                            error = "Selecciona un plan"
                                            return@Button
                                        }
                                        step++
                                    } else {
                                        loading = true
                                        val plan = selectedPlan ?: "Free"
                                        if (plan != "Free" && (cardNumber.isBlank() || expiryDate.isBlank() || cvv.isBlank())) {
                                            error = "Completa la información de pago"
                                            loading = false
                                            return@Button
                                        }
                                        scope.launch {
                                            val userData = mapOf(
                                                "name" to name.trim(),
                                                "email" to email.trim(),
                                                "password" to password,
                                                "plan" to plan
                                            )
                                            val result = auth.register(userData)
                                            if (result["success"] as? Boolean == true) {
                                                success = "Registro exitoso"
                                                onRegistered()
                                            } else {
                                                error = (result["message"] ?: "Registro fallido").toString()
                                            }
                                            loading = false
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                                enabled = !loading
                            ) {
                                Text(if (step == 2) "Completar" else "Siguiente", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(Modifier.width(8.dp))
                                Icon(if (step == 2) Icons.Rounded.Check else Icons.Outlined.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            TextButton(onClick = onNavigateLogin) {
                                Text(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(color = Color(0xFF6B7280))) {
                                            append("¿Ya tienes una cuenta? ")
                                        }
                                        withStyle(style = SpanStyle(color = primaryGreen, fontWeight = FontWeight.Bold)) {
                                            append("Inicia sesión aquí")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    hint: String,
    icon: ImageVector,
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
private fun StatusMessage(message: String, color: Color, icon: ImageVector) {
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

@Composable
private fun PlanCard(
    title: String,
    price: String,
    period: String,
    icon: ImageVector,
    features: List<String>,
    disabledFeatures: List<String> = emptyList(),
    isSelected: Boolean,
    onTap: () -> Unit,
    isBest: Boolean = false
) {
    val primaryGreen = Color(0xFF1E4D2B)
    val borderGray = Color(0xFFE5E7EB)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFFF0F9F4) else Color.White),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 3.dp else 2.dp,
            color = if (isSelected) primaryGreen else borderGray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTap() },
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 0.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            if (isBest) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .background(
                            brush = Brush.linearGradient(listOf(Color(0xFFF59E0B), Color(0xFFF97316))),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "RECOMENDADO",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = primaryGreen, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E293B))) {
                        append(price)
                    }
                    withStyle(SpanStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF64748B))) {
                        append(" $period")
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            features.forEach { feature ->
                Row(modifier = Modifier.padding(bottom = 8.dp)) {
                    Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(feature, style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF475569), fontWeight = FontWeight.Medium))
                }
            }
            disabledFeatures.forEach { feature ->
                Row(modifier = Modifier.padding(bottom = 8.dp)) {
                    Icon(Icons.Outlined.Close, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(feature, style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF475569), fontWeight = FontWeight.Medium))
                }
            }
        }
    }
}
package pe.edu.pe.certiweb_android.public.pages.history.history_item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun HistoryItemWidget(reservation: Map<String, Any?>) {
    val imageUrl = (reservation["imageUrl"] as? String) ?: ""
    val name = (reservation["reservationName"] as? String) ?: "N/A"
    val brand = (reservation["brand"] as? String) ?: ""
    val model = (reservation["model"] as? String) ?: ""
    val date = formatDateTime((reservation["inspectionDateTime"] as? String) ?: "")
    val price = (reservation["price"] as? String) ?: ""
    val status = (reservation["status"] as? String) ?: ""

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        Column {
            // Image
            AsyncImage(
                model = if (imageUrl.isNotEmpty()) imageUrl else "https://static.vecteezy.com/system/resources/previews/005/720/408/non_2x/crossed-image-icon-picture-not-available-delete-picture-symbol-free-vector.jpg",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Title
                Text(
                    text = name,
                    color = Color(0xFF2C3E50),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Brand Chip
                Box(
                    modifier = Modifier
                        .background(Color(0xFF1E4D2B).copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                        .border(1.5.dp, Color(0xFF1E4D2B), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "$brand - $model",
                        color = Color(0xFF1E4D2B),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Details Box
                Column(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.02f), RoundedCornerShape(12.dp))
                        .border(1.dp, Color.Black.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    DetailRow("Fecha y hora", date)
                    Divider(color = Color.Black.copy(alpha = 0.05f), modifier = Modifier.padding(vertical = 8.dp))
                    DetailRow("Precio", "S/ $price")
                    Divider(color = Color.Black.copy(alpha = 0.05f), modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Status Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Estado",
                            color = Color(0xFF2C3E50),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        
                        val statusColor = getStatusColor(status)
                        Box(
                            modifier = Modifier
                                .background(statusColor.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = status.uppercase(),
                                color = statusColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color(0xFF2C3E50),
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        )
        Text(
            text = value,
            color = Color(0xFF495057),
            fontSize = 13.sp
        )
    }
}

fun formatDateTime(s: String): String {
    if (s.isBlank()) return "Fecha no especificada"
    return try {
        val parsed = LocalDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME)
        parsed.toString().replace("T", " ") // Simple formatting, can be improved
    } catch (e: DateTimeParseException) {
        "Fecha inválida"
    }
}

fun getStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "accepted", "confirmada" -> Color(0xFF28A745)
        "pending", "pendiente" -> Color(0xFFFFC107)
        "cancelled", "cancelada" -> Color(0xFFDC3545)
        "completed", "completada" -> Color(0xFF007BFF)
        else -> Color(0xFF6C757D)
    }
}

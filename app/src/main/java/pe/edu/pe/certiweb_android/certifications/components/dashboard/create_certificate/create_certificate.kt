package pe.edu.pe.certiweb_android.certifications.components.dashboard.create_certificate

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun CreateCertificateBanner(
    onStartCertification: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F0E1))
            .padding(vertical = 32.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.widthIn(max = 600.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Image Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(20.dp, RoundedCornerShape(8.dp), spotColor = Color.Black.copy(alpha = 0.1f))
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://img.freepik.com/foto-gratis/coche-lujoso-estacionado-carretera-faro-iluminado-al-atardecer_181624-60607.jpg?semt=ais_hybrid&w=740")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Certifica tu vehículo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Adjusted height for mobile
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Certifica tu vehículo",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B4332)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Asegura la autenticidad de tu vehículo con nuestro proceso de certificación seguro y confiable.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF495057),
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.5
                )
            )
        }
    }
}

package pe.edu.pe.certiweb_android.certifications.components.dashboard.brand_search

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun BrandSearch(
    onBrandSelected: (String) -> Unit,
    onScrollToTop: () -> Unit
) {
    val brands = remember {
        listOf(
            Brand("Audi", "https://upload.wikimedia.org/wikipedia/commons/9/92/Audi-Logo_2016.svg", Color(0xFFBB0A30)),
            Brand("Mercedes-Benz", "https://www.pngarts.com/files/3/Mercedes-Benz-Logo-PNG-Photo.png", Color(0xFF00ADEF)),
            Brand("BMW", "https://upload.wikimedia.org/wikipedia/commons/4/44/BMW.svg", Color(0xFF0066B2)),
            Brand("Volkswagen", "https://cdn.worldvectorlogo.com/logos/volkswagen-10.svg", Color(0xFF001E50))
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 48.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Explora nuestras marcas",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Descubre vehículos certificados de las mejores marcas del mercado",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF64748B),
                lineHeight = 24.sp
            ),
            textAlign = TextAlign.Center,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(48.dp))

        val chunkedBrands = brands.chunked(2)
        chunkedBrands.forEachIndexed { _, rowBrands ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                rowBrands.forEach { brand ->
                    Box(modifier = Modifier.weight(1f)) {
                        BrandCard(
                            brand = brand,
                            onClick = { onBrandSelected(brand.name) }
                        )
                    }
                }
                if (rowBrands.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedButton(
            onClick = onScrollToTop,
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Icon(Icons.Filled.ArrowUpward, contentDescription = null, tint = Color(0xFF64748B))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Volver al inicio", color = Color(0xFF64748B))
        }
    }
}

data class Brand(val name: String, val logoUrl: String, val color: Color)

@Composable
fun BrandCard(
    brand: Brand,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val elevation by animateDpAsState(if (isPressed) 12.dp else 8.dp, label = "elevation")
    val barWidth by animateDpAsState(if (isPressed) 40.dp else 0.dp, label = "barWidth")

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.8f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = if (isPressed)
                                listOf(Color.Gray.copy(alpha = 0.1f), Color.Gray.copy(alpha = 0.2f))
                            else
                                listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0))
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(brand.logoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = brand.name,
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Fit,
                    loading = {
                         Icon(Icons.Filled.DirectionsCar, contentDescription = null, tint = Color.Gray)
                    },
                    error = {
                        Icon(Icons.Filled.DirectionsCar, contentDescription = null, tint = Color.Gray)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = brand.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = if (isPressed) brand.color else Color(0xFF1E293B)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF64748B))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Ver vehículos",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .height(3.dp)
                    .width(barWidth)
                    .background(brand.color, RoundedCornerShape(2.dp))
            )
        }
    }
}

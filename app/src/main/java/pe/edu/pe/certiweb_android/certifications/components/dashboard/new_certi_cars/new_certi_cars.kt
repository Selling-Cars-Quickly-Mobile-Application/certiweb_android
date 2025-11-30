package pe.edu.pe.certiweb_android.certifications.components.dashboard.new_certi_cars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NewCertiCars(
    onCarClick: (String) -> Unit,
    onSeeMoreClick: () -> Unit
) {
    val cars = remember {
        listOf(
            Car("1", "BMW Serie 4", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSYVcLx4pGvnOpKwlHUU49s8jkRkJDGVxaiDw&s", "S/43,999.00", "Azul Metálico", "carDetail/4"),
            Car("2", "Ford Mustang GT", "https://www.vdm.ford.com/content/dam/na/ford/en_us/images/mustang/2025/jellybeans/Ford_Mustang_2025_200A_PJS_883_89W_13B_COU_64F_99H_44U_EBST_YZTAC_DEFAULT_EXT_4.png", "S/45,999.00", "Gris", "carDetail/5"),
            Car("3", "Kia Niro", "https://cdn.motor1.com/images/mgl/ojyBzq/s3/kia-niro-2025.jpg", "S/39,000.00", "Rojo", "carDetail/2"),
            Car("4", "Kia Sportage", "https://s3.amazonaws.com/kia-greccomotors/Sportage_blanca_01_9a1ad740c7.png", "S/32,999.00", "Blanco Perla", "carDetail/3")
        )
    }

    val pagerState = rememberPagerState(pageCount = { cars.size })
    val scope = rememberCoroutineScope()
    
    // Autoplay logic
    LaunchedEffect(pagerState.currentPage) {
        delay(5000)
        if (pagerState.currentPage < cars.size - 1) {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F0E1))
            .padding(vertical = 40.dp, horizontal = 12.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            Text(
                text = "Vehículos certificados",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B4332)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Descubre nuestra selección de vehículos certificados y de confianza",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF495057),
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.5
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                pageSpacing = 16.dp,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                CarCard(
                    car = cars[page],
                    onClick = { onCarClick(cars[page].route) }
                )
            }

            // Navigation Buttons
            IconButton(
                onClick = {
                    scope.launch {
                        if (pagerState.currentPage > 0) {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
                    .background(Color.White, CircleShape)
                    .shadow(2.dp, CircleShape)
            ) {
                Icon(Icons.Filled.ChevronLeft, contentDescription = "Previous", modifier = Modifier.size(28.dp))
            }

            IconButton(
                onClick = {
                    scope.launch {
                        if (pagerState.currentPage < cars.size - 1) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .background(Color.White, CircleShape)
                    .shadow(2.dp, CircleShape)
            ) {
                Icon(Icons.Filled.ChevronRight, contentDescription = "Next", modifier = Modifier.size(28.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { onSeeMoreClick() }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ver más modelos",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1B4332)
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Filled.ArrowForward, contentDescription = null, tint = Color(0xFF1B4332), modifier = Modifier.size(18.dp))
        }
    }
}

data class Car(val id: String, val name: String, val imageUrl: String, val price: String, val color: String, val route: String)

@Composable
fun CarCard(car: Car, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable { onClick() }
    ) {
        Column {
            Box(modifier = Modifier.weight(1f)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(car.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = car.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Gradient Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )
                // Price and View Details
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = car.price,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Ver detalles",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White)
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = car.name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF212529)
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = car.color,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF6C757D)
                    )
                )
            }
        }
    }
}

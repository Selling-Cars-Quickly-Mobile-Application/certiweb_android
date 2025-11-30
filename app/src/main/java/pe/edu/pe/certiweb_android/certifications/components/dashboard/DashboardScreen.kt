package pe.edu.pe.certiweb_android.certifications.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import pe.edu.pe.certiweb_android.certifications.components.dashboard.brand_search.BrandSearch
import pe.edu.pe.certiweb_android.certifications.components.dashboard.create_certificate.CreateCertificateBanner
import pe.edu.pe.certiweb_android.certifications.components.dashboard.new_certi_cars.NewCertiCars
import pe.edu.pe.certiweb_android.certifications.components.dashboard.search_filters.SearchFilters
import pe.edu.pe.certiweb_android.certifications.components.dashboard.toolbar.DashboardToolbar

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import pe.edu.pe.certiweb_android.certifications.services.UserService

@Composable
fun DashboardScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    onOpenAds: () -> Unit,
    onOpenHome: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenReservation: () -> Unit,
    onOpenSupport: () -> Unit,
    onOpenTerms: () -> Unit,
    onLogout: () -> Unit
) {
    // Dashboard integration
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    
    val context = LocalContext.current
    val userService = remember { UserService(context) }
    var userName by remember { mutableStateOf("Usuario") }

    LaunchedEffect(Unit) {
        val res = userService.findUserBySession()
        res.onSuccess { user ->
            userName = (user["name"] ?: "Usuario").toString()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                drawerContentColor = Color.Black,
                modifier = Modifier.width(350.dp)
            ) {
                DashboardDrawerContent(
                    userName = userName,
                    onClose = { scope.launch { drawerState.close() } },
                    onNavigate = { route ->
                        scope.launch { drawerState.close() }
                        when (route) {
                            "cars" -> onOpenAds()
                            "reservation" -> onOpenReservation()
                            "profile" -> onOpenProfile()
                            "history" -> onOpenHistory()
                            "support" -> onOpenSupport()
                            "terms" -> onOpenTerms()
                            else -> {}
                        }
                    },
                    onLogout = {
                        scope.launch { drawerState.close() }
                        onLogout()
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                DashboardToolbar(
                    userName = userName,
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
                        .verticalScroll(scrollState)
                ) {
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Padding(padding = 12.dp) {
                        SearchFilters(
                            onSearch = { brand, model ->
                                var route = "cars?brand=$brand"
                                if (model != null) {
                                    route += "&model=$model"
                                }
                                onNavigate(route)
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Padding(padding = 12.dp) {
                        NewCertiCars(
                            onCarClick = { route -> onNavigate(route) },
                            onSeeMoreClick = { onNavigate("cars") }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Padding(padding = 12.dp) {
                        CreateCertificateBanner(
                            onStartCertification = onOpenReservation
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Padding(padding = 12.dp) {
                        BrandSearch(
                            onBrandSelected = { brand -> onNavigate("cars?brand=$brand") },
                            onScrollToTop = {
                                scope.launch {
                                    scrollState.animateScrollTo(0)
                                }
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun Padding(padding: androidx.compose.ui.unit.Dp, content: @Composable BoxScope.() -> Unit) {
    Box(modifier = Modifier.padding(horizontal = padding, vertical = 0.dp), content = content)
}

@Composable
fun DashboardDrawerContent(
    userName: String,
    onClose: () -> Unit,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF1E4D2B), Color(0xFF2D5A3D)),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userName,
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Menú",
                        style = TextStyle(
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    )
                }
                
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Navigation Section
            DrawerSectionTitle("NAVEGACIÓN")
            DrawerMenuItem(
                icon = Icons.Default.DirectionsCar,
                title = "Vehículos certificados",
                subtitle = "Explora vehículos certificados",
                onClick = { onNavigate("cars") }
            )
            
            Divider(color = Color(0xFFEEEEEE))
            
            // Account Section
            DrawerSectionTitle("CUENTA")
            DrawerMenuItem(
                icon = Icons.Default.Person,
                title = "Perfil",
                subtitle = "Gestiona tu perfil",
                onClick = { onNavigate("profile") }
            )
            DrawerMenuItem(
                icon = Icons.Default.HelpOutline,
                title = "Soporte",
                subtitle = "Ayuda y soporte",
                onClick = { onNavigate("support") }
            )
            DrawerMenuItem(
                icon = Icons.Default.Description,
                title = "Términos de uso",
                subtitle = "Términos y condiciones",
                onClick = { onNavigate("terms") }
            )
            
            Divider(color = Color(0xFFEEEEEE))
            
            // Logout
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                LogoutMenuItem(onClick = onLogout)
            }
        }
    }
}

@Composable
fun DrawerSectionTitle(title: String) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF666666),
            letterSpacing = 0.5.sp
        ),
        modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp)
    )
}

@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF0F7F2)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF1E4D2B),
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color(0xFF333333)
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            )
        }
        
        Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = null,
            tint = Color(0xFFCCCCCC),
            modifier = Modifier.size(12.dp)
        )
    }
}

@Composable
fun LogoutMenuItem(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFEF7F7))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFEBEE)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = null,
                tint = Color(0xFFE53935),
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Cerrar sesión",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color(0xFFE53935)
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Cerrar sesión",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color(0xFFE53935)
                )
            )
        }
        
        Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = null,
            tint = Color(0xFFE53935),
            modifier = Modifier.size(12.dp)
        )
    }
}

package pe.edu.pe.certiweb_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pe.edu.pe.certiweb_android.ui.theme.Certiweb_androidTheme
import pe.edu.pe.certiweb_android.public.pages.login.LoginScreen
import pe.edu.pe.certiweb_android.public.pages.register.RegisterScreen
import pe.edu.pe.certiweb_android.certifications.components.dashboard.DashboardScreen
import pe.edu.pe.certiweb_android.config.DioClient
import pe.edu.pe.certiweb_android.public.pages.carlist.CarListScreen
import pe.edu.pe.certiweb_android.public.pages.cardetail.CarDetailScreen
import pe.edu.pe.certiweb_android.public.pages.cardetail.CarPdfViewer
import pe.edu.pe.certiweb_android.public.pages.info.SupportScreen
import pe.edu.pe.certiweb_android.public.pages.info.TermsOfUseScreen
import pe.edu.pe.certiweb_android.public.pages.profile.ProfileScreen
import pe.edu.pe.certiweb_android.public.pages.home.HomeScreen
import pe.edu.pe.certiweb_android.certifications.components.reservation.ReservationScreen
import pe.edu.pe.certiweb_android.certifications.components.admin.AdminCertificationScreen
import pe.edu.pe.certiweb_android.certifications.components.reservation.ReservationListScreen
import pe.edu.pe.certiweb_android.public.pages.history.HistoryScreen

import android.content.Context
import androidx.compose.ui.platform.LocalContext

class CertiwebMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DioClient.init(applicationContext)
        enableEdgeToEdge()
        
        val prefs = getSharedPreferences("CertiwebPreferences", Context.MODE_PRIVATE)
        val termsAccepted = prefs.getBoolean("termsAccepted", false)
        val rememberMe = prefs.getBoolean("rememberMe", false)

        // Clear session if rememberMe is false
        if (!rememberMe) {
            prefs.edit()
                .remove("authToken")
                .remove("currentUser")
                .remove("currentSession")
                .remove("adminToken")
                .remove("currentAdmin")
                .apply()
        }

        val authToken = prefs.getString("authToken", null)
        
        val startDestination = when {
            !termsAccepted -> "terms?mode=accept"
            !authToken.isNullOrBlank() -> "dashboard"
            else -> "login"
        }

        setContent {
            Certiweb_androidTheme { AppNavK(startDestination) }
        }
    }
}

@Composable
fun AppNavK(startDestination: String) {
    val navController = rememberNavController()
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                onLoggedIn = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onAdminLoggedIn = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegistered = {
                    navController.navigate("dashboard") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateLogin = { navController.popBackStack() }
            )
        }
        composable("dashboard") {
            DashboardScreen(
                onBack = {
                    // Handle back if needed, or just minimize
                },
                onNavigate = { route -> navController.navigate(route) },
                onOpenAds = { navController.navigate("cars") },
                onOpenHome = { /* principal sin imágenes */ },
                onOpenHistory = { navController.navigate("history") },
                onOpenProfile = { navController.navigate("profile") },
                onOpenReservation = { navController.navigate("reservation_list") },
                onOpenSupport = { navController.navigate("support") },
                onOpenTerms = { navController.navigate("terms") },
                onLogout = {
                    val prefs = context.getSharedPreferences("CertiwebPreferences", Context.MODE_PRIVATE)
                    prefs.edit()
                        .remove("authToken")
                        .remove("currentUser")
                        .remove("currentSession")
                        .remove("adminToken")
                        .remove("currentAdmin")
                        .apply()
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }
        composable(
            "cars?brand={brand}&model={model}",
            arguments = listOf(
                androidx.navigation.navArgument("brand") { nullable = true; defaultValue = null },
                androidx.navigation.navArgument("model") { nullable = true; defaultValue = null }
            )
        ) { backStackEntry ->
            val brand = backStackEntry.arguments?.getString("brand")
            val model = backStackEntry.arguments?.getString("model")
            CarListScreen(
                brand = brand,
                model = model,
                onBack = { navController.popBackStack() },
                onSelect = { id -> navController.navigate("carDetail/$id") }
            )
        }
        composable("carDetail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            CarDetailScreen(carId = id, onViewCertificate = { data -> pe.edu.pe.certiweb_android.public.pages.pdf.PdfState.data = data; navController.navigate("carPdf") })
        }
        composable("carPdf") { CarPdfViewer(urlOrData = "", onBack = { navController.popBackStack() }) }
        composable("support") { SupportScreen(onBack = { navController.popBackStack() }) }
        composable(
            "terms?mode={mode}",
            arguments = listOf(
                androidx.navigation.navArgument("mode") { nullable = true; defaultValue = "view" }
            )
        ) { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode") ?: "view"
            TermsOfUseScreen(
                mode = mode,
                onAccept = {
                    val prefs = context.getSharedPreferences("CertiwebPreferences", Context.MODE_PRIVATE)
                    prefs.edit().putBoolean("termsAccepted", true).apply()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("profile") { 
            ProfileScreen(
                onLogout = {
                    val prefs = context.getSharedPreferences("CertiwebPreferences", Context.MODE_PRIVATE)
                    prefs.edit()
                        .remove("authToken")
                        .remove("currentUser")
                        .remove("currentSession")
                        .remove("adminToken")
                        .remove("currentAdmin")
                        .apply()
                    navController.navigate("login") { 
                        popUpTo("dashboard") { inclusive = true } 
                    } 
                },
                onBack = { navController.popBackStack() }
            ) 
        }
        composable("reservation_list") { ReservationListScreen(onBack = { navController.popBackStack() }, onCreateNew = { navController.navigate("reservation_new") }) }
        composable("reservation_new") { ReservationScreen(onCreated = { navController.popBackStack() }) }
        composable("history") { HistoryScreen(onBack = { navController.popBackStack() }) }
        composable("admin") { AdminCertificationScreen() }
        // principal no necesita ruta dedicada, se usa dashboard
    }
}

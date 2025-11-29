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
import pe.edu.pe.certiweb_android.public.pages.pdf.CarPdfViewer
import pe.edu.pe.certiweb_android.public.pages.info.SupportScreen
import pe.edu.pe.certiweb_android.public.pages.info.TermsOfUseScreen
import pe.edu.pe.certiweb_android.public.pages.profile.ProfileScreen
import pe.edu.pe.certiweb_android.public.pages.home.HomeScreen
import pe.edu.pe.certiweb_android.certifications.components.reservation.ReservationScreen
import pe.edu.pe.certiweb_android.certifications.components.admin.AdminCertificationScreen
import pe.edu.pe.certiweb_android.certifications.components.reservation.ReservationListScreen

class CertiwebMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DioClient.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            Certiweb_androidTheme { AppNavK() }
        }
    }
}

@Composable
fun AppNavK() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
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
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                onOpenAds = { navController.navigate("cars") },
                onOpenHome = { /* principal sin imágenes */ },
                onOpenProfile = { navController.navigate("profile") },
                onOpenReservation = { navController.navigate("reservation_list") },
                onOpenSupport = { navController.navigate("support") },
                onOpenTerms = { navController.navigate("terms") }
            )
        }
        composable("cars") { CarListScreen(onBack = { navController.popBackStack() }, onSelect = { id -> navController.navigate("carDetail/$id") }) }
        composable("carDetail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            CarDetailScreen(carId = id, onViewCertificate = { data -> pe.edu.pe.certiweb_android.public.pages.pdf.PdfState.data = data; navController.navigate("carPdf") })
        }
        composable("carPdf") { CarPdfViewer(urlOrData = "") }
        composable("support") { SupportScreen() }
        composable("terms") { TermsOfUseScreen() }
        composable("profile") { ProfileScreen(onLogout = { navController.navigate("login") }) }
        composable("reservation_list") { ReservationListScreen(onBack = { navController.popBackStack() }, onCreateNew = { navController.navigate("reservation_new") }) }
        composable("reservation_new") { ReservationScreen(onCreated = { navController.popBackStack() }) }
        composable("admin") { AdminCertificationScreen() }
        // principal no necesita ruta dedicada, se usa dashboard
    }
}

package pe.edu.pe.certiweb_android.certifications.components.dashboard.search_filters

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import pe.edu.pe.certiweb_android.certifications.services.CarService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilters(
    onSearch: (String, String?) -> Unit
) {
    val service = remember { CarService() }
    val predefinedBrands = remember { listOf("Toyota", "Hyundai", "Kia", "Chevrolet", "Suzuki", "Mitsubishi", "Honda", "Volkswagen", "Ford", "Mercedes-Benz", "BMW", "Audi") }
    
    var cars by remember { mutableStateOf<List<Map<String, Any?>>>(emptyList()) }
    var selectedBrand by remember { mutableStateOf<String?>(null) }
    var selectedModel by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        loading = true
        try {
            cars = service.getAllCars()
        } catch (_: Exception) {
            cars = emptyList()
        } finally {
            loading = false
        }
    }
    
    val brands = remember(cars) {
        val carBrands = cars.mapNotNull { it["brand"]?.toString() }
        (predefinedBrands + carBrands).filter { it.isNotEmpty() }
            .map { it.replaceFirstChar { char -> char.uppercase() } }
            .distinct()
            .sorted()
    }

    val models = remember(selectedBrand, cars) {
        if (selectedBrand == null) {
            emptyList()
        } else {
            cars.filter { (it["brand"]?.toString() ?: "").equals(selectedBrand, ignoreCase = true) }
                .mapNotNull { it["model"]?.toString() }
                .distinct()
                .sorted()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(25.dp, RoundedCornerShape(16.dp), spotColor = Color.Black.copy(alpha = 0.05f))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(0xFFF8FAFC))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(24.dp)
    ) {
        Column {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0))
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = Color(0xFF475569), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Filtros de búsqueda",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Brand Dropdown
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Label, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Marca",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF475569)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                
                var brandExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = brandExpanded,
                    onExpandedChange = { brandExpanded = !brandExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedBrand ?: "",
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Selecciona una marca") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = brandExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF3B82F6),
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = brandExpanded,
                        onDismissRequest = { brandExpanded = false }
                    ) {
                        brands.sorted().forEach { brand ->
                            DropdownMenuItem(
                                text = { Text(brand) },
                                onClick = {
                                    selectedBrand = brand
                                    selectedModel = null
                                    brandExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Model Dropdown
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.DirectionsCar, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Modelo",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF475569)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                var modelExpanded by remember { mutableStateOf(false) }
                
                ExposedDropdownMenuBox(
                    expanded = modelExpanded,
                    onExpandedChange = { if (selectedBrand != null) modelExpanded = !modelExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedModel ?: "",
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Selecciona un modelo") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelExpanded) },
                        enabled = selectedBrand != null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF3B82F6),
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            disabledBorderColor = Color(0xFFE2E8F0),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color(0xFFF1F5F9)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = modelExpanded,
                        onDismissRequest = { modelExpanded = false }
                    ) {
                        models.forEach { model ->
                            DropdownMenuItem(
                                text = { Text(model) },
                                onClick = {
                                    selectedModel = model
                                    modelExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = {
                        selectedBrand = null
                        selectedModel = null
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF64748B))
                ) {
                    Text("Limpiar", fontWeight = FontWeight.Medium)
                }

                Button(
                    onClick = {
                        if (selectedBrand != null) {
                            onSearch(selectedBrand!!, selectedModel)
                        }
                    },
                    enabled = selectedBrand != null,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF93C5FD),
                        disabledContentColor = Color.White
                    )
                ) {
                    if (loading) {
                        Text("Cargando...")
                    } else {
                        Icon(Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Buscar", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

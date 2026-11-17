package com.example.entregapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.entregapp.model.RouteStop
import com.example.entregapp.ui.components.EntregappHeader
import com.example.entregapp.ui.theme.PetroleumBlue
import com.example.entregapp.ui.theme.SageGreen
import com.example.entregapp.ui.theme.SageGreenContainer

@Composable
fun AddAddressScreen(
    currentStops: List<RouteStop>,
    onAddStop: (newAddress: String, reference: String) -> Unit,
    onRemoveStop: (Int) -> Unit,
    onMoveStopUp: (Int) -> Unit,
    onMoveStopDown: (Int) -> Unit,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchAddressText by remember { mutableStateOf("") }
    var referenceText by remember { mutableStateOf("") }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    val quickSuggestions = listOf(
        Pair("Calle Universidad 123", "Campus A -> Sur"),
        Pair("Paseo de la Reforma 67", "Campus B S -> Norte"),
        Pair("Av. Insurgentes Sur 1500", "Campus C - Edificio Principal")
    )

    fun handleAdd(address: String) {
        if (address.isNotBlank()) {
            onAddStop(address, referenceText.ifBlank { "Dirección agregada por repartidor" })
            snackbarMessage = "¡Dirección '$address' agregada a la ruta!"
            searchAddressText = ""
            referenceText = ""
        }
    }

    Scaffold(
        topBar = {
            Column {
                EntregappHeader(
                    onLogoutClick = onLogoutClick,
                    onProfileClick = onProfileClick
                )

                // Subheader with Back Button
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Regresar",
                                tint = PetroleumBlue
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Agregar Dirección a Ruta Actual",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        snackbarHost = {
            snackbarMessage?.let { msg ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { snackbarMessage = null }) {
                            Text("OK", color = Color.White)
                        }
                    },
                    containerColor = PetroleumBlue
                ) {
                    Text(msg, color = Color.White)
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Ruta actual (List of existing stops)
            item {
                Text(
                    text = "Ruta actual",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            itemsIndexed(currentStops, key = { _, stop -> stop.id }) { index, stop ->
                CurrentStopListItem(
                    index = index,
                    stop = stop,
                    totalCount = currentStops.size,
                    onMoveUp = { onMoveStopUp(index) },
                    onMoveDown = { onMoveStopDown(index) },
                    onRemove = { onRemoveStop(stop.id) }
                )
            }

            // Section 2: Form Container NUEVA DIRECCIÓN
            item {
                Spacer(modifier = Modifier.height(8.dp))
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddLocation,
                                contentDescription = "Nueva dirección",
                                tint = PetroleumBlue
                            )
                            Text(
                                text = "NUEVA DIRECCIÓN",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = PetroleumBlue
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Search Address TextField
                        OutlinedTextField(
                            value = searchAddressText,
                            onValueChange = { searchAddressText = it },
                            placeholder = { Text("Buscar o escribir dirección...") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Buscar",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            trailingIcon = {
                                if (searchAddressText.isNotEmpty()) {
                                    IconButton(onClick = { searchAddressText = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Limpiar"
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Reference Note Field (optional)
                        OutlinedTextField(
                            value = referenceText,
                            onValueChange = { referenceText = it },
                            placeholder = { Text("Referencia (ej: Frente a cafetería, Edif B)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Suggestions Box
                        Text(
                            text = "Sugerencias rápidas",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            quickSuggestions.forEach { (address, ref) ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { searchAddressText = address; referenceText = ref },
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Place,
                                            contentDescription = "Pin",
                                            tint = SageGreen,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = address,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 13.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = ref,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // UBICACIÓN ACTUAL Button
                        OutlinedButton(
                            onClick = {
                                searchAddressText = "Av. Politécnico 2050, Edif. Central"
                                referenceText = "Ubicación GPS actual detectada"
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = "Ubicación actual",
                                    tint = SageGreen
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "UBICACIÓN ACTUAL",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = SageGreen
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // GUARDAR Y AGREGAR SIGUIENTE Button
                        Button(
                            onClick = { handleAdd(searchAddressText) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SageGreen),
                            enabled = searchAddressText.isNotBlank()
                        ) {
                            Text(
                                text = "GUARDAR Y AGREGAR SIGUIENTE",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // CONFIRMAR DIRECCIÓN Button
                        Button(
                            onClick = {
                                handleAdd(searchAddressText)
                                onBackClick()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PetroleumBlue),
                            enabled = searchAddressText.isNotBlank()
                        ) {
                            Text(
                                text = "CONFIRMAR DIRECCIÓN",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentStopListItem(
    index: Int,
    stop: RouteStop,
    totalCount: Int,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onRemove: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Reorder controls (Up/Down drag handle simulation)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    if (index > 0) {
                        IconButton(onClick = onMoveUp, modifier = Modifier.size(20.dp)) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = "Mover arriba",
                                tint = PetroleumBlue
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.DragHandle,
                        contentDescription = "Reordenar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    if (index < totalCount - 1) {
                        IconButton(onClick = onMoveDown, modifier = Modifier.size(20.dp)) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Mover abajo",
                                tint = PetroleumBlue
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "(${stop.stopNumber}) ${stop.address}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completada",
                    tint = SageGreen,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

package com.example.entregapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.entregapp.model.DeliveryRoute
import com.example.entregapp.model.RouteStop
import com.example.entregapp.model.StopStatus
import com.example.entregapp.ui.components.EntregappHeader
import com.example.entregapp.ui.theme.*

@Composable
fun CurrentRouteScreen(
    currentRoute: DeliveryRoute,
    onCompleteStop: (Int) -> Unit,
    onAddAddressClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Find active stop (first current or pending stop)
    val activeStopIndex = currentRoute.stops.indexOfFirst { it.status == StopStatus.CURRENT }
        .takeIf { it >= 0 }
        ?: currentRoute.stops.indexOfFirst { it.status == StopStatus.PENDING }.takeIf { it >= 0 }
        ?: (currentRoute.stops.size - 1)

    val activeStop = currentRoute.stops.getOrNull(activeStopIndex) ?: currentRoute.stops.first()
    var showRouteCompletedDialog by remember { mutableStateOf(false) }

    if (showRouteCompletedDialog) {
        AlertDialog(
            onDismissRequest = { showRouteCompletedDialog = false },
            title = { Text("¡Ruta Completada!", fontWeight = FontWeight.Bold) },
            text = { Text("Todas las entregas de '${currentRoute.title}' han sido confirmadas con éxito.") },
            confirmButton = {
                Button(
                    onClick = { showRouteCompletedDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PetroleumBlue)
                ) {
                    Text("Aceptar", color = Color.White)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        EntregappHeader(
            onLogoutClick = onLogoutClick,
            onProfileClick = onProfileClick
        )

        // Route Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "RECORRIDO ACTUAL",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        letterSpacing = 0.5.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "RUTA: ${currentRoute.title.uppercase()} - ${currentRoute.stops.size} PARADAS",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PetroleumBlue
                    )
                }

                IconButton(
                    onClick = onAddAddressClick,
                    modifier = Modifier
                        .background(SageGreenContainer, CircleShape)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddLocation,
                        contentDescription = "Agregar Dirección",
                        tint = SageGreen
                    )
                }
            }
        }

        // Main Map Canvas Container
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            SimulatedInteractiveMap(
                stops = currentRoute.stops,
                activeStopIndex = activeStopIndex,
                modifier = Modifier.fillMaxSize()
            )

            // Current Stop Floating Info Banner on Map
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(20.dp),
                color = SageGreen,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "En camino",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "EN CAMINO A STOP #${activeStop.stopNumber} (5 MIN)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Fixed Bottom Sheet Card (Stop Details & Confirm Delivery Button)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "STOP #${activeStop.stopNumber}",
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        color = PetroleumBlue
                    )

                    Surface(
                        color = if (activeStop.status == StopStatus.COMPLETED) StopCompletedBlue.copy(alpha = 0.15f) else SageGreenContainer,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (activeStop.status == StopStatus.COMPLETED) "ENTREGADO" else "EN PROGRESO",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (activeStop.status == StopStatus.COMPLETED) StopCompletedBlue else SageGreen,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Dirección",
                        tint = PetroleumBlue,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Dirección: ${activeStop.address}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (activeStop.reference.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = "Ubicación: ",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = activeStop.reference,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action Button: CONFIRMAR ENTREGA COMPLETADA
                Button(
                    onClick = {
                        onCompleteStop(activeStop.id)
                        if (activeStopIndex == currentRoute.stops.size - 1) {
                            showRouteCompletedDialog = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (activeStop.status == StopStatus.COMPLETED) StopCompletedBlue else SageGreen
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Confirmar",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (activeStop.status == StopStatus.COMPLETED) "ENTREGA REGISTRADA" else "CONFIRMAR ENTREGA COMPLETADA",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SimulatedInteractiveMap(
    stops: List<RouteStop>,
    activeStopIndex: Int,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    // Pulse animation for active stop marker
    val infiniteTransition = rememberInfiniteTransition(label = "markerPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFEFF3EE))
    ) {
        val w = size.width
        val h = size.height

        // 1. Draw Map Background Street Layout (Grid & Diagonal Roads)
        val roadPaintColor = MapRoadColor

        // Horizontal roads
        drawRoundRect(roadPaintColor, topLeft = Offset(0f, h * 0.25f), size = Size(w, 28f), cornerRadius = CornerRadius(4f, 4f))
        drawRoundRect(roadPaintColor, topLeft = Offset(0f, h * 0.55f), size = Size(w, 32f), cornerRadius = CornerRadius(4f, 4f))
        drawRoundRect(roadPaintColor, topLeft = Offset(0f, h * 0.80f), size = Size(w, 24f), cornerRadius = CornerRadius(4f, 4f))

        // Vertical roads
        drawRoundRect(roadPaintColor, topLeft = Offset(w * 0.25f, 0f), size = Size(28f, h), cornerRadius = CornerRadius(4f, 4f))
        drawRoundRect(roadPaintColor, topLeft = Offset(w * 0.52f, 0f), size = Size(32f, h), cornerRadius = CornerRadius(4f, 4f))
        drawRoundRect(roadPaintColor, topLeft = Offset(w * 0.78f, 0f), size = Size(26f, h), cornerRadius = CornerRadius(4f, 4f))

        // Diagonal road
        val diagPath = Path().apply {
            moveTo(w * 0.10f, h * 0.85f)
            lineTo(w * 0.85f, h * 0.15f)
        }
        drawPath(diagPath, color = roadPaintColor, style = Stroke(width = 30f))

        // 2. Draw Landmark Text Labels on Map
        val labelStyle = TextStyle(
            color = Color(0xFF495546),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )

        val landmarks = listOf(
            Triple("INICIO", w * 0.18f, h * 0.12f),
            Triple("BIBLIOTECA", w * 0.65f, h * 0.14f),
            Triple("ZONA DE COMIDA", w * 0.72f, h * 0.42f),
            Triple("DESTINO", w * 0.74f, h * 0.70f)
        )

        landmarks.forEach { (text, x, y) ->
            val result = textMeasurer.measure(text, style = labelStyle)
            drawText(
                textLayoutResult = result,
                topLeft = Offset(x - result.size.width / 2, y)
            )
        }

        // 3. Draw Route Path Polyline
        if (stops.isNotEmpty()) {
            val stopPoints = stops.map { Offset(it.mapXRatio * w, it.mapYRatio * h) }

            // Path up to active stop (Completed / Solid Blue)
            if (activeStopIndex > 0) {
                val completedPath = Path().apply {
                    moveTo(stopPoints[0].x, stopPoints[0].y)
                    for (i in 1..activeStopIndex.coerceAtMost(stopPoints.size - 1)) {
                        lineTo(stopPoints[i].x, stopPoints[i].y)
                    }
                }
                drawPath(
                    path = completedPath,
                    color = StopCompletedBlue,
                    style = Stroke(width = 8f, pathEffect = PathEffect.cornerPathEffect(10f))
                )
            }

            // Path from active stop onwards (Pending / Dashed Grey)
            if (activeStopIndex < stopPoints.size - 1) {
                val pendingPath = Path().apply {
                    moveTo(stopPoints[activeStopIndex].x, stopPoints[activeStopIndex].y)
                    for (i in (activeStopIndex + 1) until stopPoints.size) {
                        lineTo(stopPoints[i].x, stopPoints[i].y)
                    }
                }
                drawPath(
                    path = pendingPath,
                    color = Color(0xFF9EA79C),
                    style = Stroke(
                        width = 6f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f), 0f)
                    )
                )
            }
        }

        // 4. Draw Numbered Stop Markers (#1 to #10)
        stops.forEachIndexed { index, stop ->
            val center = Offset(stop.mapXRatio * w, stop.mapYRatio * h)
            val isCurrent = index == activeStopIndex
            val isCompleted = stop.status == StopStatus.COMPLETED

            val markerColor = when {
                isCompleted -> StopCompletedBlue
                isCurrent -> StopCurrentGreen
                else -> StopPendingGrey
            }

            // Pulse ring for active stop
            if (isCurrent) {
                drawCircle(
                    color = StopCurrentGreen.copy(alpha = 0.35f),
                    radius = 28f * pulseScale,
                    center = center
                )
            }

            // Marker Background Circle
            drawCircle(
                color = Color.White,
                radius = if (isCurrent) 22f else 18f,
                center = center
            )
            drawCircle(
                color = markerColor,
                radius = if (isCurrent) 19f else 15f,
                center = center
            )

            // Number Text inside marker
            val numText = "#${stop.stopNumber}"
            val numStyle = TextStyle(
                color = Color.White,
                fontSize = if (isCurrent) 12.sp else 10.sp,
                fontWeight = FontWeight.Bold
            )
            val textResult = textMeasurer.measure(numText, style = numStyle)
            drawText(
                textLayoutResult = textResult,
                topLeft = Offset(center.x - textResult.size.width / 2, center.y - textResult.size.height / 2)
            )
        }
    }
}

package com.example.entregapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.entregapp.model.ScreenTab
import com.example.entregapp.ui.theme.PetroleumBlue
import com.example.entregapp.ui.theme.SageGreen

@Composable
fun EntregappHeader(
    modifier: Modifier = Modifier,
    showUserActions: Boolean = true,
    onLogoutClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PetroleumBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = "Logo Entregapp",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        text = "ENTREGAPP",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        letterSpacing = 1.sp,
                        color = PetroleumBlue
                    )
                }
            }

            // User Actions
            if (showUserActions) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onProfileClick,
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil Repartidor",
                            tint = PetroleumBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onLogoutClick,
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PowerSettingsNew,
                            contentDescription = "Cerrar Sesión",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MiniRoutePreview(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFEBF0EC))
            .border(1.dp, Color(0xFFD0D7D2), RoundedCornerShape(10.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Background Grid Streets
            val roadColor = Color(0xFFD5DFD7)
            drawRect(roadColor, topLeft = Offset(0f, height * 0.4f), size = androidx.compose.ui.geometry.Size(width, 10f))
            drawRect(roadColor, topLeft = Offset(0f, height * 0.75f), size = androidx.compose.ui.geometry.Size(width, 8f))
            drawRect(roadColor, topLeft = Offset(width * 0.35f, 0f), size = androidx.compose.ui.geometry.Size(10f, height))
            drawRect(roadColor, topLeft = Offset(width * 0.7f, 0f), size = androidx.compose.ui.geometry.Size(8f, height))

            // Route Path
            val path = Path().apply {
                moveTo(width * 0.15f, height * 0.2f)
                lineTo(width * 0.35f, height * 0.4f)
                lineTo(width * 0.70f, height * 0.4f)
                lineTo(width * 0.70f, height * 0.75f)
                lineTo(width * 0.85f, height * 0.85f)
            }

            drawPath(
                path = path,
                color = PetroleumBlue,
                style = Stroke(width = 4f, pathEffect = PathEffect.cornerPathEffect(6f))
            )

            // Nodes / Stops on mini map
            val points = listOf(
                Offset(width * 0.15f, height * 0.2f),
                Offset(width * 0.35f, height * 0.4f),
                Offset(width * 0.70f, height * 0.4f),
                Offset(width * 0.70f, height * 0.75f),
                Offset(width * 0.85f, height * 0.85f)
            )

            points.forEachIndexed { index, point ->
                val nodeColor = if (index < 2) PetroleumBlue else SageGreen
                drawCircle(color = Color.White, radius = 7f, center = point)
                drawCircle(color = nodeColor, radius = 5f, center = point)
            }
        }
    }
}

@Composable
fun EntregappBottomNavigationBar(
    currentTab: ScreenTab,
    onTabSelected: (ScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentTab == ScreenTab.RUTAS,
            onClick = { onTabSelected(ScreenTab.RUTAS) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.RUTAS) Icons.Default.Home else Icons.Outlined.Home,
                    contentDescription = "Inicio"
                )
            },
            label = { Text("Inicio", fontWeight = if (currentTab == ScreenTab.RUTAS) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PetroleumBlue,
                selectedTextColor = PetroleumBlue,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        NavigationBarItem(
            selected = currentTab == ScreenTab.RECORRIDO,
            onClick = { onTabSelected(ScreenTab.RECORRIDO) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.RECORRIDO) Icons.Default.Map else Icons.Outlined.Map,
                    contentDescription = "Recorrido"
                )
            },
            label = { Text("Recorrido", fontWeight = if (currentTab == ScreenTab.RECORRIDO) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PetroleumBlue,
                selectedTextColor = PetroleumBlue,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        NavigationBarItem(
            selected = currentTab == ScreenTab.PERFIL,
            onClick = { onTabSelected(ScreenTab.PERFIL) },
            icon = {
                Icon(
                    imageVector = if (currentTab == ScreenTab.PERFIL) Icons.Default.Person else Icons.Outlined.Person,
                    contentDescription = "Perfil"
                )
            },
            label = { Text("Perfil", fontWeight = if (currentTab == ScreenTab.PERFIL) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PetroleumBlue,
                selectedTextColor = PetroleumBlue,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}

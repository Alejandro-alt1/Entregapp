package com.example.entregapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.entregapp.model.DeliveryRoute
import com.example.entregapp.model.MockData
import com.example.entregapp.model.RouteStop
import com.example.entregapp.model.ScreenTab
import com.example.entregapp.model.StopStatus
import com.example.entregapp.ui.components.EntregappBottomNavigationBar
import com.example.entregapp.ui.screens.*

@Composable
fun MainApp() {
    var isLoggedIn by remember { mutableStateOf(false) }
    var currentTab by remember { mutableStateOf(ScreenTab.RUTAS) }

    // Routes State
    var routes by remember { mutableStateOf(MockData.getInitialRoutes()) }
    var activeRouteId by remember { mutableStateOf("ruta_martes") }

    val activeRoute = routes.find { it.id == activeRouteId } ?: routes.first()

    // Handlers
    val handleLoginSuccess = {
        isLoggedIn = true
        currentTab = ScreenTab.RUTAS
    }

    val handleLogout = {
        isLoggedIn = false
        currentTab = ScreenTab.LOGIN
    }

    val handleSelectRoute = { route: DeliveryRoute ->
        activeRouteId = route.id
        currentTab = ScreenTab.RECORRIDO
    }

    val handleEditRoute = { route: DeliveryRoute ->
        activeRouteId = route.id
        currentTab = ScreenTab.AGREGAR_DIRECCION
    }

    val handleDeleteRoute = { routeToDelete: DeliveryRoute ->
        routes = routes.filter { it.id != routeToDelete.id }
        if (activeRouteId == routeToDelete.id && routes.isNotEmpty()) {
            activeRouteId = routes.first().id
        }
    }

    val handleAddNewRoute = {
        currentTab = ScreenTab.AGREGAR_DIRECCION
    }

    val handleCompleteStop = { stopId: Int ->
        routes = routes.map { route ->
            if (route.id == activeRouteId) {
                val updatedStops = route.stops.map { stop ->
                    if (stop.id == stopId) {
                        stop.copy(status = StopStatus.COMPLETED)
                    } else stop
                }

                // Check if next stop should be set to CURRENT
                val firstPendingIndex = updatedStops.indexOfFirst { it.status == StopStatus.PENDING }
                val finalStops = updatedStops.mapIndexed { index, stop ->
                    if (index == firstPendingIndex && stop.status != StopStatus.COMPLETED) {
                        stop.copy(status = StopStatus.CURRENT)
                    } else stop
                }

                route.copy(stops = finalStops)
            } else route
        }
    }

    val handleAddStop = { newAddress: String, referenceText: String ->
        routes = routes.map { route ->
            if (route.id == activeRouteId) {
                val nextNumber = route.stops.size + 1
                val newStop = RouteStop(
                    id = (System.currentTimeMillis() % 100000).toInt(),
                    stopNumber = nextNumber,
                    address = newAddress,
                    reference = referenceText,
                    locationName = "STOP #$nextNumber",
                    status = StopStatus.PENDING,
                    mapXRatio = (0.2f + (nextNumber * 0.07f)).coerceAtMost(0.85f),
                    mapYRatio = (0.3f + (nextNumber * 0.05f)).coerceAtMost(0.85f)
                )
                route.copy(
                    stops = route.stops + newStop,
                    stopsCount = route.stops.size + 1
                )
            } else route
        }
    }

    val handleRemoveStop = { stopIdToRemove: Int ->
        routes = routes.map { route ->
            if (route.id == activeRouteId) {
                val filtered = route.stops.filter { it.id != stopIdToRemove }
                val renumbered = filtered.mapIndexed { idx, stop ->
                    stop.copy(stopNumber = idx + 1)
                }
                route.copy(stops = renumbered, stopsCount = renumbered.size)
            } else route
        }
    }

    val handleMoveStopUp = { index: Int ->
        if (index > 0) {
            routes = routes.map { route ->
                if (route.id == activeRouteId) {
                    val list = route.stops.toMutableList()
                    val temp = list[index]
                    list[index] = list[index - 1]
                    list[index - 1] = temp
                    val renumbered = list.mapIndexed { idx, stop -> stop.copy(stopNumber = idx + 1) }
                    route.copy(stops = renumbered)
                } else route
            }
        }
    }

    val handleMoveStopDown = { index: Int ->
        routes = routes.map { route ->
            if (route.id == activeRouteId && index < route.stops.size - 1) {
                val list = route.stops.toMutableList()
                val temp = list[index]
                list[index] = list[index + 1]
                list[index + 1] = temp
                val renumbered = list.mapIndexed { idx, stop -> stop.copy(stopNumber = idx + 1) }
                route.copy(stops = renumbered)
            } else route
        }
    }

    if (!isLoggedIn) {
        LoginScreen(onLoginSuccess = handleLoginSuccess)
    } else {
        Scaffold(
            bottomBar = {
                EntregappBottomNavigationBar(
                    currentTab = currentTab,
                    onTabSelected = { selectedTab -> currentTab = selectedTab }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            val modifier = Modifier.padding(innerPadding)

            when (currentTab) {
                ScreenTab.LOGIN -> {
                    LoginScreen(onLoginSuccess = handleLoginSuccess)
                }
                ScreenTab.RUTAS -> {
                    RoutesScreen(
                        routesList = routes,
                        onSelectRoute = handleSelectRoute,
                        onEditRoute = handleEditRoute,
                        onDeleteRoute = handleDeleteRoute,
                        onAddNewRoute = handleAddNewRoute,
                        onLogoutClick = handleLogout,
                        onProfileClick = { currentTab = ScreenTab.PERFIL },
                        modifier = modifier
                    )
                }
                ScreenTab.RECORRIDO -> {
                    CurrentRouteScreen(
                        currentRoute = activeRoute,
                        onCompleteStop = handleCompleteStop,
                        onAddAddressClick = { currentTab = ScreenTab.AGREGAR_DIRECCION },
                        onLogoutClick = handleLogout,
                        onProfileClick = { currentTab = ScreenTab.PERFIL },
                        modifier = modifier
                    )
                }
                ScreenTab.AGREGAR_DIRECCION -> {
                    AddAddressScreen(
                        currentStops = activeRoute.stops,
                        onAddStop = handleAddStop,
                        onRemoveStop = handleRemoveStop,
                        onMoveStopUp = handleMoveStopUp,
                        onMoveStopDown = handleMoveStopDown,
                        onBackClick = { currentTab = ScreenTab.RUTAS },
                        onLogoutClick = handleLogout,
                        onProfileClick = { currentTab = ScreenTab.PERFIL },
                        modifier = modifier
                    )
                }
                ScreenTab.PERFIL -> {
                    ProfileScreen(
                        onLogoutClick = handleLogout,
                        modifier = modifier
                    )
                }
            }
        }
    }
}

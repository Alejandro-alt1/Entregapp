package com.example.entregapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.entregapp.model.MockData
import com.example.entregapp.ui.MainApp
import com.example.entregapp.ui.screens.AddAddressScreen
import com.example.entregapp.ui.screens.CurrentRouteScreen
import com.example.entregapp.ui.screens.LoginScreen
import com.example.entregapp.ui.screens.RoutesScreen
import com.example.entregapp.ui.theme.EntregappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EntregappTheme {
                MainApp()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    EntregappTheme {
        LoginScreen(onLoginSuccess = {})
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RoutesScreenPreview() {
    EntregappTheme {
        RoutesScreen(
            routesList = MockData.getInitialRoutes(),
            onSelectRoute = {},
            onEditRoute = {},
            onDeleteRoute = {},
            onAddNewRoute = {},
            onLogoutClick = {},
            onProfileClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CurrentRouteScreenPreview() {
    EntregappTheme {
        CurrentRouteScreen(
            currentRoute = MockData.getInitialRoutes()[1],
            onCompleteStop = {},
            onAddAddressClick = {},
            onLogoutClick = {},
            onProfileClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddAddressScreenPreview() {
    EntregappTheme {
        AddAddressScreen(
            currentStops = MockData.getInitialRoutes()[1].stops,
            onAddStop = { _, _ -> },
            onRemoveStop = {},
            onMoveStopUp = {},
            onMoveStopDown = {},
            onBackClick = {},
            onLogoutClick = {},
            onProfileClick = {}
        )
    }
}

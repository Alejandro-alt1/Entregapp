package com.example.entregapp.model

enum class StopStatus {
    COMPLETED,
    CURRENT,
    PENDING
}

data class RouteStop(
    val id: Int,
    val stopNumber: Int,
    val address: String,
    val reference: String = "",
    val locationName: String = "",
    val status: StopStatus = StopStatus.PENDING,
    val mapXRatio: Float = 0.5f,
    val mapYRatio: Float = 0.5f
)

data class DeliveryRoute(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val estimatedTime: String,
    val stopsCount: Int,
    val tag: String? = null,
    val stops: List<RouteStop>
)

enum class ScreenTab {
    LOGIN,
    RUTAS,
    RECORRIDO,
    AGREGAR_DIRECCION,
    PERFIL
}

object MockData {
    fun getInitialRoutes(): List<DeliveryRoute> {
        val campusStops = listOf(
            RouteStop(1, 1, "Calle Universidad 123", "Acceso Principal Campus A", "INICIO", StopStatus.COMPLETED, 0.22f, 0.18f),
            RouteStop(2, 2, "Av. Reforma 45", "Frente a Edificio Administrativo", "REFORMA 45", StopStatus.COMPLETED, 0.18f, 0.45f),
            RouteStop(3, 3, "Paseo de la Reforma 67", "Entrada Estacionamiento B", "REFORMA 67", StopStatus.COMPLETED, 0.25f, 0.60f),
            RouteStop(4, 4, "Calle Universidad 456, Edif. A", "Frente a la cafetería", "STOP #4", StopStatus.CURRENT, 0.42f, 0.52f),
            RouteStop(5, 5, "Av. Universitaria 89", "Edificio de Investigaciones", "BIBLIOTECA", StopStatus.PENDING, 0.58f, 0.20f),
            RouteStop(6, 6, "Av. Central 102", "Detrás de Biblioteca Central", "FACULTAD 6", StopStatus.PENDING, 0.68f, 0.28f),
            RouteStop(7, 7, "Plaza Gastronómica Sur", "Kiosco de Envíos #2", "ZONA DE COMIDA", StopStatus.PENDING, 0.70f, 0.48f),
            RouteStop(8, 8, "Av. Universidad 500", "Puerta 4 Campus B", "PUERTA 8", StopStatus.PENDING, 0.55f, 0.42f),
            RouteStop(9, 9, "Piso 3, Torre Innovación", "Recepción de Paquetes", "TORRE #9", StopStatus.PENDING, 0.45f, 0.32f),
            RouteStop(10, 10, "Circuito Exterior 777", "Almacén Central de Reparto", "DESTINO", StopStatus.PENDING, 0.72f, 0.65f)
        )

        val mondayStops = listOf(
            RouteStop(1, 1, "Calle Universidad 123", "Campus A", "INICIO", StopStatus.COMPLETED, 0.2f, 0.2f),
            RouteStop(2, 2, "Av. Reforma 45", "Edificio Norte", "STOP #2", StopStatus.CURRENT, 0.4f, 0.35f),
            RouteStop(3, 3, "Paseo de la Reforma 67", "Campus B", "STOP #3", StopStatus.PENDING, 0.5f, 0.5f),
            RouteStop(4, 4, "Calle Insurgentes 88", "Campus Sur", "STOP #4", StopStatus.PENDING, 0.6f, 0.65f),
            RouteStop(5, 5, "Av. Hidalgo 300", "Puerta 2", "STOP #5", StopStatus.PENDING, 0.7f, 0.75f),
            RouteStop(6, 6, "Calle Juárez 12", "Edificio C", "STOP #6", StopStatus.PENDING, 0.8f, 0.8f),
            RouteStop(7, 7, "Av. Universidad 990", "Almacén Sur", "DESTINO", StopStatus.PENDING, 0.85f, 0.88f)
        )

        val expressStops = listOf(
            RouteStop(1, 1, "Campus 3 - Puerta Emergencia", "Caseta 1", "URGENTE 1", StopStatus.CURRENT, 0.3f, 0.3f),
            RouteStop(2, 2, "Edificio de Gobierno - Piso 2", "Ventanilla 4", "URGENTE 2", StopStatus.PENDING, 0.5f, 0.6f),
            RouteStop(3, 3, "Centro Médico Universitario", "Laboratorio Central", "DESTINO", StopStatus.PENDING, 0.8f, 0.7f)
        )

        return listOf(
            DeliveryRoute(
                id = "ruta_lunes",
                title = "Ruta Lunes (7 Paradas)",
                subtitle = "Lunes 12 de Oct. | Campus A -> Sur",
                description = "Paradas de Variado y campus a - sur gamtos...",
                estimatedTime = "Total: Aprox. 1.5 hrs",
                stopsCount = 7,
                tag = null,
                stops = mondayStops
            ),
            DeliveryRoute(
                id = "ruta_martes",
                title = "Ruta Martes (10 Paradas)",
                subtitle = "Martes 13 de Oct. | Campus B -> Norte",
                description = "Paradas de Variado y campus B - norte taerte...",
                estimatedTime = "Total: Aprox. 2.0 hrs",
                stopsCount = 10,
                tag = null,
                stops = campusStops
            ),
            DeliveryRoute(
                id = "ruta_express",
                title = "Ruta Express (3 Paradas)",
                subtitle = "Emergencias",
                description = "Paradas de Variado y campus 3 - Emergencia...",
                estimatedTime = "Total: Aprox. 45 min",
                stopsCount = 3,
                tag = "Emergencias",
                stops = expressStops
            )
        )
    }
}

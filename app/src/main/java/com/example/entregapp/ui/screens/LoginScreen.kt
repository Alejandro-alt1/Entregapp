package com.example.entregapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.entregapp.ui.theme.PetroleumBlue
import com.example.entregapp.ui.theme.SageGreen

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var emailOrUser by remember { mutableStateOf("repartidor@entregapp.com") }
    var password by remember { mutableStateOf("123456") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberSession by remember { mutableStateOf(true) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Logo & Title Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PetroleumBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text(
                    text = "ENTREGAPP",
                    fontWeight = FontWeight.Black,
                    fontSize = 32.sp,
                    letterSpacing = 2.sp,
                    color = PetroleumBlue
                )
            }

            Text(
                text = "Acceso para Repartidores",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 6.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Delivery Graphic Illustration
            DeliveryTruckIllustration(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Username or Email Field
            OutlinedTextField(
                value = emailOrUser,
                onValueChange = { emailOrUser = it },
                label = { Text("Usuario o Correo") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Usuario",
                        tint = PetroleumBlue
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Contraseña",
                        tint = PetroleumBlue
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Remember Session & Forgot Password Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = rememberSession,
                        onCheckedChange = { rememberSession = it },
                        colors = CheckboxDefaults.colors(checkedColor = PetroleumBlue)
                    )
                    Text(
                        text = "Recordar sesión",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                TextButton(onClick = { /* Handle forgot password */ }) {
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PetroleumBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button: INICIAR SESIÓN
            Button(
                onClick = onLoginSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PetroleumBlue)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "INICIAR SESIÓN",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Entrar",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Footer Texts
            Text(
                text = "Uso exclusivo para personal de reparto",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿No tienes cuenta? ",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(
                    onClick = { /* Contact admin */ },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Solicita acceso aquí",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PetroleumBlue
                    )
                }
            }

            TextButton(
                onClick = { /* Support */ },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Soporte técnico",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DeliveryTruckIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Ground / Road line
        drawLine(
            color = Color(0xFFD0D6CF),
            start = Offset(width * 0.1f, height * 0.85f),
            end = Offset(width * 0.9f, height * 0.85f),
            strokeWidth = 4f
        )

        // Green Delivery Van Body
        val truckColor = Color(0xFF4A7C59)
        val truckDark = Color(0xFF32533B)
        val wheelColor = Color(0xFF333333)

        // Main cargo box
        drawRoundRect(
            color = truckColor,
            topLeft = Offset(width * 0.35f, height * 0.35f),
            size = Size(width * 0.45f, height * 0.40f),
            cornerRadius = CornerRadius(12f, 12f)
        )

        // Van Cabin (front)
        drawRoundRect(
            color = truckColor,
            topLeft = Offset(width * 0.70f, height * 0.45f),
            size = Size(width * 0.18f, height * 0.30f),
            cornerRadius = CornerRadius(10f, 10f)
        )

        // Cabin Window
        drawRoundRect(
            color = Color(0xFFD4E6F1),
            topLeft = Offset(width * 0.75f, height * 0.48f),
            size = Size(width * 0.10f, height * 0.14f),
            cornerRadius = CornerRadius(4f, 4f)
        )

        // Cargo box lines / detail
        drawLine(
            color = truckDark,
            start = Offset(width * 0.42f, height * 0.37f),
            end = Offset(width * 0.42f, height * 0.73f),
            strokeWidth = 3f
        )

        // Wheels
        drawCircle(color = wheelColor, radius = height * 0.11f, center = Offset(width * 0.45f, height * 0.78f))
        drawCircle(color = Color.LightGray, radius = height * 0.05f, center = Offset(width * 0.45f, height * 0.78f))

        drawCircle(color = wheelColor, radius = height * 0.11f, center = Offset(width * 0.76f, height * 0.78f))
        drawCircle(color = Color.LightGray, radius = height * 0.05f, center = Offset(width * 0.76f, height * 0.78f))

        // Courier Standing Figure
        val skinColor = Color(0xFFE8BEAC)
        val shirtColor = PetroleumBlue
        val capColor = SageGreen

        // Courier Head & Cap
        drawCircle(color = skinColor, radius = height * 0.07f, center = Offset(width * 0.22f, height * 0.38f))
        drawArc(
            color = capColor,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(width * 0.18f, height * 0.30f),
            size = Size(width * 0.08f, height * 0.12f)
        )

        // Courier Body
        drawRoundRect(
            color = shirtColor,
            topLeft = Offset(width * 0.19f, height * 0.45f),
            size = Size(width * 0.06f, height * 0.25f),
            cornerRadius = CornerRadius(6f, 6f)
        )

        // Cardboard Box carried by courier
        drawRoundRect(
            color = Color(0xFFC89658),
            topLeft = Offset(width * 0.24f, height * 0.45f),
            size = Size(width * 0.10f, height * 0.15f),
            cornerRadius = CornerRadius(4f, 4f)
        )
    }
}

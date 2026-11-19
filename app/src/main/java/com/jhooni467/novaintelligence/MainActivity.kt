package com.jhooni467.novaintelligence

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhooni467.novaintelligence.ui.theme.NovaColors
import com.jhooni467.novaintelligence.ui.theme.NovaTheme
import kotlinx.coroutines.launch

private data class ChatMessage(
    val role: String,
    val content: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NovaTheme {
                NovaHomeScreen()
            }
        }
    }
}

@Composable
private fun NovaHomeScreen() {
    val context = LocalContext.current
    val preferences = remember {
        context.getSharedPreferences("nova_settings", Context.MODE_PRIVATE)
    }
    var spaceUrl by remember {
        mutableStateOf(preferences.getString("space_url", "") ?: "")
    }
    var prompt by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    var showConnectionDialog by remember { mutableStateOf(false) }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    val scope = rememberCoroutineScope()

    fun saveSpaceUrl(candidate: String) {
        try {
            val normalized = NovaSpaceClient.normalizeBaseUrl(candidate)
            spaceUrl = normalized
            preferences.edit().putString("space_url", normalized).apply()
            showConnectionDialog = false
            statusMessage = "Dirección guardada. Nova Core se comprobará al enviar tu primer mensaje."
        } catch (error: IllegalArgumentException) {
            statusMessage = error.message ?: "Revisa la dirección del Space."
        }
    }

    fun sendMessage() {
        val messageText = prompt.trim()
        if (messageText.isEmpty() || isSending) return

        if (spaceUrl.isBlank()) {
            statusMessage = "Primero configura la conexión de Nova Core."
            showConnectionDialog = true
            return
        }

        val previousHistory = messages.map {
            NovaSpaceMessage(role = it.role, content = it.content)
        }
        val userMessage = ChatMessage(role = "user", content = messageText)
        messages.add(userMessage)
        prompt = ""
        statusMessage = ""
        isSending = true

        scope.launch {
            try {
                val answer = NovaSpaceClient.sendMessage(
                    spaceUrl = spaceUrl,
                    message = messageText,
                    history = previousHistory
                )
                messages.add(ChatMessage(role = "assistant", content = answer))
            } catch (error: Exception) {
                messages.remove(userMessage)
                prompt = messageText
                statusMessage = error.message
                    ?: "No se pudo contactar con Nova Core. Revisa tu conexión e inténtalo de nuevo."
            } finally {
                isSending = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        ColorTop,
                        NovaColors.Background,
                        NovaColors.Background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            NovaHeader(
                isConfigured = spaceUrl.isNotBlank(),
                onConfigure = { showConnectionDialog = true }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = if (messages.isEmpty()) Arrangement.Center else Arrangement.Top
            ) {
                if (messages.isEmpty()) {
                    NovaOrb()

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "¿Qué hacemos hoy?",
                        color = NovaColors.TextPrimary,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Cuéntame qué tienes en mente.",
                        color = NovaColors.PurpleBright,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Podemos resolver algo, darle forma a una idea o descubrir algo nuevo.",
                        color = NovaColors.TextSecondary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 21.sp,
                        modifier = Modifier.fillMaxWidth(0.88f)
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Button(
                        onClick = { showConnectionDialog = true },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = if (spaceUrl.isBlank()) "Configurar motor de IA" else "Cambiar conexión de IA"
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SuggestionCard(
                            title = "Crear algo",
                            description = "Ideas y proyectos",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                prompt = "Ayúdame a desarrollar una idea nueva."
                                statusMessage = ""
                            }
                        )

                        SuggestionCard(
                            title = "Descubrir",
                            description = "Música y contenido",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                prompt = "Quiero descubrir música y contenido nuevo."
                                statusMessage = ""
                            }
                        )
                    }
                } else {
                    messages.forEach { message ->
                        MessageBubble(message = message)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (isSending) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "Nova está pensando…",
                                color = NovaColors.PurpleBright,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            Composer(
                value = prompt,
                isSending = isSending,
                onValueChange = {
                    prompt = it
                    statusMessage = ""
                },
                onSend = { sendMessage() }
            )

            if (statusMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = statusMessage,
                    color = NovaColors.PurpleBright,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "NOVA INTELLIGENCE  •  0.2.0",
                color = NovaColors.TextSecondary.copy(alpha = 0.62f),
                fontSize = 9.sp,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (showConnectionDialog) {
        ConnectionDialog(
            currentUrl = spaceUrl,
            onDismiss = { showConnectionDialog = false },
            onSave = { saveSpaceUrl(it) }
        )
    }
}

@Composable
private fun NovaHeader(
    isConfigured: Boolean,
    onConfigure: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            NovaColors.Purple,
                            Color(0xFF6541B5)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✦",
                color = Color(0xFF171329),
                fontSize = 25.sp
            )
        }

        Spacer(modifier = Modifier.size(11.dp))

        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(
                text = "NOVA",
                color = NovaColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )
            Text(
                text = "INTELLIGENCE",
                color = NovaColors.TextSecondary,
                fontSize = 8.sp,
                letterSpacing = 2.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Surface(
            modifier = Modifier.clickable(onClick = onConfigure),
            color = NovaColors.SurfaceRaised,
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, NovaColors.Border)
        ) {
            Text(
                text = if (isConfigured) "Motor configurado" else "Conectar IA",
                color = NovaColors.PurpleBright,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp)
            )
        }
    }
}

@Composable
private fun NovaOrb() {
    Box(
        modifier = Modifier
            .size(82.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFDBCEFF),
                        NovaColors.Purple,
                        Color(0xFF593B9B)
                    )
                )
            )
            .border(1.dp, NovaColors.PurpleBright.copy(alpha = 0.45f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "✦",
            color = Color(0xFF211638),
            fontSize = 38.sp
        )
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isUser = message.role == "user"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(if (isUser) 0.86f else 0.96f),
            color = if (isUser) NovaColors.Purple.copy(alpha = 0.22f) else NovaColors.Surface,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(
                1.dp,
                if (isUser) NovaColors.Purple.copy(alpha = 0.45f) else NovaColors.Border
            )
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                if (!isUser) {
                    Text(
                        text = "NOVA",
                        color = NovaColors.PurpleBright,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.8.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
                Text(
                    text = message.content,
                    color = NovaColors.TextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
            }
        }
    }
}

@Composable
private fun ConnectionDialog(
    currentUrl: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var input by remember(currentUrl) { mutableStateOf(currentUrl) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Conectar Nova Core",
                color = NovaColors.TextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Pega la dirección HTTPS de tu Hugging Face Space con ZeroGPU. La conexión se probará cuando envíes el primer mensaje.",
                    color = NovaColors.TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("URL del Space") },
                    placeholder = { Text("https://usuario-nova-core.hf.space") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )
                Text(
                    text = "Solo se aceptan direcciones oficiales terminadas en .hf.space. No pegues contraseñas ni tokens aquí.",
                    color = NovaColors.TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(input) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        containerColor = NovaColors.Surface,
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
private fun SuggestionCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        color = NovaColors.Surface.copy(alpha = 0.92f),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, NovaColors.Border)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 15.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = title,
                color = NovaColors.TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                color = NovaColors.TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun Composer(
    value: String,
    isSending: Boolean,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        color = NovaColors.Surface,
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(1.dp, NovaColors.Border)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = !isSending,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                textStyle = TextStyle(
                    color = NovaColors.TextPrimary,
                    fontSize = 14.sp
                ),
                cursorBrush = SolidColor(NovaColors.Purple),
                maxLines = 4,
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = if (isSending) "Nova está respondiendo…" else "Escribe tu mensaje…",
                                color = NovaColors.TextSecondary,
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        if (value.isBlank() || isSending) NovaColors.SurfaceRaised
                        else NovaColors.Purple
                    )
                    .clickable(
                        enabled = value.isNotBlank() && !isSending,
                        onClick = onSend
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isSending) "…" else "↑",
                    color = if (value.isBlank() || isSending) NovaColors.TextSecondary
                    else NovaColors.Background,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private val ColorTop = Color(0xFF17102A)

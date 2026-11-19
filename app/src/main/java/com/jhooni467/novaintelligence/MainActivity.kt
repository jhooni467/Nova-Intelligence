package com.jhooni467.novaintelligence

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.verticalScroll
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import com.jhooni467.novaintelligence.ui.theme.NovaColors
import com.jhooni467.novaintelligence.ui.theme.NovaTheme

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
    var prompt by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("") }

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
            NovaHeader()

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                NovaOrb()

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Hola, soy Nova.",
                    color = NovaColors.TextPrimary,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "¿Qué exploramos hoy?",
                    color = NovaColors.PurpleBright,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Tu espacio para conversar, crear ideas y descubrir cosas nuevas.",
                    color = NovaColors.TextSecondary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 21.sp,
                    modifier = Modifier.fillMaxWidth(0.88f)
                )

                Spacer(modifier = Modifier.height(30.dp))

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
            }

            Composer(
                value = prompt,
                onValueChange = {
                    prompt = it
                    statusMessage = ""
                },
                onSend = {
                    statusMessage =
                        "Nova Core todavía no está conectado. La conversación real llegará en una próxima etapa."
                }
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
                text = "NOVA INTELLIGENCE  •  0.1.0",
                color = NovaColors.TextSecondary.copy(alpha = 0.62f),
                fontSize = 9.sp,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun NovaHeader() {
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
            color = NovaColors.SurfaceRaised,
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, NovaColors.Border)
        ) {
            Text(
                text = "0.1.0",
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
                                text = "Pregúntale algo a Nova...",
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
                        if (value.isBlank()) NovaColors.SurfaceRaised
                        else NovaColors.Purple
                    )
                    .clickable(enabled = value.isNotBlank(), onClick = onSend),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "↑",
                    color = if (value.isBlank()) NovaColors.TextSecondary
                    else NovaColors.Background,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private val ColorTop = Color(0xFF17102A)

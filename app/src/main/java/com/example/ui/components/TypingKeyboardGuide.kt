package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.SecondaryCyan

@Composable
fun TypingKeyboardGuide(
    activeKey: Char? = null,
    modifier: Modifier = Modifier
) {
    val row1 = listOf('q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p')
    val row2 = listOf('a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', ';')
    val row3 = listOf('z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.', '/')

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⌨️ Finger Placement Guide",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Left Hand: A S D F (Pinky -> Index) | Right Hand: J K L ; (Index -> Pinky)",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Keyboard rows
            KeyboardRow(keys = row1, activeKey = activeKey)
            Spacer(modifier = Modifier.height(4.dp))
            KeyboardRow(keys = row2, activeKey = activeKey, isHomeRow = true)
            Spacer(modifier = Modifier.height(4.dp))
            KeyboardRow(keys = row3, activeKey = activeKey)
        }
    }
}

@Composable
private fun KeyboardRow(
    keys: List<Char>,
    activeKey: Char?,
    isHomeRow: Boolean = false
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        keys.forEach { key ->
            val isActive = activeKey != null && activeKey.lowercaseChar() == key
            val isHomeIndex = isHomeRow && (key == 'f' || key == 'j')

            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .width(28.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        when {
                            isActive -> SecondaryCyan
                            isHomeIndex -> PrimaryIndigo.copy(alpha = 0.4f)
                            else -> Color(0xFF334155)
                        }
                    )
                    .border(
                        width = if (isHomeIndex) 1.5.dp else 0.5.dp,
                        color = if (isHomeIndex) PrimaryIndigo else Color.Transparent,
                        shape = RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = key.toString().uppercase(),
                    fontWeight = if (isActive || isHomeIndex) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 12.sp,
                    color = if (isActive) Color.Black else Color.White
                )
            }
        }
    }
}

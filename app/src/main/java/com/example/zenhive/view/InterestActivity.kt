package com.example.zenhive.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zenhive.R

class InterestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PreferenceScreen()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreferenceScreen() {
    val categories = mapOf(
        "Knowledge" to listOf("Physics", "Space", "Language", "History", "Philosophy", "AI/ML", "Quantum", "Stock Market"),
        "Wellness" to listOf("Fitness", "Yoga", "Journaling", "Mindfulness", "Productivity"),
        "Hanging Out" to listOf("Travel", "Cafe-Hopping", "Board Games", "Minimalism"),
        "Social/Community" to listOf("Activism", "Volunteering", "LGBTQ+", "Politics", "Public Speaking")
    )

    // Use a Set<String> to hold selected interests and trigger recomposition properly
    val selectedInterests = remember { mutableStateOf(setOf<String>()) }
    val scrollState = rememberScrollState()

    // Button press scale animation state
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 300f),
        label = "buttonScaleAnimation"
    )

    Scaffold(
        containerColor = colorResource(id = R.color.loginbgg),
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { /* Handle navigation */ },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.black)),
                    modifier = Modifier
                        .width(200.dp)
                        .height(55.dp)
                        .scale(scale)
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Find People", color = Color.White)
                }
            }
        }
    ) { paddingValues ->  // Fixed: Renamed padding to paddingValues to avoid naming conflict
        Column(
            modifier = Modifier
                .padding(paddingValues)  // Fixed: Using the paddingValues from Scaffold
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            Text(
                text = "Set up your profile",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = colorResource(id = R.color.white)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Pick what you're into and find your kind of people",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            Spacer(Modifier.height(24.dp))

            categories.forEach { (category, interests) ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.preferencebg)),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = colorResource(id = R.color.payalo)
                        )
                        FlowRow(
                            modifier = Modifier.padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            interests.forEach { interest ->
                                val isSelected = selectedInterests.value.contains(interest)
                                val color = getColorForInterest(interest, isSelected)
                                AssistChip(
                                    onClick = {
                                        selectedInterests.value = if (isSelected) {
                                            selectedInterests.value - interest
                                        } else {
                                            selectedInterests.value + interest
                                        }
                                    },
                                    label = { Text(interest) },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = color,
                                        labelColor = Color.Black
                                    ),
                                    elevation = AssistChipDefaults.assistChipElevation(
                                        elevation = 4.dp,
                                        pressedElevation = 8.dp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))

            // For debugging: show selected interests below
            Text(
                text = "Selected: ${selectedInterests.value.joinToString(", ")}",
                color = Color.LightGray,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable

fun getColorForInterest(interest: String, selected: Boolean): Color {
    return if (selected) {
        when (interest) {
            in listOf("Space", "AI/ML", "Stock Market","Physics","Language","History","Philosophy","Quantum") -> Color(0xFF2196F3) // blue
            in listOf("Fitness","Yoga", "Productivity", "Mindfulness", "Journaling") -> Color(0xFF4CAF50) // green
            in listOf("Cafe-Hopping", "Minimalism", "Travel", "Board Games") -> Color(0xFF03A9F4) // light blue
            in listOf("Activism", "Volunteering", "Politics","LGBTQ+","Public Speaking",) -> Color(0xFFF44336) // red
            else -> Color.Gray
        }
    } else {
        Color.LightGray
    }
}


@Preview(showBackground = true)
@Composable
fun PreferenceScreenPreview() {
    MaterialTheme {
        PreferenceScreen()
    }
}
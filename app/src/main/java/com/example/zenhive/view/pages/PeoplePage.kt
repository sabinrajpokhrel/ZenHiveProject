package com.example.zenhive.view.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zenhive.R
import com.example.zenhive.ui.components.PeopleSuggestionCard


@Composable
fun PeoplePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())  // Enables vertical scrolling
    ) {
        Text("People picked for you", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Browse through people you find interesting and add them to your hive", color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))

        PeopleSuggestionCard(
            name = "John",
            age = 35,
            gender = "Male",
            description = "Join the hive to get Farming and Stock-Market Analysis 🌾💹",
            interests = listOf("Farming", "Stocks", "Philosophy", "Comedy"),
            interestColors = listOf(Color(0xFFB3E283), Color(0xFF89CFF0), Color(0xFFF6D186), Color(0xFFFFD580)),
            avatarRes = R.drawable.person2
        )

        PeopleSuggestionCard(
            name = "Sofie",
            age = 19,
            gender = "Female",
            description = "Feel free to join my hive. I share fashion insights there! ✨",
            interests = listOf("Beauty", "Fashion", "Books", "AI/ML"),
            interestColors = listOf(Color(0xFFFFB6C1), Color(0xFFFF6961), Color(0xFFF6D186), Color(0xFF89CFF0)),
            avatarRes = R.drawable.person1
        )

        PeopleSuggestionCard(
            name = "Max",
            age = 20,
            gender = "Male",
            description = "I do games and also live stream. Hop in and we’ll play together 🎮",
            interests = listOf("Music", "Culture", "E-Sports", "Plants"),
            interestColors = listOf(Color(0xFF89CFF0), Color(0xFFF6D186), Color(0xFFFF6961), Color(0xFFB3E283)),
            avatarRes = R.drawable.person3
        )

        PeopleSuggestionCard(
            name = "Emma",
            age = 28,
            gender = "Female",
            description = "Food blogger sharing healthy recipes and restaurant experiences. 🍽️✨",
            interests = listOf("Food", "Travel", "Photography", "Yoga"),
            interestColors = listOf(Color(0xFFFFE066), Color(0xFF89CFF0), Color(0xFFF6D186), Color(0xFFB3E283)),
            avatarRes = R.drawable.person4
        )

        PeopleSuggestionCard(
            name = "Siddika",
            age = 20,
            gender = "Female",
            description = "Fitness enthusiast and motivational coach. Let's hustle together! 💪🔥",
            interests = listOf("Fitness", "Motivation", "Hiking", "Podcasts"),
            interestColors = listOf(Color(0xFFF6D186), Color(0xFFFF6961), Color(0xFF89CFF0), Color(0xFFFFD580)),
            avatarRes = R.drawable.person5
        )

        PeopleSuggestionCard(
            name = "Lily",
            age = 24,
            gender = "Female",
            description = "Artist and illustrator sharing sketches and creative tutorials. 🎨🖌️",
            interests = listOf("Art", "Illustration", "Design", "Cats"),
            interestColors = listOf(Color(0xFFFFB6C1), Color(0xFFF6D186), Color(0xFFB3E283), Color(0xFFFFE066)),
            avatarRes = R.drawable.person6
        )
    }
}


package com.example.zenhive.view.pages

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zenhive.R
import com.example.zenhive.ui.components.HiveCard

@Composable
fun FeaturedHivesPage() {
    val scrollState = rememberScrollState()
    var activePage by remember { mutableStateOf("hives") }
    Row(
        modifier = Modifier
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = { activePage = "hives" },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (activePage == "hives") Color(0xFFFBC125) else Color.DarkGray,
                contentColor = if (activePage == "hives") Color.Black else Color.White
            ),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text("Explore Hives")
        }
        Button(
            onClick = { activePage = "people"},
            colors = ButtonDefaults.buttonColors(
                containerColor = if (activePage == "people") Color(0xFFFBC125) else Color.DarkGray,
                contentColor = if (activePage == "people") Color.Black else Color.White
            ),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text("Meet People")
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    if (activePage == "hives") {
        Text(
            "Discover, Connect & Grow",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "Find curated sessions and people that match your curiosity.",
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column {
            HiveCard(
                category = "ui/ux",
                title = "How do you create a design hypothesis?",
                creators = listOf(R.drawable.person1, R.drawable.person2),
                membersCount = 31,
                commentsCount = 12
            )
            HiveCard(
                category = "Stock Market",
                title = "NEPSE Technical Analysis Guide",
                creators = listOf(R.drawable.person3, R.drawable.person4),
                membersCount = 23,
                commentsCount = 6
            )
        }
    } else {

        PeoplePage()
    }
}
package com.example.zenhive.view.pages

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.zenhive.ui.components.HiveCard
import com.example.zenhive.viewmodel.HiveViewModel
import com.example.zenhive.R // 👈 Needed for placeholder image

@Composable
fun FeaturedHivesPage() {
    val scrollState = rememberScrollState()
    var activePage by remember { mutableStateOf("hives") }
    val hiveViewModel: HiveViewModel = viewModel()
    val liveHives by hiveViewModel.liveHives.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.horizontalScroll(scrollState),
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
                onClick = { activePage = "people" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activePage == "people") Color(0xFFFBC125) else Color.DarkGray,
                    contentColor = if (activePage == "people") Color.Black else Color.White
                ),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text("Meet People")
            }
        }

        if (activePage == "hives") {
            Text(
                "Discover, Connect & Grow",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Find curated sessions and people that match your curiosity.",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(liveHives.sortedByDescending { it.timestamp }) { hive ->
                HiveCard(
                        title = hive.title,
                        creator = listOf(R.drawable.person), // Placeholder image // change this later
                        membersCount = hive.participants.size
                    )
                }
            }
        } else {
            PeoplePage()
        }
    }
}

package com.example.zenhive.view.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.zenhive.ui.components.LogoButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationPage() {
    Scaffold(
        containerColor = Color(0xFF1C1C1C),
        floatingActionButton = {
            LogoButton(
                onExploreClick = { /* Navigate to Featured Hives */ }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            // Notification content will go here
            // You can add notification items later
        }
    }
}

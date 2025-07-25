package com.example.zenhive.view.pages

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.zenhive.model.NotificationModel
import com.example.zenhive.repository.NotificationRepositoryImplementation
import com.example.zenhive.ui.components.LogoButton
import com.example.zenhive.view.HiveGroupCallActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationPage(onNavigateToFeaturedHives: () -> Unit = {}) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = sharedPref.getString("CURRENT_USER_UID", null)
    var notifications by remember { mutableStateOf(listOf<NotificationModel>()) }
    val notificationRepo = remember { NotificationRepositoryImplementation() }

    LaunchedEffect(userId) {
        if (userId != null) {
            coroutineScope.launch {
                notifications = notificationRepo.getNotificationsForUser(userId)
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFF1C1C1C),
        floatingActionButton = {
            LogoButton(
                onExploreClick = onNavigateToFeaturedHives)
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            items(notifications.size) { idx ->
                val notification = notifications[idx]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF232323))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(notification.message, color = Color.White)
                        if (notification.type == "invite") {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                // Add user to hive participants
                                if (userId != null) {
                                    val hiveRef = FirebaseDatabase.getInstance().getReference("hives").child(notification.hiveId)
                                    hiveRef.child("participants").get().addOnSuccessListener { snapshot ->
                                        val ids = snapshot.children.mapNotNull { it.getValue(String::class.java) }.toMutableList()
                                        if (!ids.contains(userId)) {
                                            ids.add(userId)
                                            hiveRef.child("participants").setValue(ids)
                                        }
                                        // Mark notification as read
                                        coroutineScope.launch {
                                            notificationRepo.markNotificationAsRead(userId, notification.id)
                                        }
                                        // Go to HiveGroupCallActivity
                                        val intent = Intent(context, HiveGroupCallActivity::class.java)
                                        intent.putExtra("HIVE_ID", notification.hiveId)
                                        intent.putExtra("HIVE_TITLE", notification.hiveTitle)
                                        intent.putExtra("HIVE_OWNER", notification.fromDisplayName)
                                        context.startActivity(intent)
                                    }
                                } else {
                                    Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                                }
                            }) {
                                Text("Join")
                            }
                        }
                    }
                }
            }
        }
    }
}
//Notification Page
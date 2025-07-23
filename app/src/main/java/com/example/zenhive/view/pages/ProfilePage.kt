package com.example.zenhive.view.pages

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.zenhive.viewmodel.HiveViewModel
import com.example.zenhive.R
import com.example.zenhive.ui.components.LogoButton
import com.example.zenhive.view.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(onNavigateToFeaturedHives: () -> Unit = {}) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val hiveViewModel: HiveViewModel = viewModel()
    val createdHives by hiveViewModel.liveHives.collectAsState()
    var photoUrl by remember { mutableStateOf<String?>(null) }
    var displayName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf<List<String>>(emptyList()) }

    // Fetch user information from Firebase
    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FirebaseDatabase.getInstance().getReference("users/$userId")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        photoUrl = snapshot.child("photoUrl").value as? String
                        displayName = snapshot.child("displayName").value as? String ?: ""
                        bio = snapshot.child("bio").value as? String ?: ""

                        // Get interests as a List
                        val interestsList = snapshot.child("interests").children.mapNotNull {
                            it.value as? String
                        }
                        interests = interestsList
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
        }
    }

    Scaffold(
        containerColor = Color(0xFF1C1C1C),
        floatingActionButton = {
            LogoButton(onExploreClick = onNavigateToFeaturedHives)
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            // Sign out from Firebase
                            FirebaseAuth.getInstance().signOut()

                            // Navigate to LoginActivity
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E2E2E))
                    ) {
                        Text("Logout", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

            }
            item {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .background(Color.Gray, CircleShape)
                ) {
                    if (photoUrl != null) {
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = "Profile Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = null,
                            tint = Color.DarkGray,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(64.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                Text("@$displayName", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            }
            item {
                Spacer(modifier = Modifier.height(5.dp))

            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("2 ", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("hives", color = Color.Gray)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("30 ", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("followers", color = Color.Gray)
                }
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))

            }


            item {
                Text(
                    bio.ifEmpty { "No bio added yet" },
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    lineHeight = 20.sp
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    interests.forEach { interest ->
                        val (color, category) = when (interest) {
                            // Knowledge category
                            "Physics", "Space", "Language", "History", "Philosophy", "AI/ML", "Quantum", "Stock Market" ->
                                Color(0xFF6CADF2) to "Knowledge"

                            // Wellness category
                            "Fitness", "Yoga", "Journaling", "Mindfulness", "Productivity" ->
                                Color(0xFF6CF28A) to "Wellness"

                            // Hanging Out category
                            "Travel", "Cafe-Hopping", "Board Games", "Minimalism" ->
                                Color(0xFFFFC727) to "Hanging Out"

                            // Social/Community category
                            "Activism", "Volunteering", "LGBTQ+", "Politics", "Public Speaking" ->
                                Color(0xFFF26C6C) to "Social/Community"

                            else -> Color(0xFF808080) to "Other" // Default gray for unmatched interests
                        }

                        Box(
                            modifier = Modifier
                                .background(color, RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = interest,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))

            }
            item {
                Text(
                    "\uD83C\uDFA4 Created Hives",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    createdHives.take(3).forEach { hive ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF8F2E4), RoundedCornerShape(30.dp))
                                .padding(16.dp)
                        ) {
                            Text("UI/UX", color = Color.Gray, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(hive.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Author Placeholder", color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = android.R.drawable.ic_menu_my_calendar), // Using system icon as fallback
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "${hive.participants.size}",
                                        color = Color.Gray
                                    ) // Removed unnecessary safe call
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Icon(
                                        painter = painterResource(id = android.R.drawable.ic_dialog_email), // Using system icon as fallback
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("12", color = Color.Gray) // Placeholder comment count
                                }
                                Button(
                                    onClick = {},
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E2E2E))
                                ) {
                                    Text("View", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

            }
        }
    }
}

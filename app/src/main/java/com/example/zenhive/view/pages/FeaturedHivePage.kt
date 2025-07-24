package com.example.zenhive.view.pages

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import com.example.zenhive.ui.components.HiveCard
import com.example.zenhive.viewmodel.HiveViewModel
import com.example.zenhive.view.pages.PeoplePage
import com.example.zenhive.R // 👈 Needed for placeholder image
import com.example.zenhive.ui.components.LogoButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeaturedHivesPage() {
    val scrollState = rememberScrollState()
    var activePage by remember { mutableStateOf("hives") }
    val hiveViewModel: HiveViewModel = viewModel()
    val liveHives by hiveViewModel.liveHives.collectAsState()

    val photoCache = remember { mutableStateMapOf<String, String?>() }
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser


    // heklper function
    fun loadCreatorPhotoUrl(hostId: String) {
        if (photoCache.containsKey(hostId)) return

        val userRef = FirebaseDatabase.getInstance().getReference("users").child(hostId)
        userRef.child("photoUrl").get().addOnSuccessListener { snapshot ->
            val url = snapshot.getValue(String::class.java)
            photoCache[hostId] = url
        }.addOnFailureListener {
            photoCache[hostId] = null
        }
    }



    Scaffold(
        containerColor = Color(0xFF1C1C1C),
        floatingActionButton = {
            LogoButton(
                onExploreClick = { /* Already on Featured Hives */ }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
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
                        LaunchedEffect(hive.hostUid) {
                            loadCreatorPhotoUrl(hive.hostUid)
                        }
                        HiveCard(
                            title = hive.title,
                            creatorPhotoUrl = photoCache[hive.hostUid],
                            membersCount = hive.participants.size,
                            onJoinClick = {
                                if (currentUser != null) {
                                    val dbRef = FirebaseDatabase.getInstance().getReference("hives").child(hive.hiveId)
                                    dbRef.child("participants").get().addOnSuccessListener { snapshot ->
                                        val participants = snapshot.children.mapNotNull { it.getValue(String::class.java) }.toMutableList()
                                        if (!participants.contains(currentUser.uid)) {
                                            participants.add(currentUser.uid)
                                            dbRef.child("participants").setValue(participants)
                                        }
                                        val intent = Intent(context, com.example.zenhive.view.HiveGroupCallActivity::class.java)
                                        intent.putExtra("HIVE_ID", hive.hiveId)
                                        intent.putExtra("HIVE_TITLE", hive.title)
                                        intent.putExtra("HIVE_OWNER", hive.hostName)
                                        context.startActivity(intent)
                                    }
                                }
                            }
                        )
                    }
                }
            } else {
                PeoplePage()
            }
        }
    }
}

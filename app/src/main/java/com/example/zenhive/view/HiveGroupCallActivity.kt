package com.example.zenhive.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil.compose.rememberAsyncImagePainter
import com.example.zenhive.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import im.zego.zegoexpress.ZegoExpressEngine
import im.zego.zegoexpress.callback.IZegoEventHandler
import im.zego.zegoexpress.constants.ZegoUpdateType
import im.zego.zegoexpress.entity.ZegoStream
import im.zego.zegoexpress.entity.ZegoUser
import java.util.*

class HiveGroupCallActivity : ComponentActivity() {

    private lateinit var hiveTitle: String
    private lateinit var hiveOwner: String
    private lateinit var hiveId: String
    private lateinit var dbRef: DatabaseReference
    private lateinit var userRef: DatabaseReference
    private var zegoUserId: String = ""
    private var zegoUserName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get hive info from intent
        hiveTitle = intent.getStringExtra("HIVE_TITLE") ?: "Unknown Hive"
        hiveOwner = intent.getStringExtra("HIVE_OWNER") ?: "Someone"
        hiveId = intent.getStringExtra("HIVE_ID") ?: ""
        dbRef = FirebaseDatabase.getInstance().getReference("hives").child(hiveId)
        userRef = FirebaseDatabase.getInstance().getReference("users")
        val currentUser = FirebaseAuth.getInstance().currentUser
        zegoUserId = currentUser?.uid ?: UUID.randomUUID().toString()
        zegoUserName = currentUser?.displayName ?: "Unknown"

        setContent {
            val participants = remember { mutableStateListOf<Participant>() }
            val context = this
            // Listen for participant changes and handle Zego room join/leave
            DisposableEffect(hiveId) {
                val participantListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val ids = snapshot.child("participants").children.mapNotNull { it.getValue(String::class.java) }
                        participants.clear()
                        ids.forEach { uid ->
                            userRef.child(uid).get().addOnSuccessListener { userSnap ->
                                val name = userSnap.child("displayName").getValue(String::class.java) ?: "Unknown"
                                val imageUrl = userSnap.child("photoUrl").getValue(String::class.java) ?: ""
                                participants.add(Participant(name, imageUrl, false))
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                }
                dbRef.addValueEventListener(participantListener)
                // Zego join room with ZegoUser
                val zegoUser = ZegoUser(zegoUserId, zegoUserName)
                ZegoExpressEngine.getEngine().loginRoom(hiveId, zegoUser)
                onDispose {
                    dbRef.removeEventListener(participantListener)
                    ZegoExpressEngine.getEngine().logoutRoom(hiveId)
                }
            }
            HiveGroupCallScreen(
                hiveTitle = hiveTitle,
                hiveOwner = hiveOwner,
                participantList = participants,
                onLeave = {
                    // Remove current user from participants and navigate to NavigationActivity
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        dbRef.child("participants").get().addOnSuccessListener { snapshot ->
                            val ids = snapshot.children.mapNotNull { it.getValue(String::class.java) }.toMutableList()
                            ids.remove(currentUser.uid)
                            dbRef.child("participants").setValue(ids)
                        }
                    }
                    val intent = android.content.Intent(context, NavigationActivity::class.java)
                    intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }
            )
        }
    }

    data class Participant(val name: String, val imageUrl: String, val isSpeaking: Boolean)
}

@Composable
fun HiveGroupCallScreen(hiveTitle: String, hiveOwner: String, participantList: List<HiveGroupCallActivity.Participant>, onLeave: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1E1E1E)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Top header
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(hiveTitle, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("$hiveOwner's Hive", color = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_mic_24),
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(participantList.size.toString(), color = Color.Gray, fontSize = 14.sp)
                    }
                }
                Text("2:38", color = Color.Gray, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Grid of users
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(participantList) { participant ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box {
                            Image(
                                painter = rememberAsyncImagePainter(participant.imageUrl),
                                contentDescription = participant.name,
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(4.dp)
                                    .clip(CircleShape)
                            )
                            if (!participant.isSpeaking) {
                                Image(
                                    painter = painterResource(id = R.drawable.baseline_mic_24),
                                    contentDescription = "Muted",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .align(Alignment.BottomEnd)
                                )
                            }
                        }
                        Text(participant.name, color = Color.White, fontSize = 14.sp)
                    }
                }
            }

            // Bottom control buttons
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onLeave,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text("Leave", fontWeight = FontWeight.Bold)
                }

                IconButton(onClick = { /* toggle mic */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_mic_24),
                        contentDescription = "Mic",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(onClick = { /* invite people */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_invite), // You need to add this icon
                        contentDescription = "Invite",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(onClick = { /* more options */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more), // You need to add this icon
                        contentDescription = "More",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

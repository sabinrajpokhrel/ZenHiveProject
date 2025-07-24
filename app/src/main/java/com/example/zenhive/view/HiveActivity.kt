package com.example.zenhive.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zenhive.R
import com.example.zenhive.model.HiveModel
import com.example.zenhive.repository.UserRepositoryImplementation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID
import kotlinx.coroutines.launch

class HiveActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HiveScreen()
        }
    }
}

@Composable
fun HiveScreen() {
    var hiveId by remember { mutableStateOf("") }
    var hiveTitle by remember { mutableStateOf("") }
    val context = LocalContext.current
    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }
    var isLoading by remember { mutableStateOf(false) }
    val userRepository = remember { UserRepositoryImplementation() }
    val coroutineScope = rememberCoroutineScope()

    // Observe FirebaseAuth state
    DisposableEffect(Unit) {
        val auth = FirebaseAuth.getInstance()
        val listener = FirebaseAuth.AuthStateListener { authInstance ->
            currentUser = authInstance.currentUser
        }
        auth.addAuthStateListener(listener)
        onDispose { auth.removeAuthStateListener(listener) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1F1F1F))
            .padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo & Title
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0xFFF9BE27)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "ZenHive",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "choose your hive",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        // Join a Hive
        Text(
            text = "Join a hive.",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = hiveId,
            onValueChange = { hiveId = it },
            placeholder = { Text("Hive ID", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFFDF7EA),
                unfocusedContainerColor = Color(0xFFFDF7EA),
                disabledContainerColor = Color(0xFFFDF7EA),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            textStyle = TextStyle(fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (hiveId.isNotBlank() && currentUser != null) {
                    val safeUser = currentUser
                    val dbRef = FirebaseDatabase.getInstance().getReference("hives").child(hiveId)
                    dbRef.get().addOnSuccessListener { snapshot ->
                        if (snapshot.exists()) {
                            val participants = snapshot.child("participants").children.mapNotNull { it.getValue(String::class.java) }.toMutableList()
                            if (safeUser != null && !participants.contains(safeUser.uid)) {
                                participants.add(safeUser.uid)
                                dbRef.child("participants").setValue(participants)
                            }
                            val hiveTitle = snapshot.child("title").getValue(String::class.java) ?: "Unknown Hive"
                            val hiveOwner = snapshot.child("hostName").getValue(String::class.java) ?: "Someone"
                            val intent = Intent(context, HiveGroupCallActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("HIVE_ID", hiveId)
                            intent.putExtra("HIVE_TITLE", hiveTitle)
                            intent.putExtra("HIVE_OWNER", hiveOwner)
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(context, "Hive not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Enter a Hive ID first", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF9BE27),
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "Join this hive",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Divider with OR
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                color = Color.Gray,
                thickness = 1.dp
            )
            Text(
                "  OR  ",
                color = Color.Gray,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Divider(
                modifier = Modifier.weight(1f),
                color = Color.Gray,
                thickness = 1.dp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Create your own
        Text(
            text = "Create your own.",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = hiveTitle,
            onValueChange = { hiveTitle = it },
            placeholder = { Text("What is it about?", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFFDF7EA),
                unfocusedContainerColor = Color(0xFFFDF7EA),
                disabledContainerColor = Color(0xFFFDF7EA),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            textStyle = TextStyle(fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Toast.makeText(context, "Button clicked", Toast.LENGTH_SHORT).show()
                val title = hiveTitle.trim()
                if (title.isEmpty()) {
                    Toast.makeText(context, "Enter a title first", Toast.LENGTH_SHORT).show()
                } else {
                    isLoading = true
                    // Get email and password from SharedPreferences (or another secure store)
                    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    val email = sharedPref.getString("CURRENT_USER_EMAIL", null)
                    val password = sharedPref.getString("CURRENT_USER_PASSWORD", null)
                    if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                        isLoading = false
                        Toast.makeText(context, "User credentials not found. Please login again.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    coroutineScope.launch {
                        val user = userRepository.login(email, password)
                        if (user != null) {
                            val uid = user.uid
                            val dbDisplayName = user.displayName ?: "Unknown"
                            val hiveId = UUID.randomUUID().toString()
                            val hive = HiveModel(
                                hiveId = hiveId,
                                title = title,
                                hostUid = uid,
                                hostName = dbDisplayName,
                                timestamp = System.currentTimeMillis(),
                                live = true,
                                participants = listOf(uid)
                            )
                            val dbRef = FirebaseDatabase.getInstance().reference
                            dbRef.child("hives").child(hiveId).setValue(hive)
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        val intent = Intent(context, HiveGroupCallActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        intent.putExtra("HIVE_ID", hiveId)
                                        intent.putExtra("HIVE_TITLE", title)
                                        intent.putExtra("HIVE_OWNER", dbDisplayName)
                                        context.startActivity(intent)
                                    } else {
                                        Toast.makeText(context, "Failed to create hive", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    isLoading = false
                                    Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                    e.printStackTrace()
                                }
                        } else {
                            isLoading = false
                            Toast.makeText(context, "User not found. Please login again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF9BE27),
                contentColor = Color.Black
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            } else {
                Text(
                    text = "Create Hive",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
//Testing in HiveActivity
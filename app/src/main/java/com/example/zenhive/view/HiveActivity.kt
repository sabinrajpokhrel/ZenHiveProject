package com.example.zenhive.view

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

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
            onClick = { /* TODO: Join logic */ },
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

                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null && hiveTitle.isNotBlank()) {
                    val hiveId = UUID.randomUUID().toString()
                    val hive = HiveModel(
                        hiveId = hiveId,
                        title = hiveTitle.trim(),
                        hostUid = currentUser.uid,
                        hostName = currentUser.displayName ?: "Unknown",
                        timestamp = System.currentTimeMillis(),
                        live = true,
                        participants = listOf(currentUser.uid)
                    )

                    val dbRef = FirebaseDatabase.getInstance().reference
                    dbRef.child("hives").child(hiveId).setValue(hive).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Navigate to NavigationActivity
                            val intent = Intent(context, NavigationActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(context, "Failed to create hive", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Enter a title first", Toast.LENGTH_SHORT).show()
                }
            },

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
                text = "Create Hive",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

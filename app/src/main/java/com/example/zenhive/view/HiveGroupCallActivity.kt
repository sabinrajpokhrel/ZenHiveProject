package com.example.zenhive.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.google.accompanist.flowlayout.FlowRow

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zenhive.R

class HiveGroupCallActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HiveGroupCallScreen()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HiveGroupCallScreen() {
    val users = listOf(
        "John", "Sofie", "Devi", "Max", "Rob", "Steve", "Deeya"
    )
    val images = listOf(
        R.drawable.person1, R.drawable.person2, R.drawable.person3,
        R.drawable.person4, R.drawable.person5
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1E1E1E) // Dark gray background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "NEPSE MARKET",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Sabin’s Hive", color = Color.LightGray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_supervised_user_circle_24),
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("126", color = Color.LightGray, fontSize = 14.sp)
                    }
                }
                Text(
                    text = "2:38",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.LightGray
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Grid of users
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                mainAxisSpacing = 24.dp,
                crossAxisSpacing = 24.dp
            ) {
                users.forEachIndexed { index, name ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box {
                            Image(
                                painter = painterResource(id = images[index % images.size]),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Image(
                                painter = painterResource(id = R.drawable.baseline_mic_off_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(name, color = Color.White, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { /* Leave logic */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD84343)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .height(50.dp)
                        .weight(1f)
                ) {
                    Text("Leave", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = { /* Mute */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_mic_off_24),
                        contentDescription = "Mute",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconButton(onClick = { /* Add user */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_add_2_24),
                        contentDescription = "Add User",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconButton(onClick = { /* More */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_more_horiz_24),
                        contentDescription = "More Options",
                        tint = Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun HiveGroupCallPreview() {
    HiveGroupCallScreen()
}
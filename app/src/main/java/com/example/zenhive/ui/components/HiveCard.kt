package com.example.zenhive.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HiveCard(
    category: String,
    title: String,
    creators: List<Int>,
    membersCount: Int,
    commentsCount: Int
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5D6)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(category, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(title, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                creators.forEach { id ->
                    Image(
                        painter = painterResource(id = id),
                        contentDescription = "Creator",
                        modifier = Modifier
                            .size(28.dp)
                            .padding(end = 4.dp)
                            .clip(RoundedCornerShape(50))
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("👥 $membersCount", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("💬 $commentsCount", fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { /* handle join */ },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Join", fontSize = 12.sp)
                }
            }
        }
    }
}

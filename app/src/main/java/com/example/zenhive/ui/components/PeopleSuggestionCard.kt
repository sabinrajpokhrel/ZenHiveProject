package com.example.zenhive.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zenhive.R

@Composable
fun PeopleSuggestionCard(
    name: String,
    age: Int,
    gender: String,
    description: String,
    interests: List<String>,
    interestColors: List<Color>,
    avatarRes: Int
) {
    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5D6)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = avatarRes),
                    contentDescription = name,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(50))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("$gender, $age y/o", fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text("Similar Interests:", color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    interests.forEachIndexed { i, interest ->
                        Surface(
                            color = interestColors.getOrElse(i) { Color.LightGray },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                interest,
                                color = Color.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(description, fontSize = 12.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(6.dp))

                Text("Movies:", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(5) {
                        Surface(
                            color = Color(0xFFB2C4E0),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.size(20.dp)
                        ) {}
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Button(
                        onClick = { /* handle add */ },
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBC125), contentColor = Color.Black),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Add")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { /* view profile */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.eye_off), // Add eye icon in drawable
                            contentDescription = "View",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}
//This is the People Suggestion Card
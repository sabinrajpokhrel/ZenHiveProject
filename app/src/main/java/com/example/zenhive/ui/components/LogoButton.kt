package com.example.zenhive.ui.components

import android.content.Intent
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.zenhive.R
import com.example.zenhive.view.HiveActivity
import kotlinx.coroutines.delay

@Composable
//This is the Logo Button that is a floating button in all activities
fun LogoButton(
    onExploreClick: () -> Unit
) {
    val context = LocalContext.current
    var isPressed by remember { mutableStateOf(false) }
    var showOptions by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = 0.4f,
            stiffness = 300f
        ),
        label = "buttonScale"
    )

    Box(contentAlignment = Alignment.Center) {
        FloatingActionButton(
            onClick = {
                isPressed = true
                showOptions = !showOptions
            },
            modifier = Modifier
                .size(94.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            containerColor = Color(0xFFfcbe22),
            shape = CircleShape
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Button",
                modifier = Modifier.size(70.dp)
            )
        }

        if (showOptions) {
            Popup(
                alignment = Alignment.TopCenter,
                offset = androidx.compose.ui.unit.IntOffset(0, -120)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFFBC125))
                        .padding(vertical = 10.dp, horizontal = 24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Explore",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.clickable {
                                showOptions = false
                                onExploreClick()
                            }
                        )

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(50.dp)
                                .background(Color.Black)
                        )

                        Text(
                            text = "Hive",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.clickable {
                                showOptions = false
                                context.startActivity(Intent(context, HiveActivity::class.java))
                            }
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

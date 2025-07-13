package com.example.zenhive.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.example.zenhive.R

class Login2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            Scaffold { innerPadding ->
                RegisterBody(innerPadding)
            }
        }
    }

    @Composable
    fun RegisterBody(innerPadding: PaddingValues) {
        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Scaffold(
            containerColor = colorResource(id = R.color.loginbgg),
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Background shape or image
                Image(
                    painter = painterResource(id = R.drawable.login_bg),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .align(Alignment.BottomCenter),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp, vertical = 40.dp)
                ) {
                    // Logo & app name
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(colorResource(id = R.color.payalo)),
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
                                "ZenHive",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text("choose your hive", fontSize = 14.sp, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    Text(
                        "Be a part of a global hive!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text("Create your account", fontSize = 14.sp, color = Color.White)

                    Spacer(modifier = Modifier.height(32.dp))

                    // Input fields
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("First Name", fontSize = 14.sp) },
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = colorResource(
                                    R.color.khairo
                                )
                            )
                        )
                        OutlinedTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Last Name", fontSize = 14.sp) },
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = colorResource(
                                    R.color.khairo
                                )
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Email", fontSize = 14.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = colorResource(
                                R.color.khairo
                            )
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Username", fontSize = 14.sp) },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = colorResource(
                                R.color.khairo
                            )
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Password", fontSize = 14.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.eye_off),
                                modifier = Modifier.size(24.dp),
                                contentDescription = "Hide password",

                                )
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = colorResource(
                                R.color.khairo
                            )
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Button(
                            onClick = { /* Register logic */ },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.payalo)),
                            modifier = Modifier
                                .width(120.dp)
                                .height(50.dp)
                        ) {
                            Text(
                                "Register",
                                fontSize = 18.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun RegisterPreview() {
        RegisterBody(innerPadding = PaddingValues(0.dp))
    }
}
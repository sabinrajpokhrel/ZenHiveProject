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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zenhive.R
import com.example.zenhive.repository.UserRepository
import com.example.zenhive.repository.UserRepositoryImplementation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetPasswordActivity : ComponentActivity() {
    private val userRepository: UserRepository = UserRepositoryImplementation()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uid = intent.getStringExtra("uid") ?: ""
        setContent {
            SetPasswordBody(
                uid = uid,
                coroutineScope = coroutineScope,
                userRepository = userRepository,
                showToast = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                },
                onPasswordSetSuccess = {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            )
        }
    }
}

@Composable
fun SetPasswordBody(
    uid: String,
    coroutineScope: CoroutineScope,
    userRepository: UserRepository,
    showToast: (String) -> Unit,
    onPasswordSetSuccess: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = colorResource(id = R.color.loginbgg),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Background Image (same as SignUp)
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
                    .padding(horizontal = 32.dp, vertical = 40.dp),
                verticalArrangement = Arrangement.Top
            ) {
                // Header with logo & title (same style)
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
                        Text(
                            "choose your hive",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Title & subtitle matching SignUp style
                Text(
                    "Set Your Password",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "Create a password for your account",
                    fontSize = 14.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Password input fields styled same as SignUp's text fields (outlined with background color)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.khairo)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.khairo)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            if (password.isBlank() || confirmPassword.isBlank()) {
                                showToast("Please fill all fields")
                                return@Button
                            }
                            if (password != confirmPassword) {
                                showToast("Passwords do not match")
                                return@Button
                            }

                            isLoading = true
                            coroutineScope.launch {
                                userRepository.updateUserPassword(uid, password)
                                isLoading = false
                                showToast("Password set successfully!")
                                onPasswordSetSuccess()
                            }
                        },
                        enabled = !isLoading,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.payalo)),
                        modifier = Modifier
                            .width(180.dp)
                            .height(50.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                "Set Password",
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
}

@Preview(showBackground = true)
@Composable
fun SetPasswordPreview() {
    val context = LocalContext.current
    SetPasswordBody(
        uid = "preview-uid",
        coroutineScope = CoroutineScope(Dispatchers.Main),
        userRepository = object : UserRepository {
            override suspend fun updateUserPassword(uid: String, password: String) {
                // no-op for preview
            }
            override suspend fun createUser(user: com.example.zenhive.model.UserModel) {
                // no-op for preview
            }
            override suspend fun getUserByUid(uid: String): com.example.zenhive.model.UserModel? {
                return null
            }
        },
        showToast = { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() },
        onPasswordSetSuccess = {}
    )
}

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zenhive.R
import com.example.zenhive.model.UserModel
import com.example.zenhive.repository.UserRepository
import com.example.zenhive.repository.UserRepositoryImplementation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SetPasswordActivity : ComponentActivity() {
    private val userRepository: UserRepository = UserRepositoryImplementation()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uid = intent.getStringExtra("uid") ?: ""
        val email = intent.getStringExtra("email") ?: ""

        setContent {
            SetPasswordBody(
                uid = uid,
                email = email,
                coroutineScope = coroutineScope,
                userRepository = userRepository,
                showToast = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                },
                onPasswordSetSuccess = { password ->
                    val intent = Intent(this, ProfileSetup::class.java).apply {
                        putExtra("uid", uid)
                        putExtra("email", email)
                        putExtra("password", password)  // now password is passed correctly
                    }
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
    email: String,
    coroutineScope: CoroutineScope,
    userRepository: UserRepository,
    showToast: (String) -> Unit,
    onPasswordSetSuccess: (String) -> Unit
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

                            coroutineScope.launch {
                                isLoading = true
                                try {
                                    val firebaseAuth = FirebaseAuth.getInstance()
                                    val currentUser = firebaseAuth.currentUser

                                    if (currentUser == null) {
                                        showToast("User not signed in")
                                        isLoading = false
                                        return@launch
                                    }

                                    // ✅ Update password
                                    currentUser.updatePassword(password).await()

                                    // ✅ Create UserModel with updated password
                                    val userModel = UserModel(
                                        uid = currentUser.uid,
                                        email = currentUser.email,
                                        password = password, // store hashed or encrypted in production!
                                        displayName = "",
                                        photoUrl = "",
                                        birthdate = "",
                                        instagram = "",
                                        spotify = "",
                                        bio = "",
                                        interests = emptyList()
                                    )

                                    userRepository.createUser(userModel)

                                    showToast("Password set successfully!")
                                    onPasswordSetSuccess(password)

                                } catch (e: Exception) {
                                    showToast("Failed to set password: ${e.localizedMessage}")
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                        ,
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

//Testing codes added

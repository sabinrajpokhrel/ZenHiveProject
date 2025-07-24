package com.example.zenhive.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.zenhive.R
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalView
import com.example.zenhive.repository.UserRepositoryImplementation
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        enableEdgeToEdge()
        setContent {
            Scaffold { innerPadding ->
                LoginBody(innerPadding)
            }
        }
    }

    @Composable
    fun LoginBody(innerPadding: PaddingValues) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()
        var isLoading by remember { mutableStateOf(false) }

        val context = LocalContext.current
        android.util.Log.d("LoginDebug", "LoginBody composition with context: $context")

        val userRepository = remember { UserRepositoryImplementation() }



        Scaffold(
            containerColor = colorResource(id = R.color.loginbgg),
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Background Image
                Image(
                    painter = painterResource(id = R.drawable.login_bg),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp) // adjust height as per design
                        .align(Alignment.BottomCenter),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp, vertical = 40.dp)
                ) {
                    // Logo and App Name
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
                                text = "ZenHive",
                                style = TextStyle(
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "choose your hive",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    // Welcome Back
                    Text(
                        text = "Welcome Back!",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Login to your account",
                        fontSize = 14.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Email Field (was Username)
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Email", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
                        },
                        textStyle = TextStyle(color = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = colorResource(id = R.color.khairo),
                            focusedContainerColor = colorResource(id = R.color.khairo)
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Password Field
                    var passwordVisible by remember { mutableStateOf(false) }

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Password", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
                        },
                        textStyle = TextStyle(color = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(id = if (passwordVisible) R.drawable.eye_off else R.drawable.eye),
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = colorResource(id = R.color.khairo),
                            focusedContainerColor = colorResource(id = R.color.khairo)
                        )
                    )


                    Spacer(modifier = Modifier.height(32.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                android.util.Log.d("LoginDebug", "Login button clicked")

                                if (email.isBlank() || password.isBlank()) {
                                    android.util.Log.d("LoginDebug", "Email or password is blank")
                                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                isLoading = true
                                android.util.Log.d("LoginDebug", "Starting login coroutine")

                                coroutineScope.launch {
                                    try {
                                        android.util.Log.d("LoginDebug", "Calling custom repository.login(email, password)")
                                        val user = userRepository.login(email, password)

                                        if (user != null) {
                                            android.util.Log.d("LoginDebug", "User found: $user")
                                            val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                                            sharedPref.edit()
                                                .putString("CURRENT_USER_EMAIL", email)
                                                .putString("CURRENT_USER_PASSWORD", password)
                                                .putString("CURRENT_USER_UID", user.uid)
                                                .apply()
                                            // No need to save to SharedPreferences since Firebase Auth handles the session
                                            context.startActivity(Intent(context, NavigationActivity::class.java))
                                            (context as? Activity)?.finish()
                                        } else {
                                            android.util.Log.d("LoginDebug", "User not found or password mismatch")
                                            Toast.makeText(context, "Invalid email or password", Toast.LENGTH_LONG).show()
                                        }
                                    } catch (e: Exception) {
                                        android.util.Log.e("LoginDebug", "Login error: ${e.message}", e)
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                    } finally {
                                        android.util.Log.d("LoginDebug", "Login process finished, setting isLoading=false")
                                        isLoading = false
                                    }
                                }
                            }
                            ,
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.payalo)),
                            modifier = Modifier
                                .width(107.dp)
                                .height(47.dp)
                        ) {
                            Text(
                                "Login",
                                fontSize = 18.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }


                    HorizontalDivider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp, vertical = 24.dp)
                    )

                    // Sign-Up Section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "New Here?",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val context = LocalContext.current
                        Button(
                            onClick = { context.startActivity(Intent(context, SignUpActivity::class.java)) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.signupbtn)),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text(
                                text = "Sign-Up",
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
    @Preview
    @Composable
    fun LoginPreviewBody() {
        LoginBody(innerPadding = PaddingValues(0.dp))
    }
}

//Testing codes added
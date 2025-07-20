package com.example.zenhive.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.zenhive.repository.UserRepositoryImplementation
import com.example.zenhive.ui.theme.ZenHiveTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class SignUpActivity : ComponentActivity() {

    private val userRepository = UserRepositoryImplementation()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val WEB_CLIENT_ID = "514228298551-7krkulbvv3cc7sge5241drfvv69o91hb.apps.googleusercontent.com"
        private const val ANDROID_CLIENT_ID = "514228298551-gcgogm4i1dudm9iqdbkhjq593josbiaq.apps.googleusercontent.com"
    }

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("SignUpActivity", "Sign in result received: ${result.resultCode}")
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            coroutineScope.launch {
                try {
                    val account = withContext(Dispatchers.IO) {
                        withTimeout(60_000) { // 60 seconds timeout
                            task.await()
                        }
                    }

                    Log.d("SignUpActivity", "Got account: ${account.email}")

                    if (account?.idToken != null) {
                        val userModel = withContext(Dispatchers.IO) {
                            userRepository.firebaseAuthWithGoogle(account.idToken!!)
                        }

                        if (userModel != null) {
                            val intent = Intent(this@SignUpActivity, SetPasswordActivity::class.java).apply {
                                putExtra("uid", userModel.uid)
                                putExtra("email", userModel.email)
                            }
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@SignUpActivity, "Authentication failed", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@SignUpActivity, "Failed to get ID token", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Log.e("SignUpActivity", "Sign in error", e)
                    val errorMessage = when {
                        e is TimeoutCancellationException -> "Sign in timed out. Please check your internet connection."
                        e.message?.contains("7:") == true -> "Google Play Services error. Please update Google Play Services and try again."
                        else -> "Sign in error: ${e.message}"
                    }
                    Toast.makeText(this@SignUpActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check and update Google Play Services
        checkAndUpdateGooglePlayServices()

        // Configure Google Sign In
        setupGoogleSignIn()

        setContent {
            var isLoading by remember { mutableStateOf(false) }

            ZenHiveTheme(darkTheme = false, dynamicColor = false) {
                Scaffold(containerColor = colorResource(id = R.color.loginbgg)) { innerPadding ->
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
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(
                                    onClick = {
                                        isLoading = true
                                        val signInIntent = googleSignInClient.signInIntent
                                        googleSignInLauncher.launch(signInIntent)
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.payalo)),
                                    modifier = Modifier
                                        .width(220.dp)
                                        .height(50.dp)
                                ) {
                                    if (isLoading) {
                                        CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                                    } else {
                                        Text(
                                            "Sign Up with Google",
                                            fontSize = 18.sp,
                                            color = Color.Black,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 40.dp, vertical = 20.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "Already registered?",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.payalo)),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text(
                                        text = "Login",
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
        }
    }

    private fun checkAndUpdateGooglePlayServices() {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                val dialog = googleApiAvailability.getErrorDialog(this, resultCode, 9001)
                dialog?.show()
            } else {
                Toast.makeText(this, "This device is not supported", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID)
            .requestServerAuthCode(WEB_CLIENT_ID)
            .requestEmail()
            .requestProfile()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso).apply {
            signOut().addOnCompleteListener {
                Log.d("SignUpActivity", "Previous sign-in state cleared")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}

package com.example.zenhive.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.example.zenhive.R

class OtpConfirmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold { innerPadding ->
                OtpConfirmBody(innerPadding)
            }
        }
    }

    @Composable
    fun OtpConfirmBody(innerPadding: PaddingValues) {
        var otp1 by remember { mutableStateOf("") }
        var otp2 by remember { mutableStateOf("") }
        var otp3 by remember { mutableStateOf("") }
        var otp4 by remember { mutableStateOf("") }

        Scaffold(
            containerColor = colorResource(id = R.color.loginbgg),
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Background image at bottom like in SignUp
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
                    // Logo & app name (same as SignUp)
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
                        "OTP Confirmation",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "Enter the 4-digit code sent to your email",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // OTP input row - 4 separate boxes
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        @Composable
                        fun otpTextField(value: String, onValueChange: (String) -> Unit) = OutlinedTextField(
                            value = value,
                            onValueChange = {
                                if (it.length <= 1 && it.all { ch -> ch.isDigit() }) {
                                    onValueChange(it)
                                }
                            },
                            modifier = Modifier
                                .width(60.dp)
                                .height(60.dp),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = colorResource(R.color.khairo),
                                focusedBorderColor = colorResource(R.color.payalo),
                                cursorColor = colorResource(R.color.payalo),
                                focusedLabelColor = colorResource(R.color.payalo)
                            )
                        )

                        otpTextField(otp1) { otp1 = it }
                        otpTextField(otp2) { otp2 = it }
                        otpTextField(otp3) { otp3 = it }
                        otpTextField(otp4) { otp4 = it }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Button(
                            onClick = {
                                // Confirm OTP logic here
                                val enteredOtp = otp1 + otp2 + otp3 + otp4
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.payalo)),
                            modifier = Modifier
                                .width(140.dp)
                                .height(50.dp)
                        ) {
                            Text(
                                "Confirm",
                                fontSize = 18.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = {
                            // Resend OTP logic here
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            "Resend OTP",
                            fontSize = 14.sp,
                            color = colorResource(R.color.payalo)
                        )
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun OtpConfirmPreview() {
        OtpConfirmBody(PaddingValues(0.dp))
    }
}

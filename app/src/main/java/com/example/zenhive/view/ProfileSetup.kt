package com.example.zenhive.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.zenhive.R
import com.example.zenhive.model.UserModel
import com.example.zenhive.repository.UserRepository
import com.example.zenhive.repository.UserRepositoryImplementation
import com.example.zenhive.ui.theme.ZenHiveTheme
import com.example.zenhive.utils.CloudinaryUploader
import kotlinx.coroutines.launch
import java.io.File
import java.time.format.DateTimeFormatter

class ProfileSetup : ComponentActivity() {

    private val userRepository: UserRepository by lazy { UserRepositoryImplementation() }
    private val uid: String by lazy { intent.getStringExtra("uid") ?: "" }
    private val email: String by lazy { intent.getStringExtra("email") ?: "" }
    private val password: String by lazy { intent.getStringExtra("password") ?: "" }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZenHiveTheme {
                Scaffold { innerPadding ->
                    PublicProfileSetup(
                        uid = uid,
                        userRepository = userRepository,
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PublicProfileSetup(
        uid: String,
        userRepository: UserRepository,
        innerPadding: PaddingValues = PaddingValues(0.dp)
    ) {
        val backgroundColor = Color(0xFFF1EAD2) // beige background
        val fieldColor = Color(0xFFFFF3B0)      // yellow field bg
        val labelColor = Color(0xFF555555)
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        var name by remember { mutableStateOf("") }
        var birthday by remember { mutableStateOf("") }
        var instagram by remember { mutableStateOf("") }
        var spotify by remember { mutableStateOf("") }
        var bio by remember { mutableStateOf("") }
        var interests by remember { mutableStateOf(listOf<String>()) }
        var photoUri by remember { mutableStateOf<Uri?>(null) }
        var isLoading by remember { mutableStateOf(false) }

        var showDatePicker by remember { mutableStateOf(false) }
        val datePickerState = rememberDatePickerState()

        // Image picker launcher
        val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            photoUri = uri
        }

        // Load existing user data when composable enters composition
        LaunchedEffect(uid) {
            if (uid.isNotBlank()) {
                isLoading = true
                try {
                    val existingUser = userRepository.getUserByUid(uid)
                    existingUser?.let { user ->
                        name = user.displayName ?: ""
                        birthday = user.birthdate ?: ""
                        instagram = user.instagram ?: ""
                        spotify = user.spotify ?: ""
                        interests = user.interests ?: emptyList()
                        bio = user.bio ?: ""
                        user.photoUrl?.let { url ->
                            photoUri = Uri.parse(url)  // if you store URL as string, convert to Uri here
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Failed to load profile: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                } finally {
                    isLoading = false
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(innerPadding)
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Setup your public profile",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Profile photo placeholder
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFCCCCCC))
                    .clickable { galleryLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (photoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(photoUri),
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "Upload",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Full Name Field
            FieldLabel("What should we call you?", labelColor)
            CustomInputField(value = name, onValueChange = { name = it }, fieldColor)

            Spacer(modifier = Modifier.height(16.dp))

            // Birthday Field
            FieldLabel("Enter your birthday", labelColor)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(fieldColor)
                    .clickable { showDatePicker = true }
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (birthday.isNotEmpty()) birthday else "Select date",
                        color = if (birthday.isNotEmpty()) Color.Black else Color.Gray
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = "Calendar",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            FieldLabel("Your Socials", labelColor)
            Spacer(modifier = Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CustomInputField(
                    value = instagram,
                    onValueChange = { instagram = it },
                    fieldColor = fieldColor,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_instagram),
                            contentDescription = "Instagram",
                            tint = Color.Gray,
                            modifier = Modifier.size(40.dp)
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                CustomInputField(
                    value = spotify,
                    onValueChange = { spotify = it },
                    fieldColor = fieldColor,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_spotify),
                            contentDescription = "Spotify",
                            tint = Color.Gray,
                            modifier = Modifier.size(36.dp)
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bio Field
            FieldLabel("Add your public bio", labelColor)
            CustomInputField(
                value = bio,
                onValueChange = { bio = it },
                fieldColor = fieldColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        try {
                            var photoUrl: String? = null
                            if (photoUri != null) {
                                try {
                                    val file = uriToFile(context, photoUri!!)
                                    photoUrl = CloudinaryUploader.uploadImage(file)
                                } catch (e: Exception) {
                                    Log.e("ProfileSetup", "Error uploading image: ${e.message}", e)
                                    Toast.makeText(context, "Failed to upload image: ${e.message}", Toast.LENGTH_LONG).show()
                                    isLoading = false
                                    return@launch
                                }
                            }

                            val updatedUser = UserModel(
                                uid = uid,
                                email = email,
                                displayName = name,
                                birthdate = birthday,
                                instagram = instagram,
                                spotify = spotify,
                                password = password,
                                interests = emptyList(),
                                bio = bio,
                                photoUrl = photoUrl
                            )

                            try {
                                userRepository.updateUserProfile(uid, updatedUser)
                                Toast.makeText(context, "Profile saved successfully!", Toast.LENGTH_SHORT).show()

                                // Navigate to InterestActivity
                                val intent = Intent(context, InterestActivity::class.java).apply {
                                    putExtra("uid", uid)  // Pass the uid to InterestActivity
                                }
                                context.startActivity(intent)
                                finish() // Close ProfileSetup activity
                            } catch (e: Exception) {
                                Log.e("ProfileSetup", "Error updating profile: ${e.message}", e)
                                Toast.makeText(context, "Failed to update profile: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Log.e("ProfileSetup", "General error: ${e.message}", e)
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        } finally {
                            isLoading = false
                        }
                    }
                },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier
                    .width(160.dp)
                    .height(50.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Next Page", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDatePicker = false
                            datePickerState.selectedDateMillis?.let { millis ->
                                val localDate = java.time.Instant.ofEpochMilli(millis)
                                    .atZone(java.time.ZoneId.systemDefault())
                                    .toLocalDate()
                                birthday = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            }
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }

    @Composable
    fun FieldLabel(text: String, color: Color) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = color,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, bottom = 4.dp)
        )
    }

    @Composable
    fun CustomInputField(
        value: String,
        onValueChange: (String) -> Unit,
        fieldColor: Color = Color.White,
        modifier: Modifier = Modifier.fillMaxWidth(),
        leadingIcon: @Composable (() -> Unit)? = null
    ) {
        val borderColor = Color(0xFFFFD600) // Yellow border color

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .background(color = Color.White),
            shape = RoundedCornerShape(10.dp),
            maxLines = 1,
            leadingIcon = leadingIcon,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                cursorColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )
    }

    fun uriToFile(context: Context, uri: Uri): File {
        Log.d("ProfileSetup", "Converting URI to file: $uri")

        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri)
        Log.d("ProfileSetup", "File mime type: $mimeType")

        val inputStream = contentResolver.openInputStream(uri)
            ?: throw Exception("Failed to open input stream for uri: $uri")

        val fileName = "upload_${System.currentTimeMillis()}.${mimeType?.substringAfter('/') ?: "jpg"}"
        val tempFile = File(context.cacheDir, fileName)

        Log.d("ProfileSetup", "Creating temp file: ${tempFile.absolutePath}")

        tempFile.outputStream().use { outputStream ->
            inputStream.use { input ->
                val bytes = input.readBytes()
                Log.d("ProfileSetup", "Read ${bytes.size} bytes from input stream")
                outputStream.write(bytes)
                outputStream.flush()
            }
        }

        Log.d("ProfileSetup", "Temp file created. Exists: ${tempFile.exists()}, Size: ${tempFile.length()} bytes")

        if (!tempFile.exists() || tempFile.length() == 0L) {
            throw Exception("Failed to create file from uri: $uri. File exists: ${tempFile.exists()}, size: ${tempFile.length()}")
        }

        return tempFile
    }



    @Preview(showBackground = true)
    @Composable
    fun RegisterPreview() {
        ZenHiveTheme {
            PublicProfileSetup(
                uid = "preview-uid",
                userRepository = object : UserRepository {
                    override suspend fun createUser(user: UserModel) {}
                    override suspend fun getUserByUid(uid: String): UserModel? = null
                    override suspend fun updateUserPassword(uid: String, newPassword: String) {}
                    override suspend fun updateUserProfile(uid: String, updatedUser: UserModel) {}
                    override suspend fun updateUserBio(uid: String, bio: String) {}
                    override suspend fun updateUserInterests(uid: String, interests: List<String>) {}
                    override suspend fun firebaseAuthWithGoogle(idToken: String): UserModel? = null
                    override suspend fun login(
                        email: String,
                        password: String
                    ): UserModel? {
                        TODO("Not yet implemented")
                    }
                },
                innerPadding = PaddingValues(0.dp)
            )
        }
    }
}

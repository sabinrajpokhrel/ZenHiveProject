package com.example.zenhive.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zenhive.R
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ContentScale
import com.example.zenhive.view.pages.ProfilePage
import com.example.zenhive.view.pages.FeaturedHivesPage
import com.example.zenhive.view.pages.PeoplePage
import com.example.zenhive.view.pages.NotificationPage
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.zenhive.ui.components.LogoButton
import com.example.zenhive.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.zenhive.model.UserModel

class NavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        enableEdgeToEdge()

        setContent {
            NavigationBody()
        }
        }
    }




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBody() {
    val context = LocalContext.current
    var selectedIndex by remember { mutableStateOf(0) }
    var activePage by remember { mutableStateOf("hives") }
    var showSearchBar by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<UserModel>?>(null) }

    Scaffold(
        containerColor = colorResource(id = R.color.loginbgg),
        topBar = {
            TopNavBar(
                onSearchClick = { showSearchBar = !showSearchBar },
                onNotificationClick = { selectedIndex = 1 },
                onNavItemClick = { index -> selectedIndex = index }
            )
        },

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .padding(horizontal = 16.dp, vertical = 16.dp)
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            if (showSearchBar) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                )
                Button(
                    onClick = {
                        if (searchText.isNotBlank()) {
                            val usersRef = FirebaseDatabase.getInstance().getReference("users")
                            usersRef.get().addOnSuccessListener { snapshot ->
                                val allUsers = snapshot.children.mapNotNull { it.getValue(UserModel::class.java) }
                                val filtered = allUsers.filter {
                                    it.displayName?.contains(searchText, ignoreCase = true) == true
                                }
                                searchResults = filtered
                            }.addOnFailureListener {
                                searchResults = emptyList()
                            }
                        } else {
                            searchResults = null
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Search")
                }
                if (searchResults != null) {
                    if (searchResults!!.isEmpty()) {
                        Text("No users found!", color = Color.White, modifier = Modifier.padding(top = 8.dp))
                    } else {
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            searchResults!!.forEach { user ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF232323))
                                ) {
                                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                        if (!user.photoUrl.isNullOrBlank()) {
                                            AsyncImage(
                                                model = user.photoUrl,
                                                contentDescription = "Profile Photo",
                                                modifier = Modifier
                                                    .size(48.dp)
                                                    .clip(CircleShape)
                                            )
                                        } else {
                                            Icon(
                                                painter = painterResource(id = R.drawable.person),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(48.dp)
                                                    .clip(CircleShape),
                                                tint = Color.Gray
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Column {
                                            Text(user.displayName ?: "No Name", color = Color.White, fontWeight = FontWeight.Bold)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(user.bio.ifBlank { "No bio available." }, color = Color.Gray)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

//            Row(
//                modifier = Modifier
//                    .horizontalScroll(rememberScrollState()),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                Button(
//                    onClick = {
//                        activePage = "hives"
//                        selectedIndex = 0
//                    },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = if (activePage == "hives") Color(0xFFFBC125) else Color.DarkGray,
//                        contentColor = if (activePage == "hives") Color.Black else Color.White
//                    ),
//                    shape = RoundedCornerShape(30.dp)
//                ) {
//                    Text("Explore Hives")
//                }
//                Button(
//                    onClick = {
//                        activePage = "people"
//                        selectedIndex = 0
//                    },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = if (activePage == "people") Color(0xFFFBC125) else Color.DarkGray,
//                        contentColor = if (activePage == "people") Color.Black else Color.White
//                    ),
//                    shape = RoundedCornerShape(30.dp)
//                ) {
//                    Text("Meet People")
//                }
//            }

//            Spacer(modifier = Modifier.height(20.dp))

            // Show NotificationPage when selectedIndex == 1
            var pageToShow = activePage
            if (selectedIndex == 1) {
                pageToShow = "notification"
            }
            var showPage = true
            when (selectedIndex) {
                1 -> {
                    NotificationPage(
                        onNavigateToFeaturedHives = {
                            selectedIndex = 0
                        }
                    )
                }
                4 -> {
                    ProfilePage(
                        onNavigateToFeaturedHives = {
                            selectedIndex = 0
                        }
                    )
                }
                else -> {
                    when (pageToShow) {
                        "notification" -> NotificationPage(
                            onNavigateToFeaturedHives = {
                                selectedIndex = 0
                            }
                        )
                        "hives" -> FeaturedHivesPage()
                        "people" -> PeoplePage()
                        else -> FeaturedHivesPage()
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit, // not used now, but keep to match your signature
    onNavItemClick: (Int) -> Unit
) {
//    var profileMenuExpanded by remember { mutableStateOf(false) }
    val userViewModel: UserViewModel = viewModel()
    val user by userViewModel.user.collectAsState()
    val context = LocalContext.current
    var userPhotoUrl by remember { mutableStateOf<String?>(null) }

    // Fetch UID from SharedPreferences or FirebaseAuth
    val sharedPref = context.getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
    val sharedPrefUid = sharedPref.getString("CURRENT_USER_UID", null)
    val userId = sharedPrefUid ?: FirebaseAuth.getInstance().currentUser?.uid

    // Fetch photoUrl from Firebase
    LaunchedEffect(userId) {
        if (userId != null) {
            com.google.firebase.database.FirebaseDatabase.getInstance().getReference("users/$userId/photoUrl")
                .addListenerForSingleValueEvent(object : com.google.firebase.database.ValueEventListener {
                    override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                        userPhotoUrl = snapshot.getValue(String::class.java)
                    }
                    override fun onCancelled(error: com.google.firebase.database.DatabaseError) {}
                })
        } else {
            userPhotoUrl = null
        }
    }

    TopAppBar(
        title = {
            IconButton(onClick = onSearchClick) {
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search"
                )
            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color.DarkGray),
        actions = {
            IconButton(onClick = onNotificationClick) {
                Image(
                    painter = painterResource(id = R.drawable.bell),
                    contentDescription = "Notifications"
                )
            }
            Box {
                IconButton(onClick = { onNavItemClick(4) }) {
                    if (userPhotoUrl != null && userPhotoUrl!!.isNotEmpty()) {
                        AsyncImage(
                            model = userPhotoUrl,
                            contentDescription = "Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.person),
                            contentDescription = "Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            }
        }
    )
}







@Preview(showBackground = true)
@Composable
fun PreviewNavigationBody() {
    NavigationBody()
}

//Testing with JUnit function

package com.example.zenhive.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zenhive.R
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay


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
    var selectedIndex by remember { mutableStateOf(0) }
    var activePage by remember { mutableStateOf("hives") }
    var showSearchBar by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = colorResource(id = R.color.loginbgg),
        topBar = {
            TopNavBar(
                onSearchClick = { showSearchBar = !showSearchBar },
                onCalendarClick = { selectedIndex = 1 },
                onNotificationClick = { selectedIndex = 2 }
            )
        },

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            if (showSearchBar) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
//                    colors = TextFieldDefaults.outlinedTextFieldColors(
//                        containerColor = Color.White,
//                        textColor = Color.Black,
//                        placeholderColor = Color.Gray
//                    )

                )
            }

            Text(
                "Discover, Connect & Grow",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Find curated sessions and people that match your curiosity.",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(20.dp))

            val scrollState = rememberScrollState()

            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { activePage = "hives" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (activePage == "hives") Color(0xFFFBC125) else Color.DarkGray,
                        contentColor = if (activePage == "hives") Color.Black else Color.White
                    ),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text("Explore Hives")
                }
                Button(
                    onClick = { activePage = "people" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (activePage == "people") Color(0xFFFBC125) else Color.DarkGray,
                        contentColor = if (activePage == "people") Color.Black else Color.White
                    ),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text("Meet People")
                }
            }



            Spacer(modifier = Modifier.height(20.dp))

            if (activePage == "hives") {
                FeaturedHivesPage()
            } else {
                PeoplePage()
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {

                // State to control scaling
                var isPressed by remember { mutableStateOf(false) }

// Animated scale value: 1.0 normally, 1.2 when pressed
                val scale by animateFloatAsState(
                    targetValue = if (isPressed) 1.2f else 1f,
                    animationSpec = spring(
                        dampingRatio = 0.4f, // makes it bounce
                        stiffness = 300f
                    ),
                    label = "buttonScale"
                )

                // State to control menu visibility
                var showOptions by remember { mutableStateOf(false) }


                Box(
                    modifier = Modifier
                        .size(94.dp)
                        .clip(CircleShape)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale

                        }
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    isPressed = true
                                    showOptions = !showOptions  // Toggle popup visibility on tap
                                },
                                onLongPress = {
                                    Log.d("LongPress", "Triggered!")
                                    showOptions = true  // Keep long press to also show popup
                                }
                            )
                        }
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo Button"
                    )
                }


                // Anchored DropdownMenu inside the same Box
                if (showOptions) {
                    Box(
                        modifier = Modifier
                            .offset(y = (-85).dp) // position popup above the button
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
                                modifier = Modifier
                                    .clickable {
                                        showOptions = false
                                        // TODO: Handle Explore
                                    }
                            )

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(50.dp)
                                    .background(Color.Black)
                            )

                            Text(
                                text = "Create",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier
                                    .clickable {
                                        showOptions = false
                                        // TODO: Handle Create
                                    }
                            )
                        }
                    }
                }




// LaunchEffect safely here
                LaunchedEffect(isPressed) {
                    if (isPressed) {
                        delay(150)
                        isPressed = false
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
    onCalendarClick: () -> Unit,
    onNotificationClick: () -> Unit // not used now, but keep to match your signature
) {
    var profileMenuExpanded by remember { mutableStateOf(false) }
    var notificationMenuExpanded by remember { mutableStateOf(false) }

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
            IconButton(onClick = onCalendarClick) {
                Image(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Calendar"
                )
            }

            Box {
                IconButton(onClick = { notificationMenuExpanded = true }) {
                    Image(
                        painter = painterResource(id = R.drawable.bell),
                        contentDescription = "Notifications"
                    )
                }

                DropdownMenu(
                    expanded = notificationMenuExpanded,
                    onDismissRequest = { notificationMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("No notifications") },
                        onClick = { notificationMenuExpanded = false }
                    )
                }
            }

            Box {
                IconButton(onClick = { profileMenuExpanded = true }) {
                    Image(
                        painter = painterResource(id = R.drawable.person),
                        contentDescription = "Profile"
                    )
                }

                DropdownMenu(
                    expanded = profileMenuExpanded,
                    onDismissRequest = { profileMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Account Settings") },
                        onClick = { profileMenuExpanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Logout") },
                        onClick = { profileMenuExpanded = false }
                    )
                }
            }
        }
    )
}

@Composable
fun FeaturedHivesPage() {
    Column {
        HiveCard(
            category = "ui/ux",
            title = "How do you create a design hypothesis?",
            creators = listOf(R.drawable.person1, R.drawable.person2),
            membersCount = 31,
            commentsCount = 12
        )
        HiveCard(
            category = "Stock Market",
            title = "NEPSE Technical Analysis Guide",
            creators = listOf(R.drawable.person3, R.drawable.person4),
            membersCount = 23,
            commentsCount = 6
        )
    }
}

@Composable
fun HiveCard(
    category: String,
    title: String,
    creators: List<Int>,
    membersCount: Int,
    commentsCount: Int
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5D6)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(category, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(title, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                creators.forEach { id ->
                    Image(
                        painter = painterResource(id = id),
                        contentDescription = "Creator",
                        modifier = Modifier
                            .size(28.dp)
                            .padding(end = 4.dp)
                            .clip(RoundedCornerShape(50))
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("👥 $membersCount", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("💬 $commentsCount", fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { /* handle join */ },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Join", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun PeoplePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())  // Enables vertical scrolling
    ) {
        Text("People picked for you", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Browse through people you find interesting and add them to your hive", color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))

        PeopleSuggestionCard(
            name = "John",
            age = 35,
            gender = "Male",
            description = "Join the hive to get Farming and Stock-Market Analysis 🌾💹",
            interests = listOf("Farming", "Stocks", "Philosophy", "Comedy"),
            interestColors = listOf(Color(0xFFB3E283), Color(0xFF89CFF0), Color(0xFFF6D186), Color(0xFFFFD580)),
            avatarRes = R.drawable.person2
        )

        PeopleSuggestionCard(
            name = "Sofie",
            age = 19,
            gender = "Female",
            description = "Feel free to join my hive. I share fashion insights there! ✨",
            interests = listOf("Beauty", "Fashion", "Books", "AI/ML"),
            interestColors = listOf(Color(0xFFFFB6C1), Color(0xFFFF6961), Color(0xFFF6D186), Color(0xFF89CFF0)),
            avatarRes = R.drawable.person1
        )

        PeopleSuggestionCard(
            name = "Max",
            age = 20,
            gender = "Male",
            description = "I do games and also live stream. Hop in and we’ll play together 🎮",
            interests = listOf("Music", "Culture", "E-Sports", "Plants"),
            interestColors = listOf(Color(0xFF89CFF0), Color(0xFFF6D186), Color(0xFFFF6961), Color(0xFFB3E283)),
            avatarRes = R.drawable.person3
        )

        PeopleSuggestionCard(
            name = "Emma",
            age = 28,
            gender = "Female",
            description = "Food blogger sharing healthy recipes and restaurant experiences. 🍽️✨",
            interests = listOf("Food", "Travel", "Photography", "Yoga"),
            interestColors = listOf(Color(0xFFFFE066), Color(0xFF89CFF0), Color(0xFFF6D186), Color(0xFFB3E283)),
            avatarRes = R.drawable.person4
        )

        PeopleSuggestionCard(
            name = "Siddika",
            age = 20,
            gender = "Female",
            description = "Fitness enthusiast and motivational coach. Let's hustle together! 💪🔥",
            interests = listOf("Fitness", "Motivation", "Hiking", "Podcasts"),
            interestColors = listOf(Color(0xFFF6D186), Color(0xFFFF6961), Color(0xFF89CFF0), Color(0xFFFFD580)),
            avatarRes = R.drawable.person5
        )

        PeopleSuggestionCard(
            name = "Lily",
            age = 24,
            gender = "Female",
            description = "Artist and illustrator sharing sketches and creative tutorials. 🎨🖌️",
            interests = listOf("Art", "Illustration", "Design", "Cats"),
            interestColors = listOf(Color(0xFFFFB6C1), Color(0xFFF6D186), Color(0xFFB3E283), Color(0xFFFFE066)),
            avatarRes = R.drawable.person6
        )
    }
}



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


@Preview(showBackground = true)
@Composable
fun PreviewNavigationBody() {
    NavigationBody()
}
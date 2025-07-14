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
import com.example.zenhive.view.pages.FeaturedHivesPage
import com.example.zenhive.view.pages.PeoplePage
import com.example.zenhive.view.pages.NotificationPage
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
                onNotificationClick = { selectedIndex = 1 }
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

            Spacer(modifier = Modifier.height(20.dp))

            // Show NotificationPage when selectedIndex == 1
            var pageToShow = activePage
            if (selectedIndex == 1) {
                pageToShow = "notification"
            }
            var showPage = true
            while (showPage) {
                when (pageToShow) {
                    "notification" -> {
                        NotificationPage()
                        showPage = false
                    }
                    "hives" -> {
                        FeaturedHivesPage()
                        showPage = false
                    }
                    "people" -> {
                        PeoplePage()
                        showPage = false
                    }
                    else -> {
                        FeaturedHivesPage()
                        showPage = false
                    }
                }
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
                                        selectedIndex = 0
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

    onNotificationClick: () -> Unit // not used now, but keep to match your signature
) {
    var profileMenuExpanded by remember { mutableStateOf(false) }

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







@Preview(showBackground = true)
@Composable
fun PreviewNavigationBody() {
    NavigationBody()
}
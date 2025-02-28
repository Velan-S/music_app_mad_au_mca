//package com.example.vibesync.ui.theme
//
//import androidx.compose.animation.*
//import androidx.compose.foundation.*
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import coil.compose.AsyncImage
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.graphics.painter.Painter
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.unit.Dp
//import com.example.vibesync.R
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreen(toggleTheme: () -> Unit) {
//    var showSearch by remember { mutableStateOf(false) }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.background)
//        ) {
//            // Top App Bar
//            TopAppBar(
//                title = { Text("VibeSync", fontSize = 20.sp, color = Color.White) },
//                actions = {
//                    IconButton(onClick = { showSearch = true }) {
//                        Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.White)
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
//            )
//
//            // Category Buttons
//            Row(
//                modifier = Modifier
//                    .padding(16.dp)
//                    .horizontalScroll(rememberScrollState())
//            ) {
//                listOf("Chill", "Trending", "Hip-Hop", "Jazz").forEach { category ->
//                    CategoryButton(text = category)
//                    Spacer(modifier = Modifier.width(8.dp))
//                }
//            }
//
//            // Trending Section
//            Section(title = "Trending Now", imageUrls = listOf(
//                "https://placecats.com/neo/300/200",
//                "https://placecats.com/neo/300/200",
//                "https://placecats.com/neo/300/200"
//            ))
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Recommended Section
//            Section(title = "Recommended for You", imageUrls = listOf(
//                "https://placecats.com/neo/300/200",
//                "https://placecats.com/neo/300/200",
//                "https://placecats.com/neo/300/200"
//            ))
//        }
//
//        // Bottom Navigation Bar
//        BottomNavigationBar(modifier = Modifier.align(Alignment.BottomCenter))
//
//        // Search Screen with Fade-In Effect
//        AnimatedVisibility(
//            visible = showSearch,
//            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
//            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
//        ) {
//            SearchScreen(onClose = { showSearch = false })
//        }
//    }
//}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchScreen(onClose: () -> Unit) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(), start = 16.dp, end = 16.dp)
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            IconButton(onClick = onClose) {
//                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
//            }
//            TextField(
//                value = "",
//                onValueChange = {},
//                placeholder = { Text("Search music...", color = Color.Gray) },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(15.dp)),
//                colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFF1E1E1E))
//            )
//        }
//    }
//}
//
//
//@Composable
//fun BottomNavigationBar(modifier: Modifier = Modifier) {
//    NavigationBar(containerColor = Color.Black, modifier = modifier) {
//        listOf(
//            Icons.Filled.Home to "Home",
//            painterResource(id = R.drawable.explore_icon) to "Explore",
//            painterResource(id = R.drawable.library_music_icon) to "Library"
//        ).forEach { (icon, label) ->
//            NavigationBarItem(
//                selected = label == "Home",
//                onClick = { println("$label Clicked!") },
//                icon = { when (icon) {
//                    is ImageVector -> Icon(icon, contentDescription = label, tint = Color.White)
//                    is Painter -> Icon(painter = icon, contentDescription = label, tint = Color.White)
//                    else -> error("Unsupported icon type")
//                }},
//                label = { Text(label, color = Color.White) }
//            )
//        }
//    }
//}
//
//@Composable
//fun Section(title: String, imageUrls: List<String>) {
//    Column(modifier = Modifier.padding(vertical = 12.dp)) {
//        Text(title, color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(start = 8.dp))
//        Spacer(modifier = Modifier.height(8.dp))
//        Row(modifier = Modifier.horizontalScroll(rememberScrollState()).padding(start = 8.dp)) {
//            imageUrls.forEach { imageUrl ->
//                Column(modifier = Modifier.padding(end = 8.dp)) {
//                    AsyncImage(
//                        model = imageUrl,
//                        contentDescription = "Album Art",
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(12.dp))
//                            .size(120.dp)
//                    )
//                    Text("Album Name", color = Color.White, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun CategoryButton(text: String) {
//    Button(
//        onClick = { /* TODO: Implement category filter */ },
//        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
//        modifier = Modifier.clip(RoundedCornerShape(12.dp))
//    ) {
//        Text(text, color = Color.White)
//    }
//}

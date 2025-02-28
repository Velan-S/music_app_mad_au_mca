package com.example.vibesync.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import okhttp3.OkHttpClient
import okhttp3.Request
import com.example.vibesync.R
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import android.util.Log



// Data model
data class MusicItem(val title: String, val artist: String, val plays: String, val imageUrl: String)
data class PlaylistItem(val title: String, val playlistId: String, val subtitle: String, val imageUrl: String)
class HomeViewModel : ViewModel() {
    var quickPicks = mutableStateListOf<MusicItem>()
        private set

    var thisYearInMusic = mutableStateListOf<PlaylistItem>()
        private set

    init {
        fetchMusicData()
    }

    private fun fetchMusicData() {
        viewModelScope.launch {
            val response = fetchMusicApi()
            response?.let {
                val jsonObject = JSONObject(it)
                val results = jsonObject.optJSONObject("results") ?: return@launch

                // Log entire response
                println("API Response: $jsonObject")

                // Parse Quick Picks
                results.optJSONArray("quick_picks")?.let { quickPicksJson ->
                    quickPicks.clear()
                    quickPicks.addAll(parseMusicList(quickPicksJson))
                    println("Quick Picks Loaded: ${quickPicks.size}")
                }

                val popPlaylistJson = results.optJSONArray("pop_playlists")
                println("Raw pop_playlists JSON: $popPlaylistJson")
                results.optJSONArray("pop_playlists")?.let { thisYearJson ->
                    thisYearInMusic.clear()
                    thisYearInMusic.addAll(parseThisYearMusicList(thisYearJson))
                    println("This Year in Music Loaded: ${thisYearInMusic.size}")
                }
            }
        }
    }

    fun updateThisYearMusic(newList: List<PlaylistItem>) {
        thisYearInMusic.clear()
        thisYearInMusic.addAll(newList)
    }


    private suspend fun fetchMusicApi(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://10.0.2.2:8000/music/home")
                    .get()
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    response.body?.string()
                } else null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun parseMusicList(jsonArray: JSONArray): List<MusicItem> {
        val list = mutableListOf<MusicItem>()
        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            list.add(
                MusicItem(
                    title = item.getString("title"),
                    artist = item.optString("artist", "Unknown"),
                    plays = item.optString("plays", "0"),
                    imageUrl = item.getString("thumbnail")
                )
            )
        }
        return list
    }

    private fun parseThisYearMusicList(jsonArray: JSONArray): List<PlaylistItem> {
        val list = mutableListOf<PlaylistItem>()
        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            println("Parsing Item: $item") // Log each item

            val title = item.optString("title", "Unknown Title")
            val subtitle = item.optString("subtitle", "Various Artists")
            val playlistId = item.optString("browseId", "")
            val imageUrl = item.optString("thumbnail", "")

            if (playlistId.isNotEmpty() && imageUrl.isNotEmpty()) { // Validate data
                list.add(PlaylistItem(title, playlistId, subtitle, imageUrl))
            } else {
                println("Skipping Invalid Item: $item")
            }
        }
        return list
    }
}


// Main UI
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    toggleTheme: () -> Unit,
    navigateToFullList: (String) -> Unit,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var showSearch by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // App Bar
            TopAppBar(
                title = { Text("VibeSync", fontSize = 20.sp, color = Color.White) },
                actions = {
                    IconButton(onClick = { showSearch = true }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )

            Spacer(Modifier.height(10.dp))

            // Quick Picks Section
            SongsSection(title = "Quick Picks", items = viewModel.quickPicks, navigateToFullList)

            Spacer(Modifier.height(10.dp))

            // This Year in Music Section
            PlayListSection(title = "This Year in Music", items = viewModel.thisYearInMusic, navigateToFullList)

            Spacer(Modifier.weight(1f))

            // Bottom Navigation Bar
            BottomNavigationBar()
        }
    }

    // Search Overlay
    if (showSearch) {
        SearchScreen { showSearch = false }
    }
}


// Music Section
@Composable
fun SongsSection(title: String, items: List<MusicItem>, navigateToFullList: (String) -> Unit) {
    Column(modifier = Modifier.padding(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(start = 8.dp))
            TextButton(onClick = { navigateToFullList(title) }) { Text("See All", color = Color.Cyan) }
        }

        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(items) { item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = "Album Art",
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .size(100.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = if (item.title.length > 16) item.title.take(10) + "..." else item.title,
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "${item.artist} • ${item.plays} plays",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Composable
fun PlayListSection(title: String, items: List<PlaylistItem>, navigateToFullList: (String) -> Unit) {
    Column(modifier = Modifier.padding(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(start = 8.dp))
            TextButton(onClick = { navigateToFullList(title) }) { Text("See All", color = Color.Cyan) }
        }

        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(items) { item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = "Album Art",
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .size(100.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = if (item.title.length > 16) item.title.take(10) + "..." else item.title,
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = if (item.subtitle.length > 16) item.subtitle.take(10) + "..." else {item.subtitle} ,
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


// Bottom Navigation Bar
@Composable
fun BottomNavigationBar() {
    NavigationBar(containerColor = Color.Black, modifier = Modifier.fillMaxWidth()) {
        listOf(
            Icons.Filled.Home to "Home",
            R.drawable.explore_icon to "Explore",
            R.drawable.library_music_icon to "Library"
        ).forEach { (icon, label) ->
            NavigationBarItem(
                selected = label == "Home",
                onClick = { println("$label Clicked!") },
                icon = {
                    when (icon) {
                        is ImageVector -> Icon(icon, contentDescription = label, tint = Color.White)
                        is Int -> Icon(painterResource(id = icon), contentDescription = label, tint = Color.White)
                        else -> error("Unsupported icon type")
                    }
                },
                label = { Text(label, color = Color.White) }
            )
        }
    }
}

// Search Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
        TextField(
            value = "", onValueChange = {},
            placeholder = { Text("Search music...", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp)),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFF1E1E1E))
        )
    }
}

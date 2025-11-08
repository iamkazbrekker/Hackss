package com.runanywhere.startup_hackathon20.ui.screens

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.runanywhere.startup_hackathon20.data.models.QuestModule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleVideoScreen(
    module: QuestModule,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    var isCompleted by remember { mutableStateOf(module.isCompleted) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        module.title,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C1810)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A0F0A),
                            Color(0xFF2C1810)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Video Player Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    ) {
                        if (module.videoUrl.contains("PASTE_YOUTUBE") || module.videoUrl.isEmpty()) {
                            // Placeholder for video
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF1A1A1A)),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    "📹",
                                    fontSize = 48.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Video will appear here",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Paste YouTube URL in RPGRepository",
                                    color = Color.DarkGray,
                                    fontSize = 12.sp
                                )
                            }
                        } else {
                            // WebView YouTube Player
                            val embedUrl = remember(module.videoUrl) {
                                convertToEmbedUrl(module.videoUrl, module.videoStartTime)
                            }

                            if (embedUrl.isNotEmpty()) {
                                AndroidView(
                                    factory = { context ->
                                        WebView(context).apply {
                                            layoutParams = ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT
                                            )
                                            settings.apply {
                                                javaScriptEnabled = true
                                                domStorageEnabled = true
                                                mediaPlaybackRequiresUserGesture = false
                                                loadWithOverviewMode = true
                                                useWideViewPort = true
                                                setSupportZoom(true)
                                                builtInZoomControls = false
                                                displayZoomControls = false
                                                allowContentAccess = true
                                                allowFileAccess = true
                                                javaScriptCanOpenWindowsAutomatically = true
                                                mixedContentMode =
                                                    android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                            }
                                            webChromeClient = WebChromeClient()
                                            webViewClient = object : WebViewClient() {
                                                override fun onReceivedError(
                                                    view: WebView,
                                                    request: WebResourceRequest,
                                                    error: WebResourceError
                                                ) {
                                                    super.onReceivedError(view, request, error)
                                                    // Handle error
                                                }
                                            }
                                            loadUrl(embedUrl)
                                        }
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                // Invalid video ID
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0xFF1A1A1A)),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        "❌",
                                        fontSize = 48.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        "Invalid YouTube URL",
                                        color = Color.Red,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Open in YouTube button (fallback if WebView has issues)
                if (!module.videoUrl.contains("PASTE_YOUTUBE") && module.videoUrl.isNotEmpty()) {
                    Button(
                        onClick = {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(module.videoUrl)
                            )
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E88E5)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.PlayArrow,
                                "Play",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                "Open in YouTube App",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Enemy Battle Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (module.isBoss) Color(0xFF8B0000) else Color(0xFF4A2F1F)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                if (module.isBoss) "👹 BOSS BATTLE" else "⚔️ ENEMY",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF6B6B)
                            )

                            Surface(
                                color = Color(0xFFFFD700).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "Level ${module.enemyLevel}",
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    ),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD700)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            module.enemyName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            module.enemyDescription,
                            fontSize = 14.sp,
                            color = Color(0xFFE0E0E0),
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Learning Objective Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2C1810)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            "📚 Learning Topic",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            module.topic,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Divider(color = Color(0xFF5C3D2E))

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    "Reward",
                                    fontSize = 12.sp,
                                    color = Color(0xFFC0C0C0)
                                )
                                Text(
                                    "⭐ ${module.xpReward} XP",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD700)
                                )
                            }

                            if (isCompleted) {
                                Surface(
                                    color = Color(0xFF4CAF50),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 8.dp
                                        ),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Check,
                                            "Completed",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            "Completed",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Complete Module Button
                if (!isCompleted) {
                    Button(
                        onClick = {
                            isCompleted = true
                            onComplete()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD700)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "⚔️",
                                fontSize = 20.sp
                            )
                            Text(
                                if (module.isBoss) "DEFEAT THE DEMON LORD!" else "DEFEAT ${module.enemyName.uppercase()}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2C1810)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// Helper function to extract video ID from various YouTube URL formats
private fun extractVideoId(url: String): String {
    return when {
        url.contains("youtube.com/embed/") -> {
            url.substringAfter("embed/").substringBefore("?")
        }

        url.contains("youtube.com/watch?v=") -> {
            url.substringAfter("v=").substringBefore("&")
        }

        url.contains("youtu.be/") -> {
            url.substringAfter("youtu.be/").substringBefore("?")
        }

        else -> ""
    }
}

// Helper function to convert YouTube URL to embed URL
private fun convertToEmbedUrl(url: String, startTime: Int): String {
    val videoId = extractVideoId(url)
    return "https://www.youtube.com/embed/$videoId?start=$startTime"
}

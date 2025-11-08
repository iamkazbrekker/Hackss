package com.runanywhere.startup_hackathon20.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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

data class ResourceItem(
    val title: String,
    val description: String,
    val category: String,
    val url: String,
    val icon: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourcesScreen() {
    val resources = remember {
        listOf(
            ResourceItem(
                "Khan Academy - Mathematics",
                "Comprehensive math lessons from basics to advanced",
                "Mathematics",
                "https://www.khanacademy.org/math",
                "📐"
            ),
            ResourceItem(
                "MIT OpenCourseWare",
                "Free course materials from MIT",
                "Multiple Subjects",
                "https://ocw.mit.edu",
                "🎓"
            ),
            ResourceItem(
                "Wolfram MathWorld",
                "The web's most extensive mathematics resource",
                "Mathematics",
                "https://mathworld.wolfram.com",
                "🧮"
            ),
            ResourceItem(
                "Paul's Online Math Notes",
                "Algebra, Calculus and Differential Equations",
                "Mathematics",
                "https://tutorial.math.lamar.edu",
                "📝"
            ),
            ResourceItem(
                "Brilliant.org",
                "Interactive problem-solving platform",
                "Multiple Subjects",
                "https://brilliant.org",
                "💡"
            ),
            ResourceItem(
                "Desmos Calculator",
                "Free online graphing calculator",
                "Tools",
                "https://www.desmos.com/calculator",
                "📊"
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A0F0A),
                        Color(0xFF2C1810),
                        Color(0xFF3D2417)
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4A2F1F)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📚", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Learning Resources",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                        Text(
                            "Expand your knowledge with these resources",
                            fontSize = 14.sp,
                            color = Color(0xFFC0C0C0)
                        )
                    }
                }
            }

            items(resources) { resource ->
                ResourceCard(resource)
            }
        }
    }
}

@Composable
fun ResourceCard(resource: ResourceItem) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4A2F1F)
        ),
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resource.url))
            context.startActivity(intent)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                resource.icon,
                fontSize = 40.sp,
                modifier = Modifier.padding(end = 16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    resource.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    resource.description,
                    fontSize = 13.sp,
                    color = Color(0xFFE0E0E0)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = Color(0xFFFFD700).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        resource.category,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                }
            }

            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "Open",
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

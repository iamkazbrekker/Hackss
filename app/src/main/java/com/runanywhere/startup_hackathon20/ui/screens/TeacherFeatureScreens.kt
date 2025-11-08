package com.runanywhere.startup_hackathon20.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.runanywhere.startup_hackathon20.data.models.*
import com.runanywhere.startup_hackathon20.viewmodel.EduVentureViewModel

// ==================== 1. TECH DEVELOPMENT COURSES SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechDevelopmentCoursesScreen(
    viewModel: EduVentureViewModel,
    onCourseClick: (TechDevelopmentCourse) -> Unit = {}
) {
    val techCourses by viewModel.techDevelopmentCourses.collectAsState()
    var selectedCategory by remember { mutableStateOf<TechCategory?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "🎓 Tech Development Academy",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Stats
                item {
                    TechCoursesHeaderCard(techCourses)
                }

                // Category Filter
                item {
                    Text(
                        "📚 Browse by Technology",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TechCategoryFilter(selectedCategory) { selectedCategory = it }
                }

                // Courses List
                item {
                    Text(
                        "⚔️ Available Courses",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                val filteredCourses = if (selectedCategory != null) {
                    techCourses.filter { it.category == selectedCategory }
                } else {
                    techCourses
                }

                items(filteredCourses) { course ->
                    TechCourseCard(course, onCourseClick)
                }
            }
        }
    }
}

@Composable
fun TechCoursesHeaderCard(courses: List<TechDevelopmentCourse>) {
    val enrolledCount = courses.count { it.isEnrolled }
    val completedCount = courses.count { it.isEnrolled && it.progress >= 1f }
    val totalXP = courses.filter { it.isEnrolled }.sumOf { (it.progress * it.xpReward).toInt() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A2F1F))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "🏰 Your Learning Journey",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TechStatColumn("📖", "Enrolled", "$enrolledCount")
                TechStatColumn("✅", "Completed", "$completedCount")
                TechStatColumn("⭐", "XP Earned", "$totalXP")
            }
        }
    }
}

@Composable
fun TechStatColumn(icon: String, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 28.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700)
        )
        Text(
            label,
            fontSize = 12.sp,
            color = Color(0xFFC0C0C0)
        )
    }
}

@Composable
fun TechCategoryFilter(
    selectedCategory: TechCategory?,
    onCategorySelected: (TechCategory?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            CategoryChip(
                label = "All",
                icon = "🌐",
                isSelected = selectedCategory == null,
                onClick = { onCategorySelected(null) }
            )
        }

        items(TechCategory.values().toList()) { category ->
            val (icon, label) = when (category) {
                TechCategory.ARTIFICIAL_INTELLIGENCE -> "🤖" to "AI/ML"
                TechCategory.WEB_DEVELOPMENT -> "🌐" to "Web Dev"
                TechCategory.MOBILE_DEVELOPMENT -> "📱" to "Mobile"
                TechCategory.CLOUD_COMPUTING -> "☁️" to "Cloud"
                TechCategory.DATA_SCIENCE -> "📊" to "Data Sci"
                TechCategory.CYBERSECURITY -> "🔒" to "Security"
                TechCategory.BLOCKCHAIN -> "⛓️" to "Blockchain"
                TechCategory.AR_VR -> "🥽" to "AR/VR"
                TechCategory.IOT -> "🌍" to "IoT"
                TechCategory.GAME_DEVELOPMENT -> "🎮" to "Game Dev"
                TechCategory.DEVOPS -> "⚙️" to "DevOps"
                TechCategory.UI_UX_DESIGN -> "🎨" to "UI/UX"
            }
            CategoryChip(
                label = label,
                icon = icon,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
fun CategoryChip(
    label: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        color = if (isSelected) Color(0xFFFFD700).copy(alpha = 0.3f) else Color(0xFF2C1810),
        shape = RoundedCornerShape(20.dp),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFFFFD700)) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                label,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color(0xFFFFD700) else Color(0xFFC0C0C0)
            )
        }
    }
}

@Composable
fun TechCourseCard(
    course: TechDevelopmentCourse,
    onCourseClick: (TechDevelopmentCourse) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(12.dp))
            .clickable { onCourseClick(course) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (course.isEnrolled) Color(0xFF4A2F1F) else Color(0xFF2C1810)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            course.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        "by ${course.instructor}",
                        fontSize = 12.sp,
                        color = Color(0xFF8B7355)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        course.description,
                        fontSize = 13.sp,
                        color = Color(0xFFC0C0C0),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tags
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(course.tags.take(3)) { tag ->
                    Surface(
                        color = Color(0xFFFFD700).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            tag,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            color = Color(0xFFFFD700)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoBadge("⏱️", course.duration)
                    InfoBadge("⭐", course.rating.toString())
                    InfoBadge("👥", "${course.enrolledCount}")
                }

                Surface(
                    color = when (course.difficulty) {
                        CourseDifficulty.BEGINNER -> Color(0xFF10B981)
                        CourseDifficulty.INTERMEDIATE -> Color(0xFFF59E0B)
                        CourseDifficulty.ADVANCED -> Color(0xFFEF4444)
                        CourseDifficulty.EXPERT -> Color(0xFF8B5CF6)
                    }.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        course.difficulty.name,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (course.difficulty) {
                            CourseDifficulty.BEGINNER -> Color(0xFF10B981)
                            CourseDifficulty.INTERMEDIATE -> Color(0xFFF59E0B)
                            CourseDifficulty.ADVANCED -> Color(0xFFEF4444)
                            CourseDifficulty.EXPERT -> Color(0xFF8B5CF6)
                        }
                    )
                }
            }

            // Progress bar for enrolled courses
            if (course.isEnrolled) {
                Spacer(modifier = Modifier.height(12.dp))
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${(course.progress * 100).toInt()}% Complete",
                            fontSize = 12.sp,
                            color = Color(0xFFC0C0C0)
                        )
                        Text(
                            "+${course.xpReward} XP",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { course.progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = Color(0xFFFFD700),
                        trackColor = Color(0xFF3D2417)
                    )
                }
            }
        }
    }
}

@Composable
fun InfoBadge(icon: String, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(icon, fontSize = 12.sp)
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text,
            fontSize = 11.sp,
            color = Color(0xFFC0C0C0)
        )
    }
}

// ==================== 2. RESOURCE HUB SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceHubScreen(viewModel: EduVentureViewModel) {
    val resourceItems by viewModel.resourceHubItems.collectAsState()
    var selectedCategory by remember { mutableStateOf<TechCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "📚 Resource Library",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Search Bar
                item {
                    SearchBar(searchQuery) { searchQuery = it }
                }

                // Category Filter
                item {
                    Text(
                        "🗂️ Browse Resources",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TechCategoryFilter(selectedCategory) { selectedCategory = it }
                }

                // Resources
                val filteredResources = resourceItems.filter {
                    (selectedCategory == null || it.category == selectedCategory) &&
                            (searchQuery.isEmpty() || it.title.contains(
                                searchQuery,
                                ignoreCase = true
                            ) ||
                                    it.description.contains(searchQuery, ignoreCase = true))
                }

                items(filteredResources) { resource ->
                    ResourceHubItemCard(resource) {
                        // Open URL in browser
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resource.url))
                        context.startActivity(intent)
                    }
                }

                if (filteredResources.isEmpty()) {
                    item {
                        EmptyResourceState()
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        placeholder = {
            Text("Search resources...", color = Color(0xFF8B7355))
        },
        leadingIcon = {
            Icon(Icons.Default.Search, "Search", tint = Color(0xFFFFD700))
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color(0xFF2C1810),
            unfocusedContainerColor = Color(0xFF2C1810),
            focusedBorderColor = Color(0xFFFFD700),
            unfocusedBorderColor = Color(0xFF8B7355)
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun ResourceHubItemCard(
    resource: ResourceHubItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C1810))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Icon based on type
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF4A2F1F)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    when (resource.type) {
                        ResourceHubType.VIDEO_TUTORIAL -> "🎥"
                        ResourceHubType.ARTICLE -> "📄"
                        ResourceHubType.DOCUMENTATION -> "📖"
                        ResourceHubType.COURSE -> "🎓"
                        ResourceHubType.TOOL -> "🔧"
                        ResourceHubType.BOOK -> "📚"
                        ResourceHubType.PODCAST -> "🎙️"
                        ResourceHubType.GITHUB_REPO -> "💻"
                        ResourceHubType.INTERACTIVE_DEMO -> "🎮"
                        ResourceHubType.RESEARCH_PAPER -> "📜"
                    },
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    resource.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    resource.description,
                    fontSize = 13.sp,
                    color = Color(0xFFC0C0C0),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Provider
                    Text(
                        resource.provider,
                        fontSize = 11.sp,
                        color = Color(0xFF8B7355)
                    )

                    // Free badge
                    if (resource.isFree) {
                        Surface(
                            color = Color(0xFF10B981).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                "FREE",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF10B981)
                            )
                        }
                    }

                    // Rating
                    if (resource.rating > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⭐", fontSize = 10.sp)
                            Text(
                                resource.rating.toString(),
                                fontSize = 11.sp,
                                color = Color(0xFFC0C0C0)
                            )
                        }
                    }

                    // Duration
                    resource.duration?.let {
                        Text(
                            "⏱️ $it",
                            fontSize = 11.sp,
                            color = Color(0xFFC0C0C0)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Tags
                LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(resource.tags.take(3)) { tag ->
                        Surface(
                            color = Color(0xFFFFD700).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                tag,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 10.sp,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }
                }
            }

            // Bookmark icon
            Icon(
                if (resource.bookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Bookmark",
                tint = if (resource.bookmarked) Color(0xFFFF6B6B) else Color(0xFF8B7355),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun EmptyResourceState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🔍", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No resources found",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )
            Text(
                "Try adjusting your search or filters",
                fontSize = 14.sp,
                color = Color(0xFFC0C0C0),
                textAlign = TextAlign.Center
            )
        }
    }
}

// ==================== 3. STUDENT PERFORMANCE INSIGHTS SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentPerformanceInsightsScreen(viewModel: EduVentureViewModel) {
    val insights by viewModel.studentInsights.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "📊 Performance Insights",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(insights) { insight ->
                    StudentInsightCard(insight)
                }

                if (insights.isEmpty()) {
                    item {
                        EmptyInsightsState()
                    }
                }
            }
        }
    }
}

@Composable
fun StudentInsightCard(insight: StudentPerformanceInsight) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C1810))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Student Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(Color(0xFFFFD700), Color(0xFF8B4513))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🛡️", fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            insight.studentName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "Level ${insight.overallPerformance.level} • ${insight.overallPerformance.totalXP} XP",
                            fontSize = 13.sp,
                            color = Color(0xFFC0C0C0)
                        )
                    }
                }

                // Score Badge
                Surface(
                    color = when {
                        insight.overallPerformance.averageScore >= 90 -> Color(0xFF10B981)
                        insight.overallPerformance.averageScore >= 75 -> Color(0xFFF59E0B)
                        else -> Color(0xFFEF4444)
                    }.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        "${insight.overallPerformance.averageScore.toInt()}%",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            insight.overallPerformance.averageScore >= 90 -> Color(0xFF10B981)
                            insight.overallPerformance.averageScore >= 75 -> Color(0xFFF59E0B)
                            else -> Color(0xFFEF4444)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InsightStatBox(
                    "📊",
                    "Trend",
                    when (insight.overallPerformance.performanceTrend) {
                        TrendDirection.UP -> "↗️ Up"
                        TrendDirection.DOWN -> "↘️ Down"
                        TrendDirection.STABLE -> "→ Stable"
                    }
                )
                InsightStatBox(
                    "⚔️",
                    "Quests",
                    "${insight.overallPerformance.questsCompleted}/${insight.overallPerformance.totalQuests}"
                )
                InsightStatBox(
                    "🔥",
                    "Streak",
                    "${insight.overallPerformance.currentStreak}"
                )
                InsightStatBox(
                    "📱",
                    "Engagement",
                    insight.engagementMetrics.engagementLevel.name.lowercase().capitalize()
                )
            }

            // Expanded Content
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFF8B7355).copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(16.dp))

                // Subject Performance
                Text(
                    "📚 Subject Mastery",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
                Spacer(modifier = Modifier.height(8.dp))
                insight.subjectPerformance.forEach { subject ->
                    SubjectMasteryRow(subject)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Learning Pattern
                Text(
                    "🎯 Learning Pattern",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LearningPatternInfo(insight.learningPattern)

                Spacer(modifier = Modifier.height(16.dp))

                // Recommendations
                Text(
                    "💡 Recommendations",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
                Spacer(modifier = Modifier.height(8.dp))
                insight.recommendations.forEach { recommendation ->
                    RecommendationItem(recommendation)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Expand indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Color(0xFFFFD700)
                )
            }
        }
    }
}

@Composable
fun InsightStatBox(icon: String, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 20.sp)
        Text(
            value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700)
        )
        Text(
            label,
            fontSize = 11.sp,
            color = Color(0xFF8B7355)
        )
    }
}

@Composable
fun SubjectMasteryRow(subject: SubjectPerformance) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                subject.subject,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                "${subject.mastery.name} Level",
                fontSize = 11.sp,
                color = Color(0xFF8B7355)
            )
        }

        Surface(
            color = when (subject.mastery) {
                MasteryLevel.EXPERT -> Color(0xFF8B5CF6)
                MasteryLevel.ADVANCED -> Color(0xFF10B981)
                MasteryLevel.PROFICIENT -> Color(0xFF3B82F6)
                MasteryLevel.DEVELOPING -> Color(0xFFF59E0B)
                MasteryLevel.BEGINNER -> Color(0xFFEF4444)
            }.copy(alpha = 0.2f),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                "${subject.averageScore.toInt()}%",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = when (subject.mastery) {
                    MasteryLevel.EXPERT -> Color(0xFF8B5CF6)
                    MasteryLevel.ADVANCED -> Color(0xFF10B981)
                    MasteryLevel.PROFICIENT -> Color(0xFF3B82F6)
                    MasteryLevel.DEVELOPING -> Color(0xFFF59E0B)
                    MasteryLevel.BEGINNER -> Color(0xFFEF4444)
                }
            )
        }
    }
}

@Composable
fun LearningPatternInfo(pattern: LearningPattern) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A2F1F))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            InfoRow(
                "📖 Learning Style",
                pattern.preferredLearningStyle.name.lowercase().capitalize()
            )
            InfoRow(
                "⏰ Most Active",
                pattern.mostActiveTimeOfDay.name.replace("_", " ").lowercase().capitalize()
            )
            InfoRow("⏱️ Avg Session", "${pattern.averageSessionDuration} mins")
            InfoRow("✅ Completion Rate", "${(pattern.completionRate * 100).toInt()}%")
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            fontSize = 12.sp,
            color = Color(0xFFC0C0C0)
        )
        Text(
            value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Composable
fun RecommendationItem(recommendation: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text("•", fontSize = 14.sp, color = Color(0xFFFFD700))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            recommendation,
            fontSize = 13.sp,
            color = Color(0xFFC0C0C0)
        )
    }
}

@Composable
fun EmptyInsightsState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("📊", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No student data yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )
            Text(
                "Student insights will appear here once students start completing quests",
                fontSize = 14.sp,
                color = Color(0xFFC0C0C0),
                textAlign = TextAlign.Center
            )
        }
    }
}

// ==================== 4. PROGRESS DASHBOARD SCREEN ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressDashboardScreen(viewModel: EduVentureViewModel) {
    val dashboards by viewModel.progressDashboards.collectAsState()
    val dashboard = dashboards.firstOrNull()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "📈 Progress Dashboard",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C1810)
                )
            )
        }
    ) { padding ->
        if (dashboard != null) {
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Class Overview
                    item {
                        ClassOverviewCard(dashboard)
                    }

                    // Performance Distribution
                    item {
                        PerformanceDistributionCard(dashboard.performanceDistribution)
                    }

                    // Weekly Progress
                    item {
                        WeeklyProgressCard(dashboard.weeklyProgress)
                    }

                    // Top Performers
                    item {
                        TopPerformersCard(dashboard.topPerformers)
                    }

                    // Alerts
                    item {
                        StudentAlertsCard(dashboard.studentsNeedingHelp)
                    }

                    // Subject Breakdown
                    item {
                        SubjectBreakdownCard(dashboard.subjectBreakdown)
                    }
                }
            }
        } else {
            EmptyDashboardState(Modifier.padding(padding))
        }
    }
}

@Composable
fun ClassOverviewCard(dashboard: ProgressDashboard) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A2F1F))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "🏛️ ${dashboard.className}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DashboardStatBox("👥", "Total", "${dashboard.totalStudents}")
                DashboardStatBox("✅", "Active", "${dashboard.activeStudents}")
                DashboardStatBox("📊", "Avg Score", "${dashboard.averageClassScore.toInt()}%")
                DashboardStatBox(
                    "⚔️",
                    "Avg Level",
                    String.format("%.1f", dashboard.averageClassLevel)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Engagement Bar
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Class Engagement",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Text(
                        "${dashboard.classEngagement.toInt()}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { dashboard.classEngagement / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = Color(0xFFFFD700),
                    trackColor = Color(0xFF3D2417)
                )
            }
        }
    }
}

@Composable
fun DashboardStatBox(icon: String, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 24.sp)
        Text(
            value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700)
        )
        Text(
            label,
            fontSize = 11.sp,
            color = Color(0xFFC0C0C0)
        )
    }
}

@Composable
fun PerformanceDistributionCard(distribution: PerformanceDistribution) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C1810))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "📊 Performance Distribution",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )

            Spacer(modifier = Modifier.height(16.dp))

            DistributionBar("Excellent (90-100%)", distribution.excellent, Color(0xFF10B981))
            DistributionBar("Good (75-89%)", distribution.good, Color(0xFF3B82F6))
            DistributionBar("Average (60-74%)", distribution.average, Color(0xFFF59E0B))
            DistributionBar(
                "Needs Improvement (<60%)",
                distribution.needsImprovement + distribution.struggling,
                Color(0xFFEF4444)
            )
        }
    }
}

@Composable
fun DistributionBar(label: String, count: Int, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            fontSize = 13.sp,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        Surface(
            color = color.copy(alpha = 0.3f),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                "$count",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
fun WeeklyProgressCard(weeklyData: List<WeeklyProgressData>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C1810))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "📅 Weekly Progress Trend",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )

            Spacer(modifier = Modifier.height(16.dp))

            weeklyData.forEach { week ->
                WeekProgressRow(week)
            }
        }
    }
}

@Composable
fun WeekProgressRow(week: WeeklyProgressData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Week ${week.weekNumber}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                "${week.averageScore.toInt()}%",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "⚔️ ${week.questsCompleted}",
                fontSize = 11.sp,
                color = Color(0xFFC0C0C0)
            )
            Text(
                "👥 ${week.activeStudents}",
                fontSize = 11.sp,
                color = Color(0xFFC0C0C0)
            )
            Text(
                "⭐ ${week.totalXPEarned}",
                fontSize = 11.sp,
                color = Color(0xFFC0C0C0)
            )
        }
    }
}

@Composable
fun TopPerformersCard(performers: List<TopPerformer>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C1810))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "🏆 Top Performers",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )

            Spacer(modifier = Modifier.height(12.dp))

            performers.forEach { performer ->
                TopPerformerRow(performer)
            }
        }
    }
}

@Composable
fun TopPerformerRow(performer: TopPerformer) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        when (performer.rank) {
                            1 -> Color(0xFFFFD700)
                            2 -> Color(0xFFC0C0C0)
                            3 -> Color(0xFFCD7F32)
                            else -> Color(0xFF8B7355)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "#${performer.rank}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    performer.studentName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "Lv ${performer.level} • ${performer.questsCompleted} quests",
                    fontSize = 11.sp,
                    color = Color(0xFF8B7355)
                )
            }
        }

        Surface(
            color = Color(0xFF10B981).copy(alpha = 0.2f),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                "${performer.score.toInt()}%",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF10B981)
            )
        }
    }
}

@Composable
fun StudentAlertsCard(alerts: List<StudentAlert>) {
    if (alerts.isEmpty()) return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C1810))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "⚠️ Students Needing Attention",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )

            Spacer(modifier = Modifier.height(12.dp))

            alerts.forEach { alert ->
                StudentAlertRow(alert)
            }
        }
    }
}

@Composable
fun StudentAlertRow(alert: StudentAlert) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (alert.severity) {
                AlertSeverity.CRITICAL -> Color(0xFFEF4444)
                AlertSeverity.HIGH -> Color(0xFFF59E0B)
                AlertSeverity.MEDIUM -> Color(0xFF3B82F6)
                AlertSeverity.LOW -> Color(0xFF8B7355)
            }.copy(alpha = 0.15f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        alert.studentName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        alert.message,
                        fontSize = 12.sp,
                        color = Color(0xFFC0C0C0),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Surface(
                    color = when (alert.severity) {
                        AlertSeverity.CRITICAL -> Color(0xFFEF4444)
                        AlertSeverity.HIGH -> Color(0xFFF59E0B)
                        AlertSeverity.MEDIUM -> Color(0xFF3B82F6)
                        AlertSeverity.LOW -> Color(0xFF8B7355)
                    }.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        alert.severity.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (alert.severity) {
                            AlertSeverity.CRITICAL -> Color(0xFFEF4444)
                            AlertSeverity.HIGH -> Color(0xFFF59E0B)
                            AlertSeverity.MEDIUM -> Color(0xFF3B82F6)
                            AlertSeverity.LOW -> Color(0xFF8B7355)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SubjectBreakdownCard(subjects: List<SubjectStats>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C1810))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "📚 Subject Performance",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )

            Spacer(modifier = Modifier.height(12.dp))

            subjects.forEach { subject ->
                SubjectStatsRow(subject)
            }
        }
    }
}

@Composable
fun SubjectStatsRow(subject: SubjectStats) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                subject.subject,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                "${subject.averageScore.toInt()}%",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "⚔️ ${subject.completedQuests}/${subject.totalQuests}",
                fontSize = 11.sp,
                color = Color(0xFFC0C0C0)
            )
            Text(
                "🌟 ${subject.studentsExcelling} excelling",
                fontSize = 11.sp,
                color = Color(0xFF10B981)
            )
            Text(
                "⚠️ ${subject.studentsStruggling} struggling",
                fontSize = 11.sp,
                color = Color(0xFFEF4444)
            )
        }
    }
}

@Composable
fun EmptyDashboardState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A0F0A),
                        Color(0xFF2C1810)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text("📈", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No dashboard data",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Dashboard analytics will appear here once your classes are active",
                fontSize = 14.sp,
                color = Color(0xFFC0C0C0),
                textAlign = TextAlign.Center
            )
        }
    }
}

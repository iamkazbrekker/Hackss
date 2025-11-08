package com.runanywhere.startup_hackathon20.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.runanywhere.startup_hackathon20.data.models.*
import com.runanywhere.startup_hackathon20.viewmodel.EduVentureViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RPGStudentScreenWithNav(
    knightProfile: KnightProfile?,
    routes: List<LearningRoute>,
    onModuleClick: (QuestModule) -> Unit,
    viewModel: EduVentureViewModel,
    onLogout: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF2C1810),
                contentColor = Color(0xFFFFD700)
            ) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            "Home",
                            tint = if (selectedTab == 0) Color(0xFFFFD700) else Color(0xFF8B7355)
                        )
                    },
                    label = {
                        Text(
                            "Home",
                            color = if (selectedTab == 0) Color(0xFFFFD700) else Color(0xFF8B7355),
                            fontSize = 11.sp
                        )
                    },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFFFD700),
                        selectedTextColor = Color(0xFFFFD700),
                        indicatorColor = Color(0xFF4A2F1F),
                        unselectedIconColor = Color(0xFF8B7355),
                        unselectedTextColor = Color(0xFF8B7355)
                    )
                )

                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.Default.Star,
                            "Leaderboard",
                            tint = if (selectedTab == 1) Color(0xFFFFD700) else Color(0xFF8B7355)
                        )
                    },
                    label = {
                        Text(
                            "Leaderboard",
                            color = if (selectedTab == 1) Color(0xFFFFD700) else Color(0xFF8B7355),
                            fontSize = 11.sp
                        )
                    },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFFFD700),
                        selectedTextColor = Color(0xFFFFD700),
                        indicatorColor = Color(0xFF4A2F1F),
                        unselectedIconColor = Color(0xFF8B7355),
                        unselectedTextColor = Color(0xFF8B7355)
                    )
                )

                NavigationBarItem(
                    icon = {
                        Text(
                            "🤖",
                            fontSize = 24.sp
                        )
                    },
                    label = {
                        Text(
                            "Sir Remi",
                            color = if (selectedTab == 2) Color(0xFFFFD700) else Color(0xFF8B7355),
                            fontSize = 11.sp
                        )
                    },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFFFD700),
                        selectedTextColor = Color(0xFFFFD700),
                        indicatorColor = Color(0xFF4A2F1F),
                        unselectedIconColor = Color(0xFF8B7355),
                        unselectedTextColor = Color(0xFF8B7355)
                    )
                )

                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.Default.Menu,
                            "Resources",
                            tint = if (selectedTab == 3) Color(0xFFFFD700) else Color(0xFF8B7355)
                        )
                    },
                    label = {
                        Text(
                            "Resources",
                            color = if (selectedTab == 3) Color(0xFFFFD700) else Color(0xFF8B7355),
                            fontSize = 11.sp
                        )
                    },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFFFD700),
                        selectedTextColor = Color(0xFFFFD700),
                        indicatorColor = Color(0xFF4A2F1F),
                        unselectedIconColor = Color(0xFF8B7355),
                        unselectedTextColor = Color(0xFF8B7355)
                    )
                )

                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            "Profile",
                            tint = if (selectedTab == 4) Color(0xFFFFD700) else Color(0xFF8B7355)
                        )
                    },
                    label = {
                        Text(
                            "Profile",
                            color = if (selectedTab == 4) Color(0xFFFFD700) else Color(0xFF8B7355),
                            fontSize = 11.sp
                        )
                    },
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFFFD700),
                        selectedTextColor = Color(0xFFFFD700),
                        indicatorColor = Color(0xFF4A2F1F),
                        unselectedIconColor = Color(0xFF8B7355),
                        unselectedTextColor = Color(0xFF8B7355)
                    )
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                0 -> RPGStudentScreen(
                    knightProfile = knightProfile,
                    routes = routes,
                    onModuleClick = onModuleClick,
                    onProfileClick = { selectedTab = 4 }
                )

                1 -> LeaderboardScreen(viewModel = viewModel)
                2 -> SirRemiChatScreen(
                    viewModel = viewModel,
                    onBack = { selectedTab = 0 }
                )

                3 -> ResourcesScreen()
                4 -> ProfileDashboardScreen(
                    knightProfile = knightProfile,
                    viewModel = viewModel,
                    onLogout = onLogout
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RPGStudentScreen(
    knightProfile: KnightProfile?,
    routes: List<LearningRoute>,
    onModuleClick: (QuestModule) -> Unit,
    onProfileClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "⚔️ Knight's Journey",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            Icons.Default.Person,
                            "Profile",
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
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Knight Profile Card
                item {
                    knightProfile?.let {
                        KnightProfileCard(it)
                    }
                }

                // Route Selection
                items(routes) { route ->
                    RouteCard(route, onModuleClick)
                }
            }
        }
    }
}

@Composable
fun KnightProfileCard(knight: KnightProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4A2F1F)
        )
    ) {
        Box {
            // Background pattern
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF5C3D2E),
                                Color(0xFF4A2F1F),
                                Color(0xFF5C3D2E)
                            )
                        )
                    )
            )

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
                    Column {
                        Text(
                            "🛡️ ${knight.knightName}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                        Text(
                            knight.rank.title,
                            fontSize = 16.sp,
                            color = Color(0xFFC0C0C0),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Rank Badge
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFFFFD700),
                                        Color(0xFFDAA520),
                                        Color(0xFF8B4513)
                                    )
                                )
                            )
                            .border(3.dp, Color(0xFF4A2F1F), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "⭐",
                                fontSize = 20.sp
                            )
                            Text(
                                "${knight.totalXP}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                "XP",
                                fontSize = 10.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // HP Bar
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "❤️ HP",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B6B)
                        )
                        Text(
                            "${knight.currentHP}/${knight.maxHP}",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { knight.currentHP.toFloat() / knight.maxHP },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .border(2.dp, Color(0xFF8B4513), RoundedCornerShape(6.dp)),
                        color = Color(0xFFFF6B6B),
                        trackColor = Color(0xFF3D2417)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatBox("⚔️", "Defeats", "${knight.defeatedEnemies.size}")
                    StatBox("🏅", "Badges", "${knight.badges.size}")
                    StatBox("🗡️", knight.equippedWeapon, "")
                }
            }
        }
    }
}

@Composable
fun StatBox(icon: String, label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(icon, fontSize = 20.sp)
        if (value.isNotEmpty()) {
            Text(
                value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )
        }
        Text(
            label,
            fontSize = 11.sp,
            color = Color(0xFFC0C0C0)
        )
    }
}

@Composable
fun RouteCard(
    route: LearningRoute,
    onModuleClick: (QuestModule) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C1810)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Route Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "🗺️ ${route.routeName}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                    Text(
                        route.subject,
                        fontSize = 14.sp,
                        color = Color(0xFFC0C0C0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                route.description,
                fontSize = 14.sp,
                color = Color(0xFFE0E0E0),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "⚔️ Quest Modules",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Modules as a quest path
            route.modules.forEachIndexed { index, module ->
                ModuleQuestCard(
                    module = module,
                    isLast = index == route.modules.size - 1,
                    onClick = { onModuleClick(module) }
                )

                if (index < route.modules.size - 1) {
                    // Path connector
                    Box(
                        modifier = Modifier
                            .padding(start = 40.dp)
                            .width(4.dp)
                            .height(30.dp)
                            .background(
                                if (module.isCompleted) Color(0xFF4CAF50)
                                else Color(0xFF666666)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun ModuleQuestCard(
    module: QuestModule,
    isLast: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                module.isBoss -> Color(0xFF8B0000)
                module.isCompleted -> Color(0xFF2E7D32)
                else -> Color(0xFF4A2F1F)
            }
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Module Number Badge
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        if (module.isCompleted) Color(0xFF4CAF50)
                        else if (module.isBoss) Color(0xFFFF4444)
                        else Color(0xFF8B4513)
                    )
                    .border(2.dp, Color(0xFFFFD700), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (module.isCompleted) {
                    Text("✓", fontSize = 24.sp, color = Color.White)
                } else if (module.isBoss) {
                    Text("👹", fontSize = 24.sp)
                } else {
                    Text(
                        "${module.moduleNumber}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    module.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (module.isBoss) Color(0xFFFFD700) else Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    "🗡️ ${module.enemyName}",
                    fontSize = 14.sp,
                    color = Color(0xFFFF6B6B),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    module.enemyDescription,
                    fontSize = 12.sp,
                    color = Color(0xFFE0E0E0),
                    lineHeight = 16.sp,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Level Badge
                    Surface(
                        color = Color(0xFF8B4513).copy(alpha = 0.3f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            "Lv ${module.enemyLevel}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                    }

                    // XP Reward
                    Surface(
                        color = Color(0xFFFFD700).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            "⭐ +${module.xpReward} XP",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                    }
                }
            }

            // Arrow indicator
            if (!module.isCompleted) {
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "Start",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

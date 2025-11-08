package com.runanywhere.startup_hackathon20.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.runanywhere.startup_hackathon20.data.models.KnightProfile
import com.runanywhere.startup_hackathon20.viewmodel.EduVentureViewModel

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val region: String,
    val xp: Int,
    val level: Int,
    val weapon: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(viewModel: EduVentureViewModel? = null) {
    var leaderboard by remember { mutableStateOf<List<KnightProfile>>(emptyList()) }

    LaunchedEffect(Unit) {
        viewModel?.let {
            leaderboard = it.getLeaderboard()
        }
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
                // Header
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
                        Text(
                            "🏆",
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "🏆 Leaderboard",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                        Text(
                            "Top Knights Across All Kingdoms",
                            fontSize = 14.sp,
                            color = Color(0xFFC0C0C0)
                        )
                    }
                }
            }

            itemsIndexed(leaderboard) { index, knight ->
                KnightLeaderboardCard(knight, index + 1)
            }
        }
    }
}

@Composable
fun KnightLeaderboardCard(knight: KnightProfile, rank: Int) {
    val backgroundColor = when (rank) {
        1 -> Color(0xFFFFD700).copy(alpha = 0.2f)
        2 -> Color(0xFFC0C0C0).copy(alpha = 0.2f)
        3 -> Color(0xFFCD7F32).copy(alpha = 0.2f)
        else -> Color(0xFF4A2F1F)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank Badge
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        when (rank) {
                            1 -> Color(0xFFFFD700)
                            2 -> Color(0xFFC0C0C0)
                            3 -> Color(0xFFCD7F32)
                            else -> Color(0xFF8B4513)
                        }
                    )
                    .border(2.dp, Color(0xFF2C1810), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    when (rank) {
                        1 -> "🥇"
                        2 -> "🥈"
                        3 -> "🥉"
                        else -> "#$rank"
                    },
                    fontSize = if (rank <= 3) 24.sp else 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (rank > 3) Color.White else Color.Black
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    knight.knightName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "📍 ${knight.region}",
                    fontSize = 13.sp,
                    color = Color(0xFFC0C0C0)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "⭐ ${knight.totalXP} XP",
                        fontSize = 12.sp,
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "🗡️ ${knight.equippedWeapon}",
                        fontSize = 12.sp,
                        color = Color(0xFFC0C0C0)
                    )
                }
            }

            // Level Badge
            Surface(
                color = Color(0xFFFFD700).copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    knight.rank.title,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
            }
        }
    }
}

@Composable
fun LeaderboardCard(entry: LeaderboardEntry, index: Int) {
    val backgroundColor = when (entry.rank) {
        1 -> Color(0xFFFFD700).copy(alpha = 0.2f)
        2 -> Color(0xFFC0C0C0).copy(alpha = 0.2f)
        3 -> Color(0xFFCD7F32).copy(alpha = 0.2f)
        else -> Color(0xFF4A2F1F)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank Badge
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        when (entry.rank) {
                            1 -> Color(0xFFFFD700)
                            2 -> Color(0xFFC0C0C0)
                            3 -> Color(0xFFCD7F32)
                            else -> Color(0xFF8B4513)
                        }
                    )
                    .border(2.dp, Color(0xFF2C1810), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    when (entry.rank) {
                        1 -> "🥇"
                        2 -> "🥈"
                        3 -> "🥉"
                        else -> "#${entry.rank}"
                    },
                    fontSize = if (entry.rank <= 3) 24.sp else 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (entry.rank > 3) Color.White else Color.Black
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    entry.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "📍 ${entry.region}",
                    fontSize = 13.sp,
                    color = Color(0xFFC0C0C0)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "⭐ ${entry.xp} XP",
                        fontSize = 12.sp,
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "🗡️ ${entry.weapon}",
                        fontSize = 12.sp,
                        color = Color(0xFFC0C0C0)
                    )
                }
            }

            // Level Badge
            Surface(
                color = Color(0xFFFFD700).copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Lv ${entry.level}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
            }
        }
    }
}

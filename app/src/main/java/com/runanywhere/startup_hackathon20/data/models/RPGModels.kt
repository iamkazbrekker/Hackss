package com.runanywhere.startup_hackathon20.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "knight_profiles")
data class KnightProfile(
    @PrimaryKey val id: String,
    val studentId: String, // For login
    val passwordHash: String, // Hashed password
    val knightName: String,
    val rank: KnightRank,
    val totalXP: Int,
    val currentHP: Int,
    val maxHP: Int,
    val defeatedEnemies: List<String>,
    val unlockedRoutes: List<String>,
    val badges: List<Badge>,
    val equippedWeapon: String = "Wooden Sword",
    val equippedArmor: String = "Leather Armor",
    val email: String = "",
    val phoneNumber: String = "",
    val region: String = "North Kingdom" // For leaderboard
)

enum class KnightRank(val title: String, val xpRequired: Int) {
    NOVICE("Novice", 0),
    SQUIRE("Squire", 100),
    KNIGHT("Knight", 300),
    HERO("Hero", 600)
}

@Serializable
data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val unlockedAt: Long
)

@Serializable
@Entity(tableName = "learning_routes")
data class LearningRoute(
    @PrimaryKey val id: String,
    val subject: String,
    val routeName: String,
    val description: String,
    val backgroundTheme: RouteTheme,
    val modules: List<QuestModule>,
    val finalBoss: String,
    val isUnlocked: Boolean = true
)

enum class RouteTheme {
    FOREST,
    MOUNTAIN,
    DESERT,
    CASTLE,
    DUNGEON,
    MYSTIC
}

@Serializable
data class QuestModule(
    val id: String,
    val moduleNumber: Int,
    val title: String,
    val topic: String,
    val enemyName: String,
    val enemyDescription: String,
    val enemyLevel: Int,
    val xpReward: Int,
    val videoUrl: String,
    val videoStartTime: Int = 0, // in seconds
    val videoEndTime: Int? = null, // in seconds, null means play till end
    val isCompleted: Boolean = false,
    val isBoss: Boolean = false
)

@Serializable
data class VideoLesson(
    val moduleId: String,
    val youtubeVideoId: String,
    val startTimeSeconds: Int,
    val endTimeSeconds: Int?,
    val title: String,
    val description: String
)

// Combat result data
@Serializable
data class CombatResult(
    val moduleId: String,
    val enemyName: String,
    val victory: Boolean,
    val xpEarned: Int,
    val damageDealt: Int,
    val damageTaken: Int
)

// Room Type Converters
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromBadgeList(value: List<Badge>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toBadgeList(value: String): List<Badge> {
        val listType = object : TypeToken<List<Badge>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromKnightRank(value: KnightRank): String {
        return value.name
    }

    @TypeConverter
    fun toKnightRank(value: String): KnightRank {
        return KnightRank.valueOf(value)
    }

    @TypeConverter
    fun fromModuleList(value: List<QuestModule>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toModuleList(value: String): List<QuestModule> {
        val listType = object : TypeToken<List<QuestModule>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromRouteTheme(value: RouteTheme): String {
        return value.name
    }

    @TypeConverter
    fun toRouteTheme(value: String): RouteTheme {
        return RouteTheme.valueOf(value)
    }
}

package com.runanywhere.startup_hackathon20.data.repository

import android.content.Context
import com.runanywhere.startup_hackathon20.data.database.AppDatabase
import com.runanywhere.startup_hackathon20.data.models.*
import com.runanywhere.startup_hackathon20.utils.SecurityUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class RPGRepository(context: Context) {

    private val database = AppDatabase.getDatabase(context)
    private val studentDao = database.studentDao()

    private val _knightProfile = MutableStateFlow<KnightProfile?>(null)
    val knightProfile: StateFlow<KnightProfile?> = _knightProfile

    private val _learningRoutes = MutableStateFlow<List<LearningRoute>>(emptyList())
    val learningRoutes: StateFlow<List<LearningRoute>> = _learningRoutes

    // Hardcoded Google Drive folder ID
    val DRIVE_FOLDER_ID = "1sFgl7wGTuAGWRsQi-sNYW4Me-VrKmD9J" // Replace with actual folder ID

    init {
        // Initialize with default routes if database is empty
    }

    suspend fun initializeDatabase() {
        // Check if database has routes, if not, insert defaults
        val routes = studentDao.getAllRoutes()
        if (routes.isEmpty()) {
            studentDao.insertAllRoutes(listOf(createMathRoute()))
        }

        // Check if database has students, if not, create demo accounts
        val students = studentDao.getLeaderboard()
        if (students.isEmpty()) {
            createDemoAccounts()
        }
    }

    suspend fun login(studentId: String, password: String): KnightProfile? {
        val passwordHash = SecurityUtils.hashPassword(password)
        val knight = studentDao.login(studentId, passwordHash)
        if (knight != null) {
            _knightProfile.value = knight
            loadRoutesForStudent(knight.id)
        }
        return knight
    }

    suspend fun register(
        studentId: String,
        password: String,
        knightName: String,
        email: String = "",
        phoneNumber: String = ""
    ): Result<KnightProfile> {
        // Check if student ID already exists
        val existing = studentDao.getStudentByStudentId(studentId)
        if (existing != null) {
            return Result.failure(Exception("Student ID already exists"))
        }

        val newKnight = KnightProfile(
            id = UUID.randomUUID().toString(),
            studentId = studentId,
            passwordHash = SecurityUtils.hashPassword(password),
            knightName = knightName,
            rank = KnightRank.NOVICE,
            totalXP = 0,
            currentHP = 100,
            maxHP = 100,
            defeatedEnemies = emptyList(),
            unlockedRoutes = listOf("math_route"),
            badges = emptyList(),
            email = email,
            phoneNumber = phoneNumber
        )

        studentDao.insertStudent(newKnight)
        _knightProfile.value = newKnight
        loadRoutesForStudent(newKnight.id)

        return Result.success(newKnight)
    }

    private suspend fun loadRoutesForStudent(studentId: String) {
        val routes = studentDao.getAllRoutes()
        _learningRoutes.value = routes
    }

    suspend fun completeModule(moduleId: String) {
        val knight = _knightProfile.value ?: return
        val routes = _learningRoutes.value.toMutableList()
        val routeIndex = routes.indexOfFirst { route ->
            route.modules.any { it.id == moduleId }
        }

        if (routeIndex != -1) {
            val route = routes[routeIndex]
            val updatedModules = route.modules.map { module ->
                if (module.id == moduleId) {
                    earnXP(module.xpReward)
                    addDefeatedEnemy(module.enemyName)
                    updateWeapon() // Check for weapon upgrade
                    module.copy(isCompleted = true)
                } else {
                    module
                }
            }
            routes[routeIndex] = route.copy(modules = updatedModules)

            // Update database
            studentDao.updateRoute(routes[routeIndex])
            _learningRoutes.value = routes
        }
    }

    private suspend fun earnXP(amount: Int) {
        _knightProfile.value?.let { knight ->
            val newXP = knight.totalXP + amount
            val newRank = calculateRank(newXP)
            val updated = knight.copy(
                totalXP = newXP,
                rank = newRank
            )
            _knightProfile.value = updated
            studentDao.updateStudent(updated)
        }
    }

    private fun calculateRank(xp: Int): KnightRank {
        return when {
            xp >= 600 -> KnightRank.HERO
            xp >= 300 -> KnightRank.KNIGHT
            xp >= 100 -> KnightRank.SQUIRE
            else -> KnightRank.NOVICE
        }
    }

    private suspend fun updateWeapon() {
        _knightProfile.value?.let { knight ->
            val completedCount = knight.defeatedEnemies.size
            val newWeapon = when {
                completedCount >= 5 -> "Diamond Sword"
                completedCount >= 3 -> "Golden Sword"
                completedCount >= 1 -> "Iron Sword"
                else -> "Wooden Sword"
            }
            val updated = knight.copy(equippedWeapon = newWeapon)
            _knightProfile.value = updated
            studentDao.updateStudent(updated)
        }
    }

    private suspend fun addDefeatedEnemy(enemyName: String) {
        _knightProfile.value?.let { knight ->
            if (!knight.defeatedEnemies.contains(enemyName)) {
                val updated = knight.copy(
                    defeatedEnemies = knight.defeatedEnemies + enemyName
                )
                _knightProfile.value = updated
                studentDao.updateStudent(updated)
            }
        }
    }

    suspend fun unlockBadge(badge: Badge) {
        _knightProfile.value?.let { knight ->
            val updated = knight.copy(
                badges = knight.badges + badge
            )
            _knightProfile.value = updated
            studentDao.updateStudent(updated)
        }
    }

    suspend fun updateProfile(name: String, email: String, phone: String) {
        _knightProfile.value?.let { knight ->
            studentDao.updateProfile(knight.id, name, email, phone)
            val updated = knight.copy(
                knightName = name,
                email = email,
                phoneNumber = phone
            )
            _knightProfile.value = updated
        }
    }

    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit> {
        val knight = _knightProfile.value ?: return Result.failure(Exception("Not logged in"))

        if (!SecurityUtils.verifyPassword(oldPassword, knight.passwordHash)) {
            return Result.failure(Exception("Incorrect old password"))
        }

        val newHash = SecurityUtils.hashPassword(newPassword)
        studentDao.updatePassword(knight.id, newHash)

        val updated = knight.copy(passwordHash = newHash)
        _knightProfile.value = updated

        return Result.success(Unit)
    }

    suspend fun getLeaderboard(): List<KnightProfile> {
        return studentDao.getLeaderboard()
    }

    fun logout() {
        _knightProfile.value = null
        _learningRoutes.value = emptyList()
    }

    private fun createMathRoute(): LearningRoute {
        return LearningRoute(
            id = "math_route",
            subject = "Mathematics",
            routeName = "The Path of Numbers",
            description = "Journey through the mystical realm of mathematics and defeat the dark forces that threaten the kingdom!",
            backgroundTheme = RouteTheme.MYSTIC,
            finalBoss = "The Demon Lord of Mathematics",
            modules = listOf(
                QuestModule(
                    id = "math_module_1",
                    moduleNumber = 1,
                    title = "Module 1: Sets",
                    topic = "Sets - The Foundation",
                    enemyName = "The Necromancer of Sets",
                    enemyDescription = "The Necromancer of Sets has been raiding the village, capturing townsfolk in his mysterious collections. Complete this module to save the villagers and learn the ancient art of Sets!",
                    enemyLevel = 5,
                    xpReward = 50,
                    videoUrl = "https://www.youtube.com/embed/jKUpw3TyjHI",
                    videoStartTime = 0,
                    videoEndTime = 1008
                ),
                QuestModule(
                    id = "math_module_2",
                    moduleNumber = 2,
                    title = "Module 2: Relations and Functions",
                    topic = "Relations and Functions",
                    enemyName = "The Sorceress of Relations",
                    enemyDescription = "The Sorceress has cursed the land, breaking all connections between the kingdoms. Master Relations and Functions to restore harmony and defeat her dark magic!",
                    enemyLevel = 10,
                    xpReward = 75,
                    videoUrl = "PASTE_YOUTUBE_URL_HERE_FOR_RELATIONS",
                    videoStartTime = 0,
                    videoEndTime = null
                ),
                QuestModule(
                    id = "math_module_3",
                    moduleNumber = 3,
                    title = "Module 3: Trigonometric Functions",
                    topic = "Trigonometric Functions",
                    enemyName = "The Triangle Titan",
                    enemyDescription = "A colossal Titan made of triangles terrorizes the mountain pass. Learn the secrets of Trigonometric Functions to measure your way to victory!",
                    enemyLevel = 15,
                    xpReward = 100,
                    videoUrl = "PASTE_YOUTUBE_URL_HERE_FOR_TRIGONOMETRY",
                    videoStartTime = 0,
                    videoEndTime = null
                ),
                QuestModule(
                    id = "math_module_4",
                    moduleNumber = 4,
                    title = "Module 4: Complex Numbers",
                    topic = "Complex Numbers",
                    enemyName = "The Phantom of Imaginary Realm",
                    enemyDescription = "A ghostly Phantom dwells in the realm between real and imaginary. Unravel the mysteries of Complex Numbers to banish this spectral menace!",
                    enemyLevel = 20,
                    xpReward = 125,
                    videoUrl = "PASTE_YOUTUBE_URL_HERE_FOR_COMPLEX_NUMBERS",
                    videoStartTime = 0,
                    videoEndTime = null
                ),
                QuestModule(
                    id = "math_module_5",
                    moduleNumber = 5,
                    title = "Module 5: Quadratic Functions",
                    topic = "Quadratic Functions",
                    enemyName = "The Parabola Dragon",
                    enemyDescription = "A mighty Dragon whose flight path curves through the sky. Master Quadratic Functions to predict its movements and strike it down!",
                    enemyLevel = 25,
                    xpReward = 150,
                    videoUrl = "PASTE_YOUTUBE_URL_HERE_FOR_QUADRATIC",
                    videoStartTime = 0,
                    videoEndTime = null
                ),
                QuestModule(
                    id = "math_module_6",
                    moduleNumber = 6,
                    title = "Module 6: Linear Inequalities",
                    topic = "Linear Inequalities",
                    enemyName = "The Demon Lord of Mathematics",
                    enemyDescription = "The ultimate evil has risen! The Demon Lord of Mathematics threatens to plunge the world into eternal darkness. Complete this final module and master Linear Inequalities to bring back light to the world!",
                    enemyLevel = 30,
                    xpReward = 200,
                    videoUrl = "PASTE_YOUTUBE_URL_HERE_FOR_LINEAR_INEQUALITIES",
                    videoStartTime = 0,
                    videoEndTime = null,
                    isBoss = true
                )
            )
        )
    }

    private suspend fun createDemoAccounts() {
        // Create 10 demo accounts for leaderboard
        val demoAccounts = listOf(
            KnightProfile(
                "1",
                "STU001",
                SecurityUtils.hashPassword("password123"),
                "Arthur Pendragon",
                KnightRank.HERO,
                850,
                100,
                100,
                listOf("Necromancer", "Sorceress", "Titan", "Phantom", "Dragon", "Demon Lord"),
                listOf("math_route"),
                emptyList(),
                "Diamond Sword",
                "Legendary Armor",
                "arthur@example.com",
                "123-456-7890",
                "North Kingdom"
            ),
            KnightProfile(
                "2",
                "STU002",
                SecurityUtils.hashPassword("password123"),
                "Lancelot the Brave",
                KnightRank.KNIGHT,
                520,
                100,
                100,
                listOf("Necromancer", "Sorceress", "Titan"),
                listOf("math_route"),
                emptyList(),
                "Golden Sword",
                "Steel Armor",
                "lancelot@example.com",
                "",
                "East Empire"
            ),
            KnightProfile(
                "3",
                "STU003",
                SecurityUtils.hashPassword("password123"),
                "Guinevere",
                KnightRank.KNIGHT,
                480,
                100,
                100,
                listOf("Necromancer", "Sorceress", "Titan"),
                listOf("math_route"),
                emptyList(),
                "Golden Sword",
                "Enchanted Robes",
                "guin@example.com",
                "",
                "West Realm"
            ),
            KnightProfile(
                "4",
                "STU004",
                SecurityUtils.hashPassword("password123"),
                "Merlin the Wise",
                KnightRank.SQUIRE,
                280,
                100,
                100,
                listOf("Necromancer", "Sorceress"),
                listOf("math_route"),
                emptyList(),
                "Iron Sword",
                "Mage Robes",
                "",
                "",
                "South Dominion"
            ),
            KnightProfile(
                "5",
                "STU005",
                SecurityUtils.hashPassword("password123"),
                "Gawain",
                KnightRank.SQUIRE,
                245,
                100,
                100,
                listOf("Necromancer", "Sorceress"),
                listOf("math_route"),
                emptyList(),
                "Iron Sword",
                "Leather Armor",
                "",
                "",
                "North Kingdom"
            ),
            KnightProfile(
                "6",
                "STU006",
                SecurityUtils.hashPassword("password123"),
                "Tristan",
                KnightRank.SQUIRE,
                220,
                100,
                100,
                listOf("Necromancer"),
                listOf("math_route"),
                emptyList(),
                "Iron Sword",
                "Leather Armor",
                "",
                "",
                "East Empire"
            ),
            KnightProfile(
                "7",
                "STU007",
                SecurityUtils.hashPassword("password123"),
                "Isolde",
                KnightRank.SQUIRE,
                180,
                100,
                100,
                listOf("Necromancer"),
                listOf("math_route"),
                emptyList(),
                "Iron Sword",
                "Cloth Armor",
                "",
                "",
                "West Realm"
            ),
            KnightProfile(
                "8",
                "STU008",
                SecurityUtils.hashPassword("password123"),
                "Percival",
                KnightRank.NOVICE,
                85,
                100,
                100,
                emptyList(),
                listOf("math_route"),
                emptyList(),
                "Wooden Sword",
                "Cloth Armor",
                "",
                "",
                "South Dominion"
            ),
            KnightProfile(
                "9",
                "STU009",
                SecurityUtils.hashPassword("password123"),
                "Galahad",
                KnightRank.NOVICE,
                60,
                100,
                100,
                emptyList(),
                listOf("math_route"),
                emptyList(),
                "Wooden Sword",
                "Leather Armor",
                "",
                "",
                "North Kingdom"
            ),
            KnightProfile(
                "10",
                "STU010",
                SecurityUtils.hashPassword("password123"),
                "Bedivere",
                KnightRank.NOVICE,
                40,
                100,
                100,
                emptyList(),
                listOf("math_route"),
                emptyList(),
                "Wooden Sword",
                "Cloth Armor",
                "",
                "",
                "East Empire"
            )
        )

        demoAccounts.forEach { studentDao.insertStudent(it) }
    }

    companion object {
        @Volatile
        private var instance: RPGRepository? = null

        fun getInstance(context: Context): RPGRepository {
            return instance ?: synchronized(this) {
                instance ?: RPGRepository(context).also { instance = it }
            }
        }
    }
}

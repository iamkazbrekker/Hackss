package com.runanywhere.startup_hackathon20.data.repository

import com.runanywhere.startup_hackathon20.data.models.*
import com.runanywhere.startup_hackathon20.ui.screens.ResourceItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class EduVentureRepository {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _quests = MutableStateFlow<List<Quest>>(emptyList())
    val quests: StateFlow<List<Quest>> = _quests

    private val _studyMaterials = MutableStateFlow<List<StudyMaterial>>(emptyList())
    val studyMaterials: StateFlow<List<StudyMaterial>> = _studyMaterials

    private val _teacherCourses = MutableStateFlow<List<TeacherCourse>>(emptyList())
    val teacherCourses: StateFlow<List<TeacherCourse>> = _teacherCourses

    private val _sharedResources = MutableStateFlow<List<SharedResource>>(emptyList())
    val sharedResources: StateFlow<List<SharedResource>> = _sharedResources

    private val _classRooms = MutableStateFlow<List<ClassRoom>>(emptyList())
    val classRooms: StateFlow<List<ClassRoom>> = _classRooms

    // New StateFlows for enhanced teacher features
    private val _techDevelopmentCourses = MutableStateFlow<List<TechDevelopmentCourse>>(emptyList())
    val techDevelopmentCourses: StateFlow<List<TechDevelopmentCourse>> = _techDevelopmentCourses

    private val _resourceHubItems = MutableStateFlow<List<ResourceHubItem>>(emptyList())
    val resourceHubItems: StateFlow<List<ResourceHubItem>> = _resourceHubItems

    private val _studentInsights = MutableStateFlow<List<StudentPerformanceInsight>>(emptyList())
    val studentInsights: StateFlow<List<StudentPerformanceInsight>> = _studentInsights

    private val _progressDashboards = MutableStateFlow<List<ProgressDashboard>>(emptyList())
    val progressDashboards: StateFlow<List<ProgressDashboard>> = _progressDashboards

    init {
        // Initialize with mock data
        initializeMockData()
    }

    fun loginAsStudent() {
        _currentUser.value = createMockStudent()
        _quests.value = createMockQuests()
    }

    fun loginAsTeacher() {
        _currentUser.value = createMockTeacher()
        _teacherCourses.value = createMockTeacherCourses()
        _sharedResources.value = createMockSharedResources()
        _classRooms.value = createMockClassRooms()
        // Initialize new teacher features
        _techDevelopmentCourses.value = createMockTechCourses()
        _resourceHubItems.value = createMockResourceHub()
        _studentInsights.value = createMockStudentInsights()
        _progressDashboards.value = createMockProgressDashboards()
    }

    fun logout() {
        _currentUser.value = null
        _quests.value = emptyList()
        _teacherCourses.value = emptyList()
        _sharedResources.value = emptyList()
        _classRooms.value = emptyList()
        _techDevelopmentCourses.value = emptyList()
        _resourceHubItems.value = emptyList()
        _studentInsights.value = emptyList()
        _progressDashboards.value = emptyList()
    }

    fun earnXP(amount: Int) {
        _currentUser.value?.let { user ->
            val newXP = user.xp + amount
            val newLevel = calculateLevel(newXP)
            _currentUser.value = user.copy(xp = newXP, level = newLevel)
        }
    }

    fun completeQuest(questId: String) {
        _quests.value = _quests.value.map { quest ->
            if (quest.id == questId) {
                earnXP(quest.xpReward)
                quest.copy(isCompleted = true, progress = 1f)
            } else {
                quest
            }
        }

        _currentUser.value?.let { user ->
            _currentUser.value = user.copy(
                completedQuests = user.completedQuests + questId
            )
        }
    }

    fun updateQuestProgress(questId: String, progress: Float) {
        _quests.value = _quests.value.map { quest ->
            if (quest.id == questId) {
                quest.copy(progress = progress)
            } else {
                quest
            }
        }
    }

    fun addStudyMaterial(material: StudyMaterial) {
        _studyMaterials.value = _studyMaterials.value + material
    }

    fun unlockAchievement(achievement: Achievement) {
        _currentUser.value?.let { user ->
            val updatedAchievement = achievement.copy(unlockedAt = System.currentTimeMillis())
            _currentUser.value = user.copy(
                achievements = user.achievements + updatedAchievement,
                xp = user.xp + achievement.xpReward
            )
        }
    }

    private fun calculateLevel(xp: Int): Int {
        // Simple level calculation: 100 XP per level
        return (xp / 100) + 1
    }

    private fun initializeMockData() {
        _quests.value = createMockQuests()
    }

    private fun createMockStudent(): User {
        return User(
            id = UUID.randomUUID().toString(),
            name = "Alex Student",
            email = "alex@eduventure.com",
            role = UserRole.STUDENT,
            xp = 450,
            level = 5,
            achievements = listOf(
                Achievement(
                    id = "first_quest",
                    name = "First Steps",
                    description = "Complete your first quest",
                    iconName = "🎯",
                    xpReward = 50,
                    unlockedAt = System.currentTimeMillis()
                ),
                Achievement(
                    id = "quiz_master",
                    name = "Quiz Master",
                    description = "Score 100% on a quiz",
                    iconName = "🏆",
                    xpReward = 100,
                    unlockedAt = System.currentTimeMillis()
                )
            ),
            streak = 7,
            subjects = listOf("Mathematics", "Science", "History")
        )
    }

    private fun createMockTeacher(): User {
        return User(
            id = UUID.randomUUID().toString(),
            name = "Prof. Sarah Johnson",
            email = "sarah@eduventure.com",
            role = UserRole.TEACHER,
            xp = 1250,
            level = 13,
            achievements = listOf(
                Achievement(
                    id = "resource_sharer",
                    name = "Resource Contributor",
                    description = "Share 10 resources with community",
                    iconName = "📚",
                    xpReward = 200,
                    unlockedAt = System.currentTimeMillis()
                )
            ),
            subjects = listOf("Mathematics", "Physics")
        )
    }

    private fun createMockQuests(): List<Quest> {
        return listOf(
            Quest(
                id = "quest_1",
                title = "Master Linear Equations",
                description = "Learn and practice solving linear equations",
                subject = "Mathematics",
                difficulty = QuestDifficulty.EASY,
                xpReward = 50,
                challenges = listOf(
                    Challenge(
                        id = "c1",
                        title = "Watch Introduction Video",
                        description = "Learn the basics of linear equations",
                        type = ChallengeType.VIDEO,
                        content = "linear_equations_intro"
                    ),
                    Challenge(
                        id = "c2",
                        title = "Practice Problems",
                        description = "Solve 10 practice problems",
                        type = ChallengeType.PRACTICE_PROBLEM,
                        content = "practice_set_1"
                    ),
                    Challenge(
                        id = "c3",
                        title = "Quiz Time",
                        description = "Test your knowledge",
                        type = ChallengeType.QUIZ,
                        content = "quiz_linear_eq"
                    )
                )
            ),
            Quest(
                id = "quest_2",
                title = "Photosynthesis Deep Dive",
                description = "Explore the process of photosynthesis",
                subject = "Science",
                difficulty = QuestDifficulty.MEDIUM,
                xpReward = 75,
                challenges = listOf(
                    Challenge(
                        id = "c4",
                        title = "Read Chapter",
                        description = "Study the photosynthesis chapter",
                        type = ChallengeType.READING,
                        content = "photosynthesis_chapter"
                    ),
                    Challenge(
                        id = "c5",
                        title = "Flashcard Review",
                        description = "Review 20 flashcards",
                        type = ChallengeType.FLASHCARD_REVIEW,
                        content = "photosynthesis_flashcards"
                    )
                )
            ),
            Quest(
                id = "quest_3",
                title = "World War II Analysis",
                description = "Study the causes and effects of WWII",
                subject = "History",
                difficulty = QuestDifficulty.HARD,
                xpReward = 100,
                challenges = listOf(
                    Challenge(
                        id = "c6",
                        title = "Documentary Review",
                        description = "Watch and analyze historical documentary",
                        type = ChallengeType.VIDEO,
                        content = "wwii_documentary"
                    ),
                    Challenge(
                        id = "c7",
                        title = "Essay Quiz",
                        description = "Answer comprehensive questions",
                        type = ChallengeType.QUIZ,
                        content = "wwii_quiz"
                    )
                )
            ),
            Quest(
                id = "quest_4",
                title = "Advanced Calculus",
                description = "Master derivatives and integrals",
                subject = "Mathematics",
                difficulty = QuestDifficulty.EXPERT,
                xpReward = 150,
                challenges = listOf(
                    Challenge(
                        id = "c8",
                        title = "Theory Study",
                        description = "Understand calculus fundamentals",
                        type = ChallengeType.READING,
                        content = "calculus_theory"
                    ),
                    Challenge(
                        id = "c9",
                        title = "Problem Solving",
                        description = "Solve complex calculus problems",
                        type = ChallengeType.PRACTICE_PROBLEM,
                        content = "calculus_problems"
                    )
                )
            )
        )
    }

    private fun createMockTeacherCourses(): List<TeacherCourse> {
        return listOf(
            TeacherCourse(
                id = "tc_1",
                title = "Effective Classroom Management",
                description = "Learn strategies for managing diverse classrooms",
                category = "Pedagogy",
                difficulty = QuestDifficulty.MEDIUM,
                xpReward = 100,
                modules = listOf(
                    CourseModule(
                        id = "m1",
                        title = "Introduction to Classroom Dynamics",
                        description = "Understanding student behavior patterns",
                        content = "Course content about classroom dynamics..."
                    ),
                    CourseModule(
                        id = "m2",
                        title = "Conflict Resolution Techniques",
                        description = "Practical strategies for handling conflicts",
                        content = "Course content about conflict resolution..."
                    )
                )
            ),
            TeacherCourse(
                id = "tc_2",
                title = "Digital Tools for Education",
                description = "Master modern educational technology",
                category = "Technology",
                difficulty = QuestDifficulty.EASY,
                xpReward = 75,
                modules = listOf(
                    CourseModule(
                        id = "m3",
                        title = "Interactive Presentations",
                        description = "Creating engaging digital content",
                        content = "Course content about presentations..."
                    )
                )
            )
        )
    }

    private fun createMockSharedResources(): List<SharedResource> {
        return listOf(
            SharedResource(
                id = "sr_1",
                teacherId = "t1",
                teacherName = "Dr. Emily Chen",
                title = "Algebra Worksheet Bundle",
                description = "Comprehensive algebra practice worksheets",
                subject = "Mathematics",
                fileUrl = "algebra_bundle.pdf",
                type = ResourceType.WORKSHEET,
                uploadedAt = System.currentTimeMillis(),
                downloads = 156,
                rating = 4.8f
            ),
            SharedResource(
                id = "sr_2",
                teacherId = "t2",
                teacherName = "Prof. Michael Brown",
                title = "Cell Biology Lesson Plan",
                description = "Complete lesson plan with activities",
                subject = "Science",
                fileUrl = "cell_biology.pdf",
                type = ResourceType.LESSON_PLAN,
                uploadedAt = System.currentTimeMillis(),
                downloads = 98,
                rating = 4.5f
            )
        )
    }

    private fun createMockClassRooms(): List<ClassRoom> {
        return listOf(
            ClassRoom(
                id = "class_1",
                name = "Mathematics 101 - Morning Section",
                teacherId = "t1",
                subject = "Mathematics",
                students = listOf(
                    StudentProgress(
                        studentId = "s1",
                        studentName = "Emma Wilson",
                        xp = 650,
                        level = 7,
                        completedQuests = 8,
                        averageScore = 92.5f,
                        lastActive = System.currentTimeMillis(),
                        strengths = listOf("Algebra", "Geometry"),
                        weaknesses = listOf("Calculus"),
                        recentActivity = listOf(
                            ActivityLog(
                                type = ActivityType.QUEST_COMPLETED,
                                description = "Completed Linear Equations quest",
                                timestamp = System.currentTimeMillis(),
                                xpEarned = 50
                            )
                        )
                    ),
                    StudentProgress(
                        studentId = "s2",
                        studentName = "James Rodriguez",
                        xp = 480,
                        level = 5,
                        completedQuests = 5,
                        averageScore = 85.0f,
                        lastActive = System.currentTimeMillis() - 86400000,
                        strengths = listOf("Problem Solving"),
                        weaknesses = listOf("Word Problems"),
                        recentActivity = listOf(
                            ActivityLog(
                                type = ActivityType.QUIZ_TAKEN,
                                description = "Scored 85% on Algebra quiz",
                                timestamp = System.currentTimeMillis() - 86400000,
                                xpEarned = 25
                            )
                        )
                    )
                ),
                createdAt = System.currentTimeMillis()
            )
        )
    }

    // Create mock data for new teacher features
    private fun createMockTechCourses(): List<TechDevelopmentCourse> {
        return listOf(
            TechDevelopmentCourse(
                id = "tech_1",
                title = "AI in Education: ChatGPT & Beyond",
                description = "Master the latest AI tools transforming classrooms. Learn how to integrate ChatGPT, Gemini, and other AI assistants into your teaching",
                technology = "Artificial Intelligence",
                category = TechCategory.ARTIFICIAL_INTELLIGENCE,
                difficulty = CourseDifficulty.BEGINNER,
                duration = "3 weeks",
                instructor = "Dr. Sarah Chen",
                thumbnail = "ai_education.png",
                modules = listOf(
                    TechCourseModule(
                        id = "mod_1",
                        moduleNumber = 1,
                        title = "Introduction to AI in Education",
                        description = "Understanding AI's role in modern classrooms",
                        content = "Learn about the fundamentals of AI and its applications in education...",
                        videoUrl = "https://youtube.com/watch?v=ai-intro",
                        estimatedTime = "45 mins",
                        resources = listOf("AI Basics PDF", "Case Studies"),
                        isCompleted = false
                    ),
                    TechCourseModule(
                        id = "mod_2",
                        moduleNumber = 2,
                        title = "Using ChatGPT for Lesson Planning",
                        description = "Practical applications of ChatGPT",
                        content = "Discover how to use ChatGPT to create engaging lesson plans...",
                        videoUrl = "https://youtube.com/watch?v=chatgpt-lessons",
                        estimatedTime = "60 mins",
                        resources = listOf("Prompt Templates", "Best Practices Guide"),
                        isCompleted = false
                    )
                ),
                xpReward = 150,
                certificationAvailable = true,
                enrolledCount = 234,
                rating = 4.8f,
                releaseYear = 2024,
                tags = listOf("AI", "beginner-friendly", "certification", "trending"),
                progress = 0.3f,
                isEnrolled = true
            ),
            TechDevelopmentCourse(
                id = "tech_2",
                title = "Building Interactive Web Apps with React",
                description = "Create modern, interactive educational web applications using React and Next.js",
                technology = "React/Next.js",
                category = TechCategory.WEB_DEVELOPMENT,
                difficulty = CourseDifficulty.INTERMEDIATE,
                duration = "5 weeks",
                instructor = "Prof. Michael Rodriguez",
                thumbnail = "react_course.png",
                modules = listOf(),
                xpReward = 200,
                certificationAvailable = true,
                enrolledCount = 156,
                rating = 4.6f,
                releaseYear = 2024,
                tags = listOf("web-dev", "react", "hands-on", "trending"),
                progress = 0f,
                isEnrolled = false
            ),
            TechDevelopmentCourse(
                id = "tech_3",
                title = "Data Science for Educators",
                description = "Analyze student performance data and create insights using Python and data visualization",
                technology = "Python/Data Science",
                category = TechCategory.DATA_SCIENCE,
                difficulty = CourseDifficulty.INTERMEDIATE,
                duration = "4 weeks",
                instructor = "Dr. Emily Watson",
                thumbnail = "data_science.png",
                modules = listOf(),
                xpReward = 180,
                certificationAvailable = true,
                enrolledCount = 89,
                rating = 4.7f,
                releaseYear = 2024,
                tags = listOf("python", "analytics", "certification"),
                progress = 0f,
                isEnrolled = false
            ),
            TechDevelopmentCourse(
                id = "tech_4",
                title = "Mobile App Development with Flutter",
                description = "Build cross-platform educational apps for iOS and Android",
                technology = "Flutter/Dart",
                category = TechCategory.MOBILE_DEVELOPMENT,
                difficulty = CourseDifficulty.ADVANCED,
                duration = "6 weeks",
                instructor = "Prof. James Lee",
                thumbnail = "flutter_course.png",
                modules = listOf(),
                xpReward = 250,
                certificationAvailable = true,
                enrolledCount = 67,
                rating = 4.9f,
                releaseYear = 2024,
                tags = listOf("mobile", "flutter", "advanced", "hands-on"),
                progress = 0f,
                isEnrolled = false
            ),
            TechDevelopmentCourse(
                id = "tech_5",
                title = "Cloud Computing with AWS for Education",
                description = "Deploy and manage educational platforms on AWS cloud infrastructure",
                technology = "AWS/Cloud",
                category = TechCategory.CLOUD_COMPUTING,
                difficulty = CourseDifficulty.INTERMEDIATE,
                duration = "4 weeks",
                instructor = "Sarah Johnson",
                thumbnail = "aws_course.png",
                modules = listOf(),
                xpReward = 190,
                certificationAvailable = true,
                enrolledCount = 112,
                rating = 4.5f,
                releaseYear = 2024,
                tags = listOf("cloud", "aws", "infrastructure"),
                progress = 0f,
                isEnrolled = false
            )
        )
    }

    private fun createMockResourceHub(): List<ResourceHubItem> {
        return listOf(
            ResourceHubItem(
                id = "rh_1",
                title = "Complete Machine Learning Course - Andrew Ng",
                description = "Stanford's legendary ML course. Perfect for understanding AI fundamentals before applying them in education",
                type = ResourceHubType.COURSE,
                category = TechCategory.ARTIFICIAL_INTELLIGENCE,
                url = "https://www.coursera.org/learn/machine-learning",
                thumbnail = "ml_course.jpg",
                provider = "Coursera",
                isFree = false,
                rating = 4.9f,
                duration = "11 weeks",
                addedBy = "Prof. Sarah Johnson",
                addedAt = System.currentTimeMillis(),
                tags = listOf("AI", "ML", "foundational", "stanford"),
                viewCount = 523,
                bookmarked = true
            ),
            ResourceHubItem(
                id = "rh_2",
                title = "React Official Documentation",
                description = "Official React docs - the best place to learn modern React with hooks and server components",
                type = ResourceHubType.DOCUMENTATION,
                category = TechCategory.WEB_DEVELOPMENT,
                url = "https://react.dev",
                thumbnail = "react_docs.png",
                provider = "React Team",
                isFree = true,
                rating = 4.8f,
                duration = null,
                addedBy = "Dr. Michael Brown",
                addedAt = System.currentTimeMillis(),
                tags = listOf("react", "web", "documentation", "official"),
                viewCount = 892,
                bookmarked = true
            ),
            ResourceHubItem(
                id = "rh_3",
                title = "Python for Data Analysis - Wes McKinney",
                description = "Comprehensive guide to data manipulation with pandas",
                type = ResourceHubType.BOOK,
                category = TechCategory.DATA_SCIENCE,
                url = "https://wesmckinney.com/book/",
                thumbnail = "python_data.jpg",
                provider = "O'Reilly",
                isFree = true,
                rating = 4.7f,
                duration = null,
                addedBy = "Dr. Emily Watson",
                addedAt = System.currentTimeMillis(),
                tags = listOf("python", "pandas", "data-science", "free"),
                viewCount = 445,
                bookmarked = false
            ),
            ResourceHubItem(
                id = "rh_4",
                title = "Fireship - Flutter Tutorial",
                description = "Build a complete app in 100 seconds! Fast-paced Flutter intro",
                type = ResourceHubType.VIDEO_TUTORIAL,
                category = TechCategory.MOBILE_DEVELOPMENT,
                url = "https://www.youtube.com/watch?v=1xipg02Wu8s",
                thumbnail = "fireship_flutter.jpg",
                provider = "YouTube - Fireship",
                isFree = true,
                rating = 4.9f,
                duration = "12 mins",
                addedBy = "Prof. James Lee",
                addedAt = System.currentTimeMillis(),
                tags = listOf("flutter", "mobile", "video", "quick-start"),
                viewCount = 1234,
                bookmarked = true
            ),
            ResourceHubItem(
                id = "rh_5",
                title = "AWS Free Tier Guide",
                description = "Complete guide to AWS free tier services for education",
                type = ResourceHubType.ARTICLE,
                category = TechCategory.CLOUD_COMPUTING,
                url = "https://aws.amazon.com/free/",
                thumbnail = "aws_free.png",
                provider = "AWS",
                isFree = true,
                rating = 4.6f,
                duration = "15 mins read",
                addedBy = "Sarah Johnson",
                addedAt = System.currentTimeMillis(),
                tags = listOf("aws", "cloud", "free-tier", "guide"),
                viewCount = 678,
                bookmarked = false
            ),
            ResourceHubItem(
                id = "rh_6",
                title = "GitHub Student Developer Pack",
                description = "Free developer tools and cloud credits for education",
                type = ResourceHubType.TOOL,
                category = TechCategory.DEVOPS,
                url = "https://education.github.com/pack",
                thumbnail = "github_student.png",
                provider = "GitHub",
                isFree = true,
                rating = 5.0f,
                duration = null,
                addedBy = "Prof. Sarah Johnson",
                addedAt = System.currentTimeMillis(),
                tags = listOf("github", "free", "tools", "student"),
                viewCount = 956,
                bookmarked = true
            ),
            ResourceHubItem(
                id = "rh_7",
                title = "Tailwind CSS Documentation",
                description = "Utility-first CSS framework documentation",
                type = ResourceHubType.DOCUMENTATION,
                category = TechCategory.WEB_DEVELOPMENT,
                url = "https://tailwindcss.com/docs",
                thumbnail = "tailwind_docs.png",
                provider = "Tailwind Labs",
                isFree = true,
                rating = 4.8f,
                duration = null,
                addedBy = "Dr. Michael Brown",
                addedAt = System.currentTimeMillis(),
                tags = listOf("css", "tailwind", "web", "styling"),
                viewCount = 534,
                bookmarked = false
            ),
            ResourceHubItem(
                id = "rh_8",
                title = "Awesome Educational Games Repository",
                description = "Curated list of open-source educational games and resources",
                type = ResourceHubType.GITHUB_REPO,
                category = TechCategory.GAME_DEVELOPMENT,
                url = "https://github.com/topics/educational-games",
                thumbnail = "github_games.png",
                provider = "GitHub",
                isFree = true,
                rating = 4.7f,
                duration = null,
                addedBy = "Prof. James Lee",
                addedAt = System.currentTimeMillis(),
                tags = listOf("games", "open-source", "education", "github"),
                viewCount = 321,
                bookmarked = false
            )
        )
    }

    private fun createMockStudentInsights(): List<StudentPerformanceInsight> {
        return listOf(
            StudentPerformanceInsight(
                studentId = "s1",
                studentName = "Emma Wilson",
                classId = "class_1",
                overallPerformance = PerformanceMetrics(
                    averageScore = 92.5f,
                    totalXP = 650,
                    level = 7,
                    questsCompleted = 8,
                    totalQuests = 10,
                    currentStreak = 5,
                    longestStreak = 12,
                    performanceTrend = TrendDirection.UP
                ),
                subjectPerformance = listOf(
                    SubjectPerformance(
                        subject = "Mathematics",
                        averageScore = 95.0f,
                        questsCompleted = 5,
                        totalQuests = 6,
                        timeSpent = 320,
                        lastActivity = System.currentTimeMillis(),
                        mastery = MasteryLevel.ADVANCED
                    ),
                    SubjectPerformance(
                        subject = "Science",
                        averageScore = 90.0f,
                        questsCompleted = 3,
                        totalQuests = 4,
                        timeSpent = 180,
                        lastActivity = System.currentTimeMillis() - 86400000,
                        mastery = MasteryLevel.PROFICIENT
                    )
                ),
                learningPattern = LearningPattern(
                    preferredLearningStyle = LearningStyle.VISUAL,
                    mostActiveTimeOfDay = TimeOfDay.EVENING,
                    averageSessionDuration = 45,
                    completionRate = 0.85f,
                    retryRate = 0.15f
                ),
                recommendations = listOf(
                    "Continue challenging Emma with advanced problems",
                    "Encourage peer tutoring opportunities",
                    "Consider accelerated learning path"
                ),
                strengths = listOf(
                    StrengthArea("Algebra", 98.0f, "Exceptional problem-solving skills"),
                    StrengthArea("Geometry", 94.0f, "Strong spatial reasoning")
                ),
                improvementAreas = listOf(
                    ImprovementArea(
                        "Calculus",
                        75.0f,
                        90.0f,
                        listOf(
                            "Additional practice problems",
                            "Video tutorials",
                            "One-on-one sessions"
                        )
                    )
                ),
                engagementMetrics = EngagementMetrics(
                    totalLoginDays = 45,
                    averageSessionsPerWeek = 6.5f,
                    totalTimeSpent = 1200,
                    chatInteractions = 34,
                    resourcesAccessed = 28,
                    engagementLevel = EngagementLevel.HIGH
                ),
                lastUpdated = System.currentTimeMillis()
            ),
            StudentPerformanceInsight(
                studentId = "s2",
                studentName = "James Rodriguez",
                classId = "class_1",
                overallPerformance = PerformanceMetrics(
                    averageScore = 75.0f,
                    totalXP = 480,
                    level = 5,
                    questsCompleted = 5,
                    totalQuests = 10,
                    currentStreak = 2,
                    longestStreak = 8,
                    performanceTrend = TrendDirection.STABLE
                ),
                subjectPerformance = listOf(
                    SubjectPerformance(
                        subject = "Mathematics",
                        averageScore = 78.0f,
                        questsCompleted = 3,
                        totalQuests = 6,
                        timeSpent = 240,
                        lastActivity = System.currentTimeMillis() - 172800000,
                        mastery = MasteryLevel.DEVELOPING
                    )
                ),
                learningPattern = LearningPattern(
                    preferredLearningStyle = LearningStyle.KINESTHETIC,
                    mostActiveTimeOfDay = TimeOfDay.AFTERNOON,
                    averageSessionDuration = 30,
                    completionRate = 0.60f,
                    retryRate = 0.35f
                ),
                recommendations = listOf(
                    "Provide more hands-on activities",
                    "Break complex problems into smaller steps",
                    "Regular check-ins to maintain motivation"
                ),
                strengths = listOf(
                    StrengthArea("Problem Solving", 82.0f, "Good logical thinking")
                ),
                improvementAreas = listOf(
                    ImprovementArea(
                        "Word Problems",
                        65.0f,
                        80.0f,
                        listOf(
                            "More practice with real-world examples",
                            "Visual aids",
                            "Group work"
                        )
                    ),
                    ImprovementArea(
                        "Time Management",
                        60.0f,
                        85.0f,
                        listOf("Set timers for practice", "Break tasks into chunks")
                    )
                ),
                engagementMetrics = EngagementMetrics(
                    totalLoginDays = 28,
                    averageSessionsPerWeek = 3.5f,
                    totalTimeSpent = 720,
                    chatInteractions = 15,
                    resourcesAccessed = 12,
                    engagementLevel = EngagementLevel.MODERATE
                ),
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    private fun createMockProgressDashboards(): List<ProgressDashboard> {
        return listOf(
            ProgressDashboard(
                classId = "class_1",
                className = "Mathematics 101 - Morning Section",
                totalStudents = 25,
                activeStudents = 18,
                averageClassScore = 82.5f,
                averageClassLevel = 6.2f,
                totalQuestsCompleted = 127,
                classEngagement = 75.0f,
                performanceDistribution = PerformanceDistribution(
                    excellent = 6,
                    good = 9,
                    average = 7,
                    needsImprovement = 2,
                    struggling = 1
                ),
                weeklyProgress = listOf(
                    WeeklyProgressData(
                        weekNumber = 1,
                        startDate = System.currentTimeMillis() - 604800000 * 4,
                        endDate = System.currentTimeMillis() - 604800000 * 3,
                        averageScore = 78.0f,
                        questsCompleted = 28,
                        activeStudents = 22,
                        totalXPEarned = 1400
                    ),
                    WeeklyProgressData(
                        weekNumber = 2,
                        startDate = System.currentTimeMillis() - 604800000 * 3,
                        endDate = System.currentTimeMillis() - 604800000 * 2,
                        averageScore = 80.5f,
                        questsCompleted = 32,
                        activeStudents = 20,
                        totalXPEarned = 1600
                    ),
                    WeeklyProgressData(
                        weekNumber = 3,
                        startDate = System.currentTimeMillis() - 604800000 * 2,
                        endDate = System.currentTimeMillis() - 604800000,
                        averageScore = 82.0f,
                        questsCompleted = 35,
                        activeStudents = 19,
                        totalXPEarned = 1750
                    ),
                    WeeklyProgressData(
                        weekNumber = 4,
                        startDate = System.currentTimeMillis() - 604800000,
                        endDate = System.currentTimeMillis(),
                        averageScore = 84.5f,
                        questsCompleted = 32,
                        activeStudents = 18,
                        totalXPEarned = 1600
                    )
                ),
                topPerformers = listOf(
                    TopPerformer(
                        studentId = "s1",
                        studentName = "Emma Wilson",
                        score = 95.0f,
                        level = 7,
                        questsCompleted = 8,
                        rank = 1
                    ),
                    TopPerformer(
                        studentId = "s3",
                        studentName = "Alex Chen",
                        score = 92.0f,
                        level = 7,
                        questsCompleted = 7,
                        rank = 2
                    ),
                    TopPerformer(
                        studentId = "s4",
                        studentName = "Sofia Martinez",
                        score = 90.5f,
                        level = 6,
                        questsCompleted = 7,
                        rank = 3
                    )
                ),
                studentsNeedingHelp = listOf(
                    StudentAlert(
                        studentId = "s5",
                        studentName = "Ryan Thompson",
                        alertType = AlertType.DECLINING_PERFORMANCE,
                        severity = AlertSeverity.HIGH,
                        message = "Score dropped from 75% to 58% in last 2 weeks",
                        suggestedActions = listOf(
                            "Schedule one-on-one meeting",
                            "Review recent quiz results",
                            "Provide additional resources"
                        ),
                        timestamp = System.currentTimeMillis()
                    ),
                    StudentAlert(
                        studentId = "s6",
                        studentName = "Maya Patel",
                        alertType = AlertType.LOW_ENGAGEMENT,
                        severity = AlertSeverity.MEDIUM,
                        message = "No activity in last 5 days",
                        suggestedActions = listOf(
                            "Send reminder message",
                            "Check for technical issues",
                            "Contact parents if needed"
                        ),
                        timestamp = System.currentTimeMillis()
                    ),
                    StudentAlert(
                        studentId = "s7",
                        studentName = "Lucas Brown",
                        alertType = AlertType.STRUGGLING_WITH_TOPIC,
                        severity = AlertSeverity.MEDIUM,
                        message = "Difficulty with quadratic equations (3 failed attempts)",
                        suggestedActions = listOf(
                            "Assign supplementary video",
                            "Recommend peer study group",
                            "Offer extra practice problems"
                        ),
                        timestamp = System.currentTimeMillis()
                    )
                ),
                subjectBreakdown = listOf(
                    SubjectStats(
                        subject = "Algebra",
                        averageScore = 85.0f,
                        totalQuests = 45,
                        completedQuests = 38,
                        studentsExcelling = 12,
                        studentsStruggling = 3,
                        mostCommonMistakes = listOf(
                            "Sign errors in equations",
                            "Forgetting to distribute negative signs",
                            "Incorrect order of operations"
                        )
                    ),
                    SubjectStats(
                        subject = "Geometry",
                        averageScore = 80.0f,
                        totalQuests = 38,
                        completedQuests = 32,
                        studentsExcelling = 10,
                        studentsStruggling = 5,
                        mostCommonMistakes = listOf(
                            "Angle calculation errors",
                            "Confusion with theorem applications"
                        )
                    ),
                    SubjectStats(
                        subject = "Statistics",
                        averageScore = 82.5f,
                        totalQuests = 30,
                        completedQuests = 28,
                        studentsExcelling = 14,
                        studentsStruggling = 2,
                        mostCommonMistakes = listOf(
                            "Mean vs median confusion",
                            "Probability calculation errors"
                        )
                    )
                ),
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    companion object {
        @Volatile
        private var instance: EduVentureRepository? = null

        fun getInstance(): EduVentureRepository {
            return instance ?: synchronized(this) {
                instance ?: EduVentureRepository().also { instance = it }
            }
        }
    }
}

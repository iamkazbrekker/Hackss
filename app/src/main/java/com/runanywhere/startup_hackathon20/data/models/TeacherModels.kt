package com.runanywhere.startup_hackathon20.data.models

import kotlinx.serialization.Serializable

@Serializable
data class TeacherCourse(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val difficulty: QuestDifficulty,
    val modules: List<CourseModule>,
    val xpReward: Int,
    val progress: Float = 0f,
    val isCompleted: Boolean = false
)

@Serializable
data class CourseModule(
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val videoUrl: String? = null,
    val quiz: Quiz? = null,
    val isCompleted: Boolean = false
)

@Serializable
data class SharedResource(
    val id: String,
    val teacherId: String,
    val teacherName: String,
    val title: String,
    val description: String,
    val subject: String,
    val fileUrl: String,
    val type: ResourceType,
    val uploadedAt: Long,
    val downloads: Int = 0,
    val rating: Float = 0f,
    val reviews: List<ResourceReview> = emptyList()
)

enum class ResourceType {
    LESSON_PLAN,
    WORKSHEET,
    PRESENTATION,
    ASSESSMENT,
    ACTIVITY,
    OTHER
}

@Serializable
data class ResourceReview(
    val userId: String,
    val userName: String,
    val rating: Int,
    val comment: String,
    val createdAt: Long
)

@Serializable
data class ClassRoom(
    val id: String,
    val name: String,
    val teacherId: String,
    val subject: String,
    val students: List<StudentProgress>,
    val createdAt: Long
)

@Serializable
data class StudentProgress(
    val studentId: String,
    val studentName: String,
    val xp: Int,
    val level: Int,
    val completedQuests: Int,
    val averageScore: Float,
    val lastActive: Long,
    val strengths: List<String>,
    val weaknesses: List<String>,
    val recentActivity: List<ActivityLog>
)

@Serializable
data class ActivityLog(
    val type: ActivityType,
    val description: String,
    val timestamp: Long,
    val xpEarned: Int = 0
)

enum class ActivityType {
    QUEST_COMPLETED,
    QUIZ_TAKEN,
    FLASHCARD_REVIEW,
    ACHIEVEMENT_UNLOCKED,
    LEVEL_UP,
    CHAT_SESSION
}

// ============ NEW MODELS FOR ENHANCED TEACHER FEATURES ============

// 1. TEACHER DEVELOPMENT COURSES (Latest Technology)
@Serializable
data class TechDevelopmentCourse(
    val id: String,
    val title: String,
    val description: String,
    val technology: String, // e.g., "AI/ML", "Blockchain", "AR/VR"
    val category: TechCategory,
    val difficulty: CourseDifficulty,
    val duration: String, // e.g., "4 weeks"
    val instructor: String,
    val thumbnail: String,
    val modules: List<TechCourseModule>,
    val xpReward: Int,
    val certificationAvailable: Boolean = true,
    val enrolledCount: Int = 0,
    val rating: Float = 0f,
    val releaseYear: Int,
    val tags: List<String>, // e.g., ["beginner-friendly", "hands-on", "certification"]
    val progress: Float = 0f,
    val isEnrolled: Boolean = false,
    val completedAt: Long? = null
)

enum class TechCategory {
    ARTIFICIAL_INTELLIGENCE,
    WEB_DEVELOPMENT,
    MOBILE_DEVELOPMENT,
    CLOUD_COMPUTING,
    DATA_SCIENCE,
    CYBERSECURITY,
    BLOCKCHAIN,
    AR_VR,
    IOT,
    GAME_DEVELOPMENT,
    DEVOPS,
    UI_UX_DESIGN
}

enum class CourseDifficulty {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED,
    EXPERT
}

@Serializable
data class TechCourseModule(
    val id: String,
    val moduleNumber: Int,
    val title: String,
    val description: String,
    val content: String,
    val videoUrl: String? = null,
    val estimatedTime: String, // e.g., "45 mins"
    val resources: List<String> = emptyList(),
    val quiz: TechQuiz? = null,
    val practicalExercise: PracticalExercise? = null,
    val isCompleted: Boolean = false
)

@Serializable
data class TechQuiz(
    val id: String,
    val title: String,
    val questions: List<TechQuizQuestion>,
    val passingScore: Int = 70
)

@Serializable
data class TechQuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int,
    val explanation: String
)

@Serializable
data class PracticalExercise(
    val id: String,
    val title: String,
    val description: String,
    val instructions: List<String>,
    val sampleCode: String? = null,
    val expectedOutcome: String
)

// 2. RESOURCE HUB (With External Links)
@Serializable
data class ResourceHubItem(
    val id: String,
    val title: String,
    val description: String,
    val type: ResourceHubType,
    val category: TechCategory,
    val url: String, // External link
    val thumbnail: String,
    val provider: String, // e.g., "Coursera", "YouTube", "GitHub"
    val isFree: Boolean = true,
    val rating: Float = 0f,
    val duration: String? = null,
    val addedBy: String, // Teacher who added it
    val addedAt: Long,
    val tags: List<String>,
    val viewCount: Int = 0,
    val bookmarked: Boolean = false
)

enum class ResourceHubType {
    VIDEO_TUTORIAL,
    ARTICLE,
    DOCUMENTATION,
    COURSE,
    TOOL,
    BOOK,
    PODCAST,
    GITHUB_REPO,
    INTERACTIVE_DEMO,
    RESEARCH_PAPER
}

@Serializable
data class ResourceCollection(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val resources: List<ResourceHubItem>,
    val createdBy: String,
    val isPublic: Boolean = true
)

// 3. STUDENT PERFORMANCE INSIGHTS
@Serializable
data class StudentPerformanceInsight(
    val studentId: String,
    val studentName: String,
    val classId: String,
    val overallPerformance: PerformanceMetrics,
    val subjectPerformance: List<SubjectPerformance>,
    val learningPattern: LearningPattern,
    val recommendations: List<String>,
    val strengths: List<StrengthArea>,
    val improvementAreas: List<ImprovementArea>,
    val engagementMetrics: EngagementMetrics,
    val lastUpdated: Long
)

@Serializable
data class PerformanceMetrics(
    val averageScore: Float,
    val totalXP: Int,
    val level: Int,
    val questsCompleted: Int,
    val totalQuests: Int,
    val currentStreak: Int,
    val longestStreak: Int,
    val performanceTrend: TrendDirection // UP, DOWN, STABLE
)

enum class TrendDirection {
    UP,
    DOWN,
    STABLE
}

@Serializable
data class SubjectPerformance(
    val subject: String,
    val averageScore: Float,
    val questsCompleted: Int,
    val totalQuests: Int,
    val timeSpent: Long, // in minutes
    val lastActivity: Long,
    val mastery: MasteryLevel
)

enum class MasteryLevel {
    BEGINNER,
    DEVELOPING,
    PROFICIENT,
    ADVANCED,
    EXPERT
}

@Serializable
data class LearningPattern(
    val preferredLearningStyle: LearningStyle,
    val mostActiveTimeOfDay: TimeOfDay,
    val averageSessionDuration: Long, // in minutes
    val completionRate: Float,
    val retryRate: Float // How often they retry failed quests
)

enum class LearningStyle {
    VISUAL,
    AUDITORY,
    KINESTHETIC,
    READING_WRITING,
    MIXED
}

enum class TimeOfDay {
    EARLY_MORNING, // 6-9 AM
    MORNING,       // 9-12 PM
    AFTERNOON,     // 12-5 PM
    EVENING,       // 5-9 PM
    NIGHT          // 9-12 AM
}

@Serializable
data class StrengthArea(
    val area: String,
    val score: Float,
    val description: String
)

@Serializable
data class ImprovementArea(
    val area: String,
    val currentScore: Float,
    val targetScore: Float,
    val suggestions: List<String>
)

@Serializable
data class EngagementMetrics(
    val totalLoginDays: Int,
    val averageSessionsPerWeek: Float,
    val totalTimeSpent: Long, // in minutes
    val chatInteractions: Int,
    val resourcesAccessed: Int,
    val engagementLevel: EngagementLevel
)

enum class EngagementLevel {
    LOW,      // Less than 2 sessions/week
    MODERATE, // 2-4 sessions/week
    HIGH,     // 5-7 sessions/week
    VERY_HIGH // More than 7 sessions/week
}

// 4. INTERACTIVE PROGRESS DASHBOARD
@Serializable
data class ProgressDashboard(
    val classId: String,
    val className: String,
    val totalStudents: Int,
    val activeStudents: Int,
    val averageClassScore: Float,
    val averageClassLevel: Float,
    val totalQuestsCompleted: Int,
    val classEngagement: Float, // 0-100
    val performanceDistribution: PerformanceDistribution,
    val weeklyProgress: List<WeeklyProgressData>,
    val topPerformers: List<TopPerformer>,
    val studentsNeedingHelp: List<StudentAlert>,
    val subjectBreakdown: List<SubjectStats>,
    val lastUpdated: Long
)

@Serializable
data class PerformanceDistribution(
    val excellent: Int,    // 90-100%
    val good: Int,         // 75-89%
    val average: Int,      // 60-74%
    val needsImprovement: Int, // 40-59%
    val struggling: Int    // Below 40%
)

@Serializable
data class WeeklyProgressData(
    val weekNumber: Int,
    val startDate: Long,
    val endDate: Long,
    val averageScore: Float,
    val questsCompleted: Int,
    val activeStudents: Int,
    val totalXPEarned: Int
)

@Serializable
data class TopPerformer(
    val studentId: String,
    val studentName: String,
    val score: Float,
    val level: Int,
    val questsCompleted: Int,
    val rank: Int
)

@Serializable
data class StudentAlert(
    val studentId: String,
    val studentName: String,
    val alertType: AlertType,
    val severity: AlertSeverity,
    val message: String,
    val suggestedActions: List<String>,
    val timestamp: Long
)

enum class AlertType {
    LOW_ENGAGEMENT,
    DECLINING_PERFORMANCE,
    MISSED_DEADLINES,
    STRUGGLING_WITH_TOPIC,
    INACTIVE_FOR_DAYS,
    NEEDS_CHALLENGE
}

enum class AlertSeverity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

@Serializable
data class SubjectStats(
    val subject: String,
    val averageScore: Float,
    val totalQuests: Int,
    val completedQuests: Int,
    val studentsExcelling: Int,
    val studentsStruggling: Int,
    val mostCommonMistakes: List<String>
)

// Chart/Graph Data for Visualizations
@Serializable
data class ChartData(
    val labels: List<String>,
    val datasets: List<DataSet>
)

@Serializable
data class DataSet(
    val label: String,
    val data: List<Float>,
    val color: String
)

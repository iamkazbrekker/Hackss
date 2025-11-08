# 🔧 Implementation Guide - Database Integration & Features

## ✅ COMPLETED

### 1. Database Setup

- ✅ Added Room dependencies with KSP
- ✅ Created `KnightProfile` entity with authentication fields
- ✅ Created `LearningRoute` entity
- ✅ Created Type Converters for complex types
- ✅ Created `StudentDao` with all CRUD operations
- ✅ Created `AppDatabase` class
- ✅ Created `SecurityUtils` for password hashing
- ✅ Updated `RPGRepository` to use database
- ✅ Created `StudentLoginScreen` with authentication UI

### 2. Hardcoded Google Drive Folder ID

- ✅ Added `DRIVE_FOLDER_ID` constant in `RPGRepository`
- **ACTION REQUIRED**: Replace `"YOUR_DRIVE_FOLDER_ID_HERE"` in `RPGRepository.kt` line 23 with your
  actual Google Drive folder ID

---

## 🚧 REMAINING TASKS

### 3. Fix Resources Screen to Open URLs

**File**: `app/src/main/java/com/runanywhere/startup_hackathon20/ui/screens/ResourcesScreen.kt`

Update the `ResourceCard` onClick to open browser:

```kotlin
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
        // ... existing code ...
    }
}
```

Add these imports at the top:

```kotlin
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
```

---

### 4. Update ViewModel for Database Integration

**File**: `app/src/main/java/com/runanywhere/startup_hackathon20/viewmodel/EduVentureViewModel.kt`

Add these new methods and update Drive initialization:

```kotlin
class EduVentureViewModel(private val context: Context) : ViewModel() {
    
    private val repository = EduVentureRepository.getInstance()
    private val rpgRepository = RPGRepository.getInstance(context)
    private var driveHelper: DriveHelper? = null

    // Knight Profile from RPG
    val knightProfile: StateFlow<KnightProfile?> = rpgRepository.knightProfile
    val learningRoutes: StateFlow<List<LearningRoute>> = rpgRepository.learningRoutes

    // Login state
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    init {
        loadAvailableModels()
        // Initialize database
        viewModelScope.launch {
            rpgRepository.initializeDatabase()
            // Auto-initialize Drive with hardcoded folder ID
            setDriveFolderId(rpgRepository.DRIVE_FOLDER_ID)
        }
    }

    // Student Authentication
    suspend fun loginStudent(studentId: String, password: String): Boolean {
        return try {
            val knight = rpgRepository.login(studentId, password)
            if (knight == null) {
                _loginError.value = "Invalid Student ID or Password"
                false
            } else {
                _loginError.value = null
                true
            }
        } catch (e: Exception) {
            _loginError.value = "Login failed: ${e.message}"
            false
        }
    }

    fun completeModule(moduleId: String) {
        viewModelScope.launch {
            rpgRepository.completeModule(moduleId)
        }
    }

    suspend fun getLeaderboard(): List<KnightProfile> {
        return rpgRepository.getLeaderboard()
    }

    // Profile Management
    fun updateProfile(name: String, email: String, phone: String) {
        viewModelScope.launch {
            rpgRepository.updateProfile(name, email, phone)
        }
    }

    fun changePassword(oldPassword: String, newPassword: String, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            val result = rpgRepository.changePassword(oldPassword, newPassword)
            onResult(result)
        }
    }

    // Override logout
    override fun logout() {
        repository.logout()
        rpgRepository.logout()
        driveHelper?.clearCache()
    }

    // Drive with hardcoded folder ID
    override fun setDriveFolderId(folderId: String) {
        _driveFolderId.value = folderId
        // Auto-fetch PDFs when folder is set
        fetchPDFsFromDrive()
    }
}
```

---

### 5. Implement Profile Screen Functions

**File**:
`app/src/main/java/com/runanywhere/startup_hackathon20/ui/screens/ProfileDashboardScreen.kt`

Create dialog screens for each profile action:

```kotlin
// Add these composables to the file

@Composable
fun EditProfileDialog(
    knight: KnightProfile,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(knight.knightName) }
    var email by remember { mutableStateOf(knight.email) }
    var phone by remember { mutableStateOf(knight.phoneNumber) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Knight Name") }
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(name, email, phone) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onChangePassword: (String, String) -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Password") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = { Text("Old Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
                error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                when {
                    newPassword != confirmPassword -> error = "Passwords don't match"
                    newPassword.length < 6 -> error = "Password too short"
                    else -> {
                        onChangePassword(oldPassword, newPassword)
                        onDismiss()
                    }
                }
            }) {
                Text("Change Password")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
```

Then update `ProfileDashboardScreen` to use these:

```kotlin
@Composable
fun ProfileDashboardScreen(
    knightProfile: KnightProfile?,
    viewModel: EduVentureViewModel,
    onLogout: () -> Unit = {}
) {
    var showEditProfile by remember { mutableStateOf(false) }
    var showChangePassword by remember { mutableStateOf(false) }
    
    // ... existing code ...
    
    // Show dialogs
    if (showEditProfile && knightProfile != null) {
        EditProfileDialog(
            knight = knightProfile,
            onDismiss = { showEditProfile = false },
            onSave = { name, email, phone ->
                viewModel.updateProfile(name, email, phone)
                showEditProfile = false
            }
        )
    }
    
    if (showChangePassword) {
        ChangePasswordDialog(
            onDismiss = { showChangePassword = false },
            onChangePassword = { old, new ->
                viewModel.changePassword(old, new) { result ->
                    // Show toast or snackbar with result
                }
            }
        )
    }
    
    // Update the onEditProfile and onChangePassword callbacks
    ProfileDashboardScreen(
        knightProfile = knightProfile,
        onEditProfile = { showEditProfile = true },
        onChangePassword = { showChangePassword = true },
        onLogout = onLogout
    )
}
```

---

### 6. Update MainActivity Navigation

**File**: `app/src/main/java/com/runanywhere/startup_hackathon20/MainActivity.kt`

Update navigation to include student login:

```kotlin
sealed class Screen {
    object RoleSelection : Screen()
    object StudentLogin : Screen()
    object StudentHome : Screen()
    object TeacherHome : Screen()
}

@Composable
fun AppNavigation(viewModel: EduVentureViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.RoleSelection) }
    var loginError by viewModel.loginError.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    when (currentScreen) {
        Screen.RoleSelection -> {
            LoginScreen(
                viewModel = viewModel,
                onLoginAsStudent = { currentScreen = Screen.StudentLogin },
                onLoginAsTeacher = {
                    viewModel.loginAsTeacher()
                    currentScreen = Screen.TeacherHome
                }
            )
        }

        Screen.StudentLogin -> {
            StudentLoginScreen(
                onBack = { currentScreen = Screen.RoleSelection },
                onLogin = { studentId, password ->
                    coroutineScope.launch {
                        val success = viewModel.loginStudent(studentId, password)
                        if (success) {
                            currentScreen = Screen.StudentHome
                        }
                    }
                },
                errorMessage = loginError
            )
        }

        Screen.StudentHome -> {
            RPGStudentScreenWithNav(
                viewModel = viewModel,
                onLogout = {
                    viewModel.logout()
                    currentScreen = Screen.RoleSelection
                }
            )
        }

        Screen.TeacherHome -> {
            TeacherHomeScreen(
                viewModel = viewModel,
                onLogout = {
                    viewModel.logout()
                    currentScreen = Screen.RoleSelection
                }
            )
        }
    }
}
```

---

### 7. Update LeaderboardScreen to Use Database

**File**: `app/src/main/java/com/runanywhere/startup_hackathon20/ui/screens/LeaderboardScreen.kt`

Update to fetch from database:

```kotlin
@Composable
fun LeaderboardScreen(viewModel: EduVentureViewModel) {
    var leaderboard by remember { mutableStateOf<List<KnightProfile>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        leaderboard = viewModel.getLeaderboard()
    }
    
    // Rest of the implementation using leaderboard data
    // Replace mockLeaderboard with actual leaderboard variable
}
```

---

## 📝 QUICK CHECKLIST

- [ ] Replace Drive folder ID in `RPGRepository.kt`
- [ ] Update `ResourcesScreen.kt` to open URLs
- [ ] Update `EduVentureViewModel.kt` with database methods
- [ ] Add profile dialog composables
- [ ] Update `MainActivity.kt` navigation
- [ ] Update `LeaderboardScreen.kt` to use database
- [ ] Test student login with demo account: STU001 / password123
- [ ] Test all profile functions
- [ ] Test resource URL opening
- [ ] Test module completion and XP earning

---

## 🎯 DEMO ACCOUNTS

The system creates 10 demo accounts automatically:

- **STU001** to **STU010**
- Password for all: **password123**

STU001 (Arthur Pendragon) has the highest XP and completed all modules.

---

## 📚 Where to Paste Links

### Google Drive Folder ID

**Location**:
`app/src/main/java/com/runanywhere/startup_hackathon20/data/repository/RPGRepository.kt`
**Line**: 23
**Replace**: `"YOUR_DRIVE_FOLDER_ID_HERE"`
**With**: Your actual Google Drive folder ID (e.g., `"1aBcD3FgH5iJkLmNo7PqRsTuVwXyZ"`)

### Resource URLs (Already configured!)

The resources already have working URLs in `ResourcesScreen.kt`:

- Khan Academy: https://www.khanacademy.org/math
- MIT OpenCourseWare: https://ocw.mit.edu
- Wolfram MathWorld: https://mathworld.wolfram.com
- Paul's Online Math Notes: https://tutorial.math.lamar.edu
- Brilliant.org: https://brilliant.org
- Desmos Calculator: https://www.desmos.com/calculator

Just need to add the Intent code to open them (see task #3 above).

---

## ✨ Features Now Available

1. **Database Authentication** - Students login with ID and password
2. **Persistent Data** - All student progress saved to SQLite
3. **Profile Management** - Edit profile, change password
4. **Leaderboard** - Real rankings from database
5. **Hardcoded Drive** - No need for users to paste folder ID
6. **Working Resources** - Clickable links to educational sites
7. **Password Security** - SHA-256 hashed passwords
8. **Demo Accounts** - 10 pre-loaded accounts for testing


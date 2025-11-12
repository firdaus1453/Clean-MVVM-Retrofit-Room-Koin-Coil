# Android Clean MVVM Template

A production-ready Android project template with Clean Architecture and MVVM pattern, pre-configured with essential modern libraries for rapid app development.

## Tech Stack

### Core
- **Kotlin** 2.0.21
- **Jetpack Compose** - Modern declarative UI
- **Clean Architecture** - Domain, Data, Presentation layers
- **MVVM Pattern** - ViewModels with state management

### Key Libraries
- **Retrofit** 3.0.0 - REST API client
- **Room** 2.6.1 - Local database
- **Koin** 3.5.6 - Dependency injection
- **Coil** 2.7.0 - Image loading
- **Kotlinx Coroutines** - Async operations
- **Kotlinx Serialization** - JSON parsing
- **Encrypted SharedPreferences** - Secure local storage
- **Timber** 5.0.1 - Logging
- **Navigation Compose** - Screen navigation

## Project Structure

```
app/src/main/java/
├── core/
│   ├── data/
│   │   ├── auth/              # Encrypted session storage
│   │   └── networking/        # Retrofit setup
│   ├── database/              # Room database & DAOs
│   ├── domain/                # Business logic & models
│   ├── di/                    # Koin modules
│   ├── presentation/          # UI components & theme
│   └── navigation/            # Navigation routes
└── feature/                   # Feature modules
```

## Key Features

### Networking
- Pre-configured Retrofit with OkHttp
- HTTP logging interceptor
- Centralized API error handling
- Type-safe Result wrapper

### Database
- Room database setup
- Type converters
- Basic CRUD operations
- Coroutines support

### Security
- Encrypted SharedPreferences using AndroidX Security
- Secure session storage
- Master key encryption

### Dependency Injection
- Koin modules organized by feature
- Easy-to-extend DI setup
- ViewModel injection support

## Getting Started

### Prerequisites
- Android Studio Ladybug or later
- JDK 11
- Android SDK 26+

### Setup

1. Clone the template:
```bash
git clone <your-repo-url>
cd CleanMVVMRetrofit
```

2. Update package name in:
   - `build.gradle.kts`
   - Refactor package structure

3. Configure API base URL in `app/build.gradle.kts`:
```kotlin
buildConfigField("String", "BASE_URL", "\"https://your-api.com/\"")
```

4. Sync and build:
```bash
./gradlew build
```

## Usage

### Adding a New Feature

1. Create feature package under `feature/`
2. Implement layers: `data`, `domain`, `presentation`
3. Add Koin module in `feature/di/`
4. Register module in `App.kt`
5. Define routes in `core/navigation/Routes.kt`

### Network Request Example

```kotlin
sealed interface Result<out D, out E: Error> {
    data class Success<out D>(val data: D): Result<D, Nothing>
    data class Error<out E: Error>(val error: E): Result<Nothing, E>
}

suspend fun getUsers(): Result<List<User>, DataError.Network> {
    return httpClient.get("/users")
}
```

### Database Operation

```kotlin
@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
}
```

## Configuration

### Build Variants
- **Debug**: Development with logging enabled
- **Release**: Production-ready with ProGuard

### Gradle Version Catalog
All dependencies managed in `gradle/libs.versions.toml` for easy version updates.

## License

MIT License - Feel free to use this template for your projects.

## Contributing

Contributions welcome! Please open an issue or submit a PR.
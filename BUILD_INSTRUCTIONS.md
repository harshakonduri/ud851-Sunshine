# Building Vedic Vidya APK

## Prerequisites

1. **Install Android Studio** (Latest version recommended)
   - Download from: https://developer.android.com/studio
   - Install Android SDK during setup

2. **Java Development Kit (JDK) 17**
   - Android Studio usually includes this
   - Or download from: https://adoptium.net/

3. **Git** (to clone the repository)

## Build Steps

### Option 1: Build with Android Studio (Recommended)

1. **Clone the repository:**
   ```bash
   git clone <your-repo-url>
   cd ud851-Sunshine
   git checkout claude/hindu-knowledge-app-bootstrap-jKd15
   ```

2. **Open in Android Studio:**
   - Launch Android Studio
   - File → Open
   - Navigate to project folder and select it
   - Wait for Gradle sync to complete (may take 5-10 minutes first time)

3. **Build APK:**
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Wait for build to complete
   - Click "locate" in the notification to find the APK
   - APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

4. **Install on Device:**
   - Connect Android device via USB (enable USB Debugging)
   - Or use Android Emulator in Android Studio
   - Run → Run 'app' (or press Shift+F10)

### Option 2: Build from Command Line

1. **Clone repository:**
   ```bash
   git clone <your-repo-url>
   cd ud851-Sunshine
   git checkout claude/hindu-knowledge-app-bootstrap-jKd15
   ```

2. **Set ANDROID_HOME environment variable:**

   **On macOS/Linux:**
   ```bash
   export ANDROID_HOME=$HOME/Library/Android/sdk  # macOS
   # or
   export ANDROID_HOME=$HOME/Android/Sdk  # Linux
   ```

   **On Windows:**
   ```cmd
   set ANDROID_HOME=%LOCALAPPDATA%\Android\Sdk
   ```

3. **Create Gradle Wrapper (if not present):**
   ```bash
   gradle wrapper --gradle-version 7.6
   ```

4. **Build Debug APK:**
   ```bash
   ./gradlew assembleDebug
   ```

   On Windows:
   ```cmd
   gradlew.bat assembleDebug
   ```

5. **Find the APK:**
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

### Option 3: Build Release APK (Optimized, smaller size)

1. **Build Release APK:**
   ```bash
   ./gradlew assembleRelease
   ```

2. **Find the APK:**
   ```
   app/build/outputs/apk/release/app-release-unsigned.apk
   ```

**Note:** For production release, you'll need to sign the APK with a keystore.

## Building Signed Release APK

### Create Keystore (One-time setup):

```bash
keytool -genkey -v -keystore vedic-vidya.keystore \
  -alias vedic-vidya -keyalg RSA -keysize 2048 -validity 10000
```

### Sign the APK:

1. **Add signing config to `app/build.gradle.kts`:**

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("../vedic-vidya.keystore")
            storePassword = "your-password"
            keyAlias = "vedic-vidya"
            keyPassword = "your-password"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            // ... rest of config
        }
    }
}
```

2. **Build signed APK:**
   ```bash
   ./gradlew assembleRelease
   ```

3. **APK location:**
   ```
   app/build/outputs/apk/release/app-release.apk
   ```

## Installation

### Install on Android Device:

1. **Enable Unknown Sources:**
   - Settings → Security → Install unknown apps
   - Allow installation from Files/Browser

2. **Transfer APK to device:**
   - USB transfer
   - Email to yourself
   - Upload to Google Drive/Dropbox

3. **Install:**
   - Tap the APK file
   - Follow installation prompts

### Install via ADB:

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Troubleshooting

### Build Fails - SDK Not Found

**Solution:** Set ANDROID_HOME environment variable (see step 2 in command line build)

### Build Fails - Gradle Sync Issue

**Solution:**
```bash
./gradlew clean
./gradlew build
```

### Out of Memory Error

**Solution:** Add to `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
```

### Dependency Download Fails

**Solution:** Check internet connection and try:
```bash
./gradlew build --refresh-dependencies
```

## APK Size Optimization

The debug APK will be ~20-30MB. For smaller size:

1. Use release build (minification enabled)
2. Expected release APK: ~10-15MB
3. Further optimization possible with app bundles (AAB)

## Building App Bundle (for Google Play)

```bash
./gradlew bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab`

## System Requirements

**Minimum Android Version:** Android 8.0 (API 26)
**Target Android Version:** Android 14 (API 34)
**Recommended Device RAM:** 2GB+
**Storage:** 50-100MB (including cached data)

## Quick Start After Install

1. Open **Vedic Vidya** app
2. Navigate through tabs:
   - 🏠 **Home**: Daily wisdom and quick access
   - 📚 **Library**: Browse sacred texts
   - 💬 **Q&A**: Ask questions (currently demo mode)
   - 👤 **Profile**: Settings and information

3. To enable real AI responses:
   - Get API key from Anthropic Claude, Google Gemini, or OpenAI
   - See README.md for integration instructions

## Next Steps

After building and installing:

1. Test the app functionality
2. Add real LLM API key for intelligent responses
3. Load Vedic scripture data
4. Customize settings and preferences

## Support

For build issues, check:
- Android Studio version (latest recommended)
- Gradle version compatibility
- Android SDK components installed
- Java/JDK version (17 required)

---

**Ready to spread Vedic wisdom!** 🕉️

**Om Tat Sat**

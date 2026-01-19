# Troubleshooting: ClassNotFoundException for VedicVidyaApplication

## Problem

```
Caused by: java.lang.ClassNotFoundException: Didn't find class "com.vedicvidya.VedicVidyaApplication" on path: DexPathList
```

## Root Cause

This error occurs because **Hilt's annotation processor (KSP) hasn't generated the required classes yet**.

When you use `@HiltAndroidApp` on `VedicVidyaApplication`, Hilt generates a class called `Hilt_VedicVidyaApplication` at build time. If this class doesn't exist, the app crashes on launch.

## Solution: Clean Build and Regenerate Hilt Classes

Follow these steps **in order**:

### Step 1: Clean All Build Artifacts

**In Android Studio:**
```
Build → Clean Project
```

**Or from terminal:**
```bash
cd /home/user/ud851-Sunshine
./gradlew clean
```

### Step 2: Invalidate Caches (Android Studio Only)

```
File → Invalidate Caches → Invalidate and Restart
```

This ensures Android Studio doesn't use stale cached data.

### Step 3: Delete Build Directories (Optional but Recommended)

```bash
cd /home/user/ud851-Sunshine
rm -rf .gradle
rm -rf build
rm -rf app/build
rm -rf app/.cxx
```

### Step 4: Sync Gradle Files

**In Android Studio:**
```
File → Sync Project with Gradle Files
```

**Or from terminal:**
```bash
./gradlew --refresh-dependencies
```

### Step 5: Rebuild Project

**In Android Studio:**
```
Build → Rebuild Project
```

**Or from terminal:**
```bash
./gradlew assembleDebug
```

This will:
1. Run KSP annotation processing
2. Generate Hilt classes (like `Hilt_VedicVidyaApplication`)
3. Generate Room DAOs
4. Compile all Kotlin code
5. Build the APK

### Step 6: Verify Hilt Classes Were Generated

Check that the following directory exists and contains generated files:

```bash
ls -la app/build/generated/ksp/debug/kotlin/
```

You should see files like:
- `com/vedicvidya/Hilt_VedicVidyaApplication.kt`
- `com/vedicvidya/MainActivity_GeneratedInjector.kt`
- Room DAO implementations

### Step 7: Run the App

**In Android Studio:**
```
Run → Run 'app' (Shift + F10)
```

**Or from terminal:**
```bash
./gradlew installDebug
adb shell am start -n com.vedicvidya/.MainActivity
```

## Additional Checks

### Verify Hilt Configuration

**1. Check `app/build.gradle.kts` has KSP and Hilt plugins:**

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")           // ✓ Required for Hilt
    id("com.google.dagger.hilt.android")    // ✓ Required for Hilt
}

dependencies {
    implementation("com.google.dagger:hilt-android:2.44")
    ksp("com.google.dagger:hilt-android-compiler:2.44")  // ✓ Uses ksp, not kapt
}
```

**2. Check `build.gradle.kts` (root) has correct versions:**

```kotlin
plugins {
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
}
```

**3. Check `VedicVidyaApplication.kt` has @HiltAndroidApp:**

```kotlin
@HiltAndroidApp
class VedicVidyaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
```

**4. Check `AndroidManifest.xml` references the Application class:**

```xml
<application
    android:name=".VedicVidyaApplication"
    ...>
```

## Common Mistakes

### ❌ Using `kapt` instead of `ksp`

```kotlin
// WRONG - Don't use kapt
kapt("com.google.dagger:hilt-android-compiler:2.44")

// CORRECT - Use ksp
ksp("com.google.dagger:hilt-android-compiler:2.44")
```

### ❌ Missing KSP plugin

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Missing: id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}
```

### ❌ Wrong Application class name in AndroidManifest

```xml
<!-- WRONG -->
<application android:name="VedicVidyaApplication" ...>

<!-- CORRECT -->
<application android:name=".VedicVidyaApplication" ...>
```

## If Problem Persists

### Check Gradle Build Output

Look for KSP errors in the build log:

```bash
./gradlew assembleDebug --info 2>&1 | grep -i "ksp\|hilt"
```

### Check for Annotation Processing Errors

```bash
./gradlew assembleDebug 2>&1 | grep -i "error"
```

### Verify Java Version

Hilt requires Java 17 (matching your Kotlin jvmTarget):

```bash
./gradlew --version
```

Should show:
```
JVM: 17.x.x
```

### Check ProGuard Rules (Release Build Only)

If the error only occurs in release builds, check `app/proguard-rules.pro`:

```proguard
# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ApplicationComponentManager { *; }

# Keep application class
-keep class com.vedicvidya.VedicVidyaApplication { *; }
-keep class **Hilt* { *; }
```

## Still Not Working?

1. **Share the full error stack trace** from Logcat
2. **Share the Gradle build output**: `./gradlew assembleDebug --stacktrace > build.log`
3. **Check if KSP generated any files**: `ls -R app/build/generated/ksp/`

## Expected Result

After following these steps, you should see:

✅ Build completes successfully
✅ Hilt classes generated in `app/build/generated/ksp/debug/kotlin/`
✅ App launches without ClassNotFoundException
✅ Database auto-populates with sample verses on first launch

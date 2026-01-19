# Android Studio Setup Guide

## ⚠️ Important: Gradle Wrapper Setup Required

Your project is **almost complete** but needs one critical file to work in Android Studio:

### The Missing File: `gradle-wrapper.jar`

This file is required for Android Studio to detect and build the project. Here's how to add it:

## ✅ Quick Fix (Recommended - 30 seconds)

### Option 1: Let Android Studio Generate It

1. **Open the project in Android Studio**
2. You'll see an error: "Gradle wrapper not found"
3. Android Studio will offer to **"Create Gradle wrapper"** - **Click Yes**
4. Wait 30 seconds for it to download
5. **Done!** Gradle targets will appear

### Option 2: Command Line (If you have Gradle installed)

```bash
cd /path/to/ud851-Sunshine
gradle wrapper --gradle-version 7.6
```

This will download and create `gradle/wrapper/gradle-wrapper.jar`

### Option 3: Manual Download

1. Download from: https://raw.githubusercontent.com/gradle/gradle/v7.6.0/gradle/wrapper/gradle-wrapper.jar
2. Save to: `gradle/wrapper/gradle-wrapper.jar`
3. Reload project in Android Studio

---

## 🔧 Complete Setup Steps

### Step 1: Clone & Open

```bash
git clone <your-repo-url>
cd ud851-Sunshine
git checkout claude/hindu-knowledge-app-bootstrap-jKd15
```

Open in Android Studio:
- File → Open
- Select the `ud851-Sunshine` folder

### Step 2: Fix Gradle Wrapper

When Android Studio shows "Gradle wrapper not found":
- Click **"Create Gradle wrapper"** or **"OK"** to auto-fix

### Step 3: Wait for Gradle Sync

- Android Studio will download dependencies (5-10 minutes first time)
- Progress shown in bottom status bar
- Wait for "Gradle sync finished" message

### Step 4: Verify Build Targets

You should now see in the top toolbar:
- **Build Variants** button
- **Run** button (green play icon)
- **Device/Emulator** selector

### Step 5: Build APK

- Build → Build Bundle(s) / APK(s) → Build APK(s)
- Or use toolbar: Run → Run 'app'

---

## 🐛 Troubleshooting

### Problem: "Gradle wrapper not found"

**Solution:** Let Android Studio create it (recommended), or manually download `gradle-wrapper.jar`

### Problem: "Failed to download Gradle distribution"

**Cause:** Network/firewall blocking downloads

**Solutions:**
1. Check internet connection
2. Try different network (e.g., mobile hotspot)
3. Configure proxy in `gradle.properties`:
   ```properties
   systemProp.http.proxyHost=yourproxy.com
   systemProp.http.proxyPort=8080
   systemProp.https.proxyHost=yourproxy.com
   systemProp.https.proxyPort=8080
   ```

### Problem: "Plugin [id: 'com.android.application'] not found"

**Cause:** Gradle can't download Android Gradle Plugin

**Solutions:**
1. Check internet connection
2. Update `build.gradle.kts` to use a compatible AGP version
3. Try File → Invalidate Caches → Restart

### Problem: No build targets in toolbar

**Cause:** Gradle sync incomplete or failed

**Solutions:**
1. File → Sync Project with Gradle Files
2. Build → Clean Project, then Build → Rebuild Project
3. File → Invalidate Caches → Restart
4. Check "Build" tab at bottom for errors

### Problem: "Unsupported Gradle version"

**Solution:** The project uses Gradle 7.6 with Android Gradle Plugin 7.4.2. This is compatible with:
- Android Studio Flamingo (2022.2.1) or newer
- JDK 17

Update Android Studio if needed.

### Problem: Build fails with "Execution failed for task ':app:compileDebugKotlin'"

**Solutions:**
1. File → Invalidate Caches → Restart
2. Delete `.gradle` folder and `build` folders:
   ```bash
   rm -rf .gradle app/build build
   ```
3. Sync project again

---

## 📋 What You Should See

### After Opening Project:

```
ud851-Sunshine
├── app
│   ├── build.gradle.kts ✅
│   └── src
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar ⚠️ (will be created)
│       └── gradle-wrapper.properties ✅
├── build.gradle.kts ✅
├── gradlew ✅
├── gradlew.bat ✅
└── settings.gradle.kts ✅
```

### In Android Studio Toolbar:

```
[app] ▼  [debug] ▼  [Pixel 5 API 34] ▼  [▶ Run] [⬛ Stop] [🔨 Build]
```

### In Project Structure (left sidebar):

```
Project
├── app
│   ├── manifests
│   │   └── AndroidManifest.xml
│   ├── java
│   │   └── com.vedicvidya
│   │       ├── MainActivity.kt
│   │       ├── VedicVidyaApp.kt
│   │       ├── VedicVidyaApplication.kt
│   │       ├── data
│   │       ├── domain
│   │       └── ui
│   └── res
│       ├── values
│       └── xml
└── Gradle Scripts
    ├── build.gradle.kts (Project)
    ├── build.gradle.kts (Module: app)
    └── settings.gradle.kts
```

---

## 🎯 Verification Checklist

After setup, verify everything works:

- [ ] Project opens without errors
- [ ] Gradle sync completes successfully
- [ ] "Build" and "Run" buttons are enabled in toolbar
- [ ] Can select device/emulator from dropdown
- [ ] Build succeeds (Build → Build Bundle(s) / APK(s) → Build APK(s))
- [ ] No red code errors in source files
- [ ] Can run app on emulator/device

---

## 🚀 Alternative: Use GitHub Actions (No Local Setup)

If you can't set up locally, you can build APK using GitHub Actions:

1. **Create `.github/workflows/build.yml`:**

```yaml
name: Android CI

on:
  push:
    branches: [ claude/hindu-knowledge-app-bootstrap-jKd15 ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Grant execute permission for gradlew
        run: chmod +x gradlew

      - name: Build with Gradle
        run: ./gradlew assembleDebug

      - name: Upload APK
        uses: actions/upload-artifact@v3
        with:
          name: app-debug
          path: app/build/outputs/apk/debug/app-debug.apk
```

2. **Push to GitHub**
3. **Go to Actions tab** in your repository
4. **Download APK** from artifacts

---

## 📱 Expected Build Output

**APK Location:** `app/build/outputs/apk/debug/app-debug.apk`

**APK Size:** ~15-25 MB

**Build Time:**
- First build: 5-10 minutes
- Incremental builds: 30-60 seconds

---

## ✅ Summary

**The project is complete**, but Android Studio needs the `gradle-wrapper.jar` file to build it.

**Easiest solution:**
1. Open project in Android Studio
2. Click "Create Gradle wrapper" when prompted
3. Wait for download
4. Build APK

**That's it!** 🎉

If you encounter any issues not covered here, check:
- Android Studio version (should be Flamingo 2022.2.1+)
- JDK version (should be 17)
- Internet connectivity
- Gradle logs in "Build" tab at bottom

---

**Need help?** Share the error message from the "Build" tab and we can troubleshoot!

**Om Tat Sat** 🕉️

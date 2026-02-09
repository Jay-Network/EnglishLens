# EnglishLens Android App

**Camera-based English Text Analyzer**

Point your camera at any English text for instant definitions, reading level analysis, and NLP insights.

## Quick Info

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Camera**: CameraX
- **OCR**: Google ML Kit (English/Latin)
- **Dictionary**: WordNet (147k words, 207k definitions, offline)
- **NLP**: Rule-based lemmatizer, POS tagger, NER
- **Target SDK**: Android 8.0+ (API 26+)
- **GitHub**: https://github.com/Jay-Network/EnglishLens

## Features

- Real-time OCR text detection via camera
- Tap any word for instant definitions, synonyms, and antonyms
- Lemma fallback: inflected words ("running", "went", "mice") resolve to base forms
- Readability scoring: Flesch-Kincaid, Flesch RE, SMOG, Coleman-Liau
- Partial/full screen camera modes
- 3-tier definition lookup: LruCache -> WordNet DB -> Cloud API (future)

## Development

This is the Android app source code, managed as a GitHub repository.

**Active Development Agent**: jworks:46 (EnglishLens-Dev)

## Build

```bash
./gradlew assembleDebug
```

## Structure

```
EnglishLens/
├── app/                   # Main Android app module
│   └── src/main/java/com/jworks/englishlens/
│       ├── data/          # Repository, Room DB, DAO
│       ├── domain/        # Models, use cases, NLP, analysis
│       └── ui/            # Compose screens, ViewModel
├── build.gradle.kts       # Root build configuration
├── settings.gradle.kts    # Gradle settings
└── gradle.properties      # Gradle properties
```

---

**Part of**: JWorks Apps Division
**Agent Workspace**: ~/1_jworks/A_ai/4_Apps/EnglishLens/
**GitHub Backup**: This repository

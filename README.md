# EigoLens

**Camera-based English text analyzer for language learners**

Point your camera at any English text, capture it, then tap any word for instant definitions. Circle phrases for multi-word analysis. Get reading level scores for any text.

## Features

- **Tap-to-define**: Tap any word on a captured snapshot for instant offline definitions
- **Circle selection**: Switch to circle mode to lasso multiple words or phrases
- **Offline dictionary**: 147,000+ words, 207,000+ definitions powered by WordNet
- **Smart lemmatization**: Inflected forms ("running", "went", "mice") resolve to base words
- **Readability analysis**: Flesch-Kincaid, Flesch Reading Ease, SMOG, Coleman-Liau scores
- **NLP pipeline**: Parts of speech, named entities, word frequency
- **Overlay panel**: Draggable results panel over full-screen image - resize by dragging
- **Two-finger zoom/pan**: Works alongside tap/circle without mode switching
- **Gallery import**: Analyze photos from your gallery
- **Guest mode**: Core features work without an account

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose |
| Camera | CameraX |
| OCR | Google ML Kit (Text Recognition) |
| Dictionary | WordNet (bundled SQLite, offline) |
| NLP | Rule-based lemmatizer, POS tagger, NER |
| Database | Room |
| DI | Hilt |
| Auth | Supabase (optional) |
| Min SDK | Android 8.0+ (API 26) |

## Architecture

```
app/src/main/java/com/jworks/eigolens/
├── data/
│   ├── auth/           # Supabase auth repository
│   ├── local/          # Room DB, WordNet DAO, entities
│   ├── preferences/    # DataStore settings
│   └── repository/     # Definition repository (cache → WordNet → cloud)
├── di/                 # Hilt modules
├── domain/
│   ├── analysis/       # Readability calculator
│   ├── auth/           # Auth interfaces
│   ├── models/         # OCRResult, Definition, DetectedText, etc.
│   ├── nlp/            # POS tagger, NER, lemmatizer
│   └── usecases/       # ProcessCameraFrame
└── ui/
    ├── auth/           # Login screen
    ├── camera/         # DefinitionPanel, ReadabilityPanel
    ├── capture/        # CaptureFlow, AnnotationMode, InteractiveImageViewer
    ├── feedback/       # In-app feedback
    ├── gallery/        # Gallery import
    ├── settings/       # Settings screen
    └── theme/          # Material3 theme
```

### Interaction Model

EigoLens uses a **snapshot-first** approach:

1. **Capture**: Take a photo or import from gallery. ML Kit OCR detects all text.
2. **Tap** (default): Single-tap any word → definition panel slides up. Two-finger zoom/pan works simultaneously.
3. **Circle** (toggle): Switch to circle mode via FAB → draw around words → lookup. Auto-returns to tap mode.
4. **Analyze**: Readability FAB provides full-text reading level analysis.

The results panel overlays the image (draggable height) so the snapshot stays full-screen.

## Build

```bash
# Debug build
./gradlew assembleDebug

# Run tests
./gradlew testDebugUnitTest

# Release build (requires signing config in local.properties)
./gradlew assembleRelease
```

### local.properties

```properties
# Supabase (optional - app works without)
AUTH_SUPABASE_URL=https://your-project.supabase.co
AUTH_SUPABASE_ANON_KEY=your-anon-key

# Signing (required for release builds)
RELEASE_STORE_FILE=../keystore/eigolens-release.jks
RELEASE_STORE_PASSWORD=...
RELEASE_KEY_ALIAS=eigolens
RELEASE_KEY_PASSWORD=...
```

## Roadmap

- **Phase A** (done): Tap-to-define, overlay panel, zoom/pan coexistence
- **Phase B** (next): AI-powered phrase/sentence analysis (Claude/Gemini integration)
- **Phase C**: Full-text AI analysis, scope mode bar
- **Phase D**: Reading context system - persistent history, bookmarks, cross-session AI context

## License

Proprietary - JWorks

---

**Developer**: JWorks | **Contact**: jay@jworks-ai.com | **Website**: https://jworks-ai.com

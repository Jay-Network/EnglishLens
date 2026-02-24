# Changelog

All notable changes to EigoLens will be documented in this file.

## [0.2.0] - 2026-02-24

### Added
- **Tap-to-define**: Tap any word on captured text for instant definitions (replaces lasso-only)
- **WordTapDetector**: Screen-to-image coordinate transform with 20px tolerance for imprecise taps
- **ScopeLevel**: Foundation for progressive analysis (Word, Phrase, Paragraph, FullSnapshot)
- **Overlay panel**: Draggable results panel over full-screen image (replaces 40/60 split layout)
- **Pulse highlight**: Blue rounded rect with animation on tapped words
- **Landscape support**: Panel anchors to right side in landscape orientation
- **ic_tap / ic_circle drawables**: New icons for interaction mode toggle
- **Robolectric**: Unit test support for Android classes
- 6 unit tests for WordTapDetector (roundtrip transforms, tap detection, tolerance, zoom/pan)

### Changed
- **InteractionMode**: `VIEW/DRAW` → `TAP/CIRCLE`. TAP is default, CIRCLE for lasso
- **Two-finger zoom/pan**: Now works in both TAP and CIRCLE modes (was VIEW-only)
- **Auto-return**: Circle mode auto-switches back to TAP after selection
- **PanelState**: Unified `LookupState` + `AnalysisMode` into single `PanelState` sealed class
- **InteractiveImageViewer**: State hoisted to parent, receives interaction mode + callbacks
- Idle message: "Tap any word to look it up, or switch to circle mode to select phrases"
- FAB icon: Shows opposite mode (tap icon when in circle, circle icon when in tap)

### Fixed
- **Room schema validation**: Rebuilt WordNet DB tables with `NOT NULL` on primary keys
- **Room entity annotations**: Added indices and foreign key to match bundled DB schema
- **Room identity hash**: Updated to match entity definitions
- **Nullable frequency**: `WordEntry.frequency` now `Int?` to match DB column

## [0.1.1] - 2026-02-20

### Changed
- Renamed from EnglishLens to EigoLens (full rebrand, package rename)

### Fixed
- ProGuard rules for release build stability
- Firebase graceful degradation (auto-init disabled)
- Session persistence with SharedPrefsSessionManager
- Camera null bitmap handling
- Runtime camera permission request
- Theme parent mismatch
- Misleading UI text for guest mode

### Added
- App icon (blue background, white magnifying glass vector)
- Store readiness checklist
- Store listing draft

## [0.1.0] - 2026-02-15

### Added
- Initial release
- Camera text capture with CameraX + ML Kit OCR
- Offline WordNet dictionary (147K words, 207K definitions)
- Lasso word selection with coordinate transforms
- Definition panel with POS tags, synonyms, antonyms, frequency badges
- Readability analysis (Flesch-Kincaid, Flesch RE, SMOG, Coleman-Liau)
- NLP pipeline (POS tagger, NER detector, lemmatizer)
- Gallery photo import
- Supabase auth (Google Sign-In, email/password)
- In-app feedback system
- Settings screen

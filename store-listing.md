# EigoLens - Google Play Store Listing

## App Name
EigoLens

## Short Description (80 characters max)
Point your camera at English text for instant definitions and reading analysis.

(76 characters)

## Full Description (4000 characters max)

EigoLens turns your camera into a powerful English reading assistant. Simply point at any English text, capture it, and get instant word definitions, part-of-speech tags, and reading level analysis.

CORE FEATURES

Camera Text Recognition
Capture any printed English text with your camera. EigoLens uses advanced OCR to detect words, sentences, and paragraphs with high accuracy. You can also import photos from your gallery.

Instant Word Definitions
Draw a circle around any word to look up its definition instantly. EigoLens includes a comprehensive offline dictionary with over 147,000 words and 207,000 definitions powered by WordNet. No internet connection required for word lookups.

Smart Word Analysis
Get detailed information for every word including:
- Multiple definitions organized by part of speech
- Synonyms and antonyms
- Word frequency indicators
- Automatic lemmatization (finds root forms of conjugated words)

Reading Level Analysis
Understand how difficult a text is with four standard readability formulas:
- Flesch-Kincaid Grade Level
- Flesch Reading Ease Score
- SMOG Index
- Coleman-Liau Index

Each text gets a difficulty rating from Elementary to Graduate level, helping you choose reading material at the right level.

Natural Language Processing
EigoLens includes a built-in NLP pipeline that identifies:
- Parts of speech (nouns, verbs, adjectives, etc.)
- Named entities (dates, numbers, proper nouns)
- Root word forms through lemmatization

PDF Export
Save your analysis results as professionally formatted PDF documents. Share them with teachers, classmates, or keep them for your own reference.

WHO IS ENGLISHLENS FOR?

- ESL/EFL students looking to build vocabulary while reading
- Language learners who want to understand real-world English text
- Students preparing for standardized English tests
- Teachers assessing text difficulty for their students
- Anyone who encounters unfamiliar English words in daily life

KEY HIGHLIGHTS

- Works offline: Dictionary and NLP features work without internet
- Fast: OCR processing in under 200ms
- Privacy-focused: Text analysis happens entirely on your device
- Clean interface: Capture, circle, and learn in seconds
- Free to use: Core features available without an account

EigoLens is developed by JWorks as part of our language learning toolkit.

---

## App Category
Primary: Education
Sub-category: Language Learning (if available, otherwise Education)

## Tags/Keywords
english dictionary, text scanner, OCR reader, vocabulary builder, reading level, ESL, English learner, word definitions, text analysis, readability

## Privacy Policy URL
https://jworks-ai.com/privacy/englishlens

## Contact Details
- Developer name: JWorks
- Contact email: jay@jworks-ai.com
- Website: https://jworks-ai.com

---

## Content Rating Questionnaire (IARC)

### Violence
- Does the app contain violence? **No**
- Does the app depict violence against characters? **No**
- Does the app contain graphic violence? **No**

### Sexual Content
- Does the app contain sexual content or nudity? **No**
- Does the app contain sexual themes? **No**

### Language
- Does the app contain profanity or crude humor? **No**
- Note: The dictionary contains standard English words which may include some that could be considered mature vocabulary, but these are presented in an educational/dictionary context only.

### Controlled Substances
- Does the app reference or depict drug use, alcohol, or tobacco? **No**

### Gambling
- Does the app contain gambling or simulated gambling? **No**

### User-Generated Content
- Does the app allow users to communicate or share content with each other? **No**
- Users can submit feedback to developers only (not visible to other users).

### Personal Information
- Does the app collect personal information? **Limited**
- Optional email for account creation (Supabase auth)
- Optional email for feedback submission
- No data is shared with third parties
- Camera is used only for text recognition (images are not stored or transmitted)

### Ads
- Does the app contain ads? **No**

### Recommended Rating: **Everyone (E)** or **PEGI 3**
- Educational content only
- No objectionable material
- Dictionary definitions are presented in academic context

---

## Target Audience & Content Settings

### Target Age Group
- **Primary**: 13 and older
- Note: App does NOT target children under 13. Not a "Designed for Families" app.
- Reason: Requires camera permission and optional account creation.

### Content Settings
- App is suitable for all audiences
- No mature content
- Educational purpose

---

## Screenshot Descriptions (for Vision agent to generate)

Recommended 8 phone screenshots showing:

1. **Camera Preview** - Clean camera interface with capture button
2. **Text Capture** - Captured text with OCR overlay showing detected words
3. **Lasso Selection** - User drawing circle around a word in DRAW mode
4. **Definition Card** - Word definition with POS tags, synonyms, frequency badge
5. **Readability Panel** - Reading level scores with difficulty indicator
6. **Gallery Import** - Importing a photo for text analysis
7. **PDF Export** - Generated PDF analysis report
8. **Login Screen** - Clean sign-in with Google and email options

## Feature Graphic (1024x500)
- App name "EigoLens" prominently displayed
- Visual showing camera viewfinder + text overlay + definition popup
- Color scheme: Dark background with blue (#2196F3) accents matching app theme
- Tagline: "Point. Capture. Learn."

---

## Data Safety Section (Google Play Console)

### Data Collection Overview

| Data Type | Collected | Shared | Purpose |
|-----------|-----------|--------|---------|
| Email address | Optional | No | Account creation, feedback |
| Auth tokens | Yes (if signed in) | No | Session management |
| Feedback text | Optional | No | User-submitted feedback to developer |
| Device info | Yes (with feedback) | No | Bug diagnosis (OS version, device model) |
| App settings | Yes | No | Highlight color, stroke width preferences |
| FCM token | Future (when Firebase added) | No | Push notifications for feedback updates |

### Data NOT Collected
- Camera images (processed on-device only, never transmitted)
- OCR text results (stays on-device)
- Dictionary lookup history (in-memory cache only)
- Location data
- Contacts
- Files (beyond user-selected gallery photos)

### Detailed Answers for Play Console Form

**Is any of the collected data encrypted in transit?**
Yes - all network communication uses HTTPS via Supabase/Ktor.

**Can users request that their data be deleted?**
Yes - users can sign out (clears session) and contact developer for account deletion.

**Does your app collect any of these data types?**

Location: No
Personal info:
  - Email address: Optional, for account creation and feedback. Not shared.
  - User IDs: Generated by Supabase auth. Not shared.
  - Name: No

Financial info: No
Health and fitness: No
Messages: No
Photos and videos: No (camera used for OCR only, images not stored or sent)
Audio: No
Files and docs: No
Calendar: No
Contacts: No
App activity: No
Web browsing: No
App info and performance:
  - Crash logs: No (no crash reporting SDK)
  - Device or other IDs: Device model/OS sent with feedback only

**Is this data processed ephemerally?**
Camera images: Yes - processed for OCR and discarded (not stored).
Dictionary lookups: Yes - LRU cache in memory, cleared on app close.

**Security practices:**
- Data encrypted in transit (HTTPS)
- Auth sessions stored in encrypted SharedPreferences
- No third-party analytics or tracking SDKs
- No ad SDKs

---

## Release Notes (v0.1.0 - Internal Testing)

### What's New
First internal release of EigoLens - your camera-powered English reading assistant.

Features included:
- Camera text capture with ML Kit OCR
- Offline dictionary with 147,000+ words (WordNet)
- Lasso word selection with instant definitions
- Reading level analysis (Flesch-Kincaid, SMOG, Coleman-Liau)
- NLP analysis (parts of speech, named entities, lemmatization)
- Gallery photo import
- PDF export of analysis results
- Google Sign-In and email authentication
- In-app feedback system

### Testing Focus Areas
Please test and report on:
1. Camera capture on your device (does OCR detect text reliably?)
2. Lasso word selection (can you circle words accurately?)
3. Definition lookups (are definitions showing correctly?)
4. Readability scores (do they seem reasonable for the text?)
5. Gallery import (can you import and analyze saved photos?)
6. Login flow (Google Sign-In and email/password)
7. PDF export (does it generate and share correctly?)
8. General stability (any crashes or freezes?)

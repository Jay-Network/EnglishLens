# Gemini Development Log — EigoSage

## Purpose

Track Gemini integration development for EigoSage. Claude (jworks:46) is the lead development agent; Gemini powers in-app AI features (chat tutor, OCR correction, text analysis). This log documents prompt iterations, Claude vs Gemini comparisons, ADK patterns, and TODOs.

Reference: jworks:104 (SheetMusicReader) for dual output pattern.

---

## Architecture

```
                  ┌──────────────┐
  Camera Frame ──▶│  ML Kit OCR  │──▶ Fast text extraction (offline, ~50ms)
                  └──────────────┘
                         │
                         ▼
                  ┌──────────────┐
                  │ GeminiOCR    │──▶ Vision-corrected text (online, ~1-2s)
                  │ Corrector    │    Merges with ML Kit via OcrTextMerger
                  └──────────────┘
                         │
              ┌──────────┴──────────┐
              ▼                     ▼
     ┌──────────────┐      ┌──────────────┐
     │ GeminiProvider│      │GeminiChat    │
     │ (Analysis)    │      │Client (Tutor)│
     └──────────────┘      └──────────────┘
     Word definitions,      Multi-turn English
     grammar, reading       tutor dialog with
     level, CEFR tags       context from scan
```

- **Claude (jworks:46)**: Lead developer — architecture, complex features, code review
- **Gemini (in-app)**: Three roles — OCR correction, text analysis, chat tutor
- **Model**: `gemini-2.5-flash` across all three clients

## SDK & Tools

- **In-app SDK**: Direct REST API via Ktor (`generativelanguage.googleapis.com/v1beta`)
- **Model**: `gemini-2.5-flash` (vision + text)
- **ADK**: `@google/adk` v0.5.0 (available for Live agent — not yet integrated)
- **Potential Live SDK**: `@google/genai` for Gemini Live voice agent

---

## Current Gemini Features (v0.6.0)

### 1. OCR Correction (`GeminiOcrCorrector`)
- **What**: Sends camera frame bitmap to Gemini Vision for text extraction
- **Why**: Supplements ML Kit for difficult text (handwriting, stylized fonts, poor lighting)
- **Merge strategy**: `OcrTextMerger` combines ML Kit + Gemini results
- **Prompt**: Minimal — "extract all visible English text, return line-by-line"

### 2. Text Analysis (`GeminiProvider`)
- **What**: Analyzes scanned text for definitions, grammar, reading level
- **Why**: Core value prop — point camera at text, get instant analysis
- **Prompt**: Uses `AiPrompts.SYSTEM_PROMPT` + `AiPrompts.buildPrompt(context)`
- **Context**: Includes scope (word/phrase/page), surrounding text, user CEFR level

### 3. Chat Tutor (`GeminiChatClient`)
- **What**: Multi-turn English tutor dialog seeded with scanned text context
- **Why**: Allows follow-up questions ("What does this idiom mean?", "Translate to Japanese")
- **System prompt**: English language tutor persona, concise, markdown formatting
- **Context**: Full conversation history passed as message pairs

---

## Gemini Live Agent Candidates

Features suitable for a standalone Gemini Live agent (voice + camera):

| Feature | Priority | Rationale |
|---------|----------|-----------|
| **Voice reading tutor** | High | Read text aloud, discuss content via voice — natural Live API fit |
| **OCR + dialog** | High | Camera sees text, AI explains via voice — already have the pipeline |
| **Pronunciation coach** | Medium | User reads aloud, AI provides feedback — Live API bidirectional audio |
| **Vocabulary quiz** | Medium | AI asks about words from scanned text — voice Q&A natural |
| **Reading companion** | High | Adapted from BookSage Live patterns — cross-page memory + personas |

### Persona System (adapted from BookSage Live)

| Persona | Role | Voice (proposed) |
|---------|------|-------------------|
| **Lexicon** | Vocabulary focus — definitions, synonyms, etymology | Kore |
| **Sage** | Comprehension — reading level, main ideas, context | Charon |
| **Tutor** | Practice — grammar drills, translation exercises | Puck |

---

## Prompt Iterations

### v1 (v0.5.1) — Initial

**GeminiChatClient system prompt:**
> You are an English language tutor helping a user understand text they captured with EigoSage (an English reading assistant app). Be concise, helpful, and friendly. Use simple English when possible. If asked to translate, provide the translation along with brief notes on nuance. Format responses with markdown bold for key terms and bullet points for lists.

**Observations:**
- Works well for basic Q&A about scanned text
- No CEFR-level adaptation (same response regardless of user level)
- No proactive vocabulary flagging
- No persona differentiation

### v2 (v0.6.0) — Current Production

**Changes implemented:**
- ✅ CEFR-adapted system prompt via `GeminiChatClient.buildCefrSystemPrompt(cefrLevel)` — 6 distinct level-specific instruction sets (A1→C2)
- ✅ Readability score (Flesch-Kincaid grade, Flesch RE, difficulty) included in chat context seed
- ✅ User's CEFR level passed in seed message for grounded responses
- ✅ `systemPrompt` parameter on `send()` — system prompt stored in `PanelState.Chat` and reused across the session

**Observations:**
- CEFR adaptation untested end-to-end (needs device testing across all 6 levels)
- No persona differentiation yet
- No proactive vocabulary flagging yet

**Planned v3 improvements:**
- Add persona mode selection (Lexicon/Sage/Tutor)
- Smart follow-up suggestion chips (CEFR-adapted)
- Auto-bookmark words discussed in chat
- Proactive flagging of difficult words based on user's level

---

## Comparison Log

| Feature | Claude (dev) | Gemini (in-app) | Notes |
|---------|-------------|-----------------|-------|
| OCR accuracy | N/A (not in-app) | Good for printed, weak on handwriting | ML Kit handles fast path |
| Text analysis | Gold standard prompts | v1 system prompt | Need structured comparison |
| Chat quality | N/A | Adequate for basic Q&A | Needs CEFR adaptation |

---

## ADK Patterns Learned

1. **ADK v0.5.0 does NOT have `runLive()`** — use `@google/genai` directly for Live API WebSocket (from PianoQuest Live, BookSage Live)
2. **FunctionTool** — wraps callable functions with Zod schemas for type-safe tool declarations
3. **LlmAgent** — agent registration with tools, system instructions, health metadata
4. **AutoFlow routing** — BookSage Live is migrating to this for persona sub-agent routing (watch for results)
5. **Context seeding** — inject scanned text + readability metrics at session start for grounded responses
6. **Periodic context injection** — refresh AI memory every N messages with updated scan context (BookSage pattern)

## Cross-Project References

- **SheetMusicReader** (jworks:104): Dual output pattern — Claude gold standard vs Gemini automated
- **BookSage Live** (jworks:102): Cross-page memory, persona system, ADK multi-agent with AutoFlow
- **KanjiSage** (jworks:43): Sibling app — same camera+OCR architecture for Japanese

---

## TODOs

- [x] Add CEFR level to system prompt for difficulty-appropriate responses (v0.6.0)
- [x] Add readability score to chat context seed (v0.6.0)
- [ ] Test CEFR-adapted system prompts (v2) — compare response quality across levels
- [ ] Smart follow-up suggestion chips (CEFR-adapted)
- [ ] Auto-bookmark words discussed in chat
- [ ] Implement persona mode in GeminiChatClient (Lexicon/Sage/Tutor)
- [ ] Add proactive vocabulary flagging based on user's CEFR level
- [ ] Create Gemini Live agent prototype (voice + camera reading tutor)
- [ ] Explore `@google/genai` SDK for Live API WebSocket integration
- [ ] Benchmark Gemini 2.5 Flash vs Pro for text analysis quality
- [ ] Adapt BookSage Live's cross-page memory pattern for multi-scan sessions
- [ ] Track BookSage's ADK AutoFlow migration results for persona routing
- [ ] Set up dual output comparison: test same prompts on Claude API vs Gemini API

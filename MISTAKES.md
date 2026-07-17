# EigoSage Mistakes

## Lessons Learned

- **S-358: Gemini thinking tokens can silently truncate output** (2026-06-01) — When upgrading to Gemini 3.5 Flash (which enables thinking by default), output was being silently truncated. Fix: explicitly set `thinkingBudget: 0` on all REST API call sites. Always check model defaults when upgrading.
- **OcrTextMerger word-to-box misalignment** (pre-v0.8.0) — Early merge strategy tried to align words even when ML Kit and Gemini had different word counts per line. This caused taps to look up the wrong word. Fix: only replace text when word counts match exactly; otherwise keep ML Kit's original text.

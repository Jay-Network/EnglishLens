package com.jworks.kanjilens.domain.models;

import org.junit.Test;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0010\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J\b\u0010\u0005\u001a\u00020\u0004H\u0007J\b\u0010\u0006\u001a\u00020\u0004H\u0007J\b\u0010\u0007\u001a\u00020\u0004H\u0007J\b\u0010\b\u001a\u00020\u0004H\u0007J\b\u0010\t\u001a\u00020\u0004H\u0007J\b\u0010\n\u001a\u00020\u0004H\u0007J\b\u0010\u000b\u001a\u00020\u0004H\u0007J\b\u0010\f\u001a\u00020\u0004H\u0007J\b\u0010\r\u001a\u00020\u0004H\u0007J\b\u0010\u000e\u001a\u00020\u0004H\u0007J\b\u0010\u000f\u001a\u00020\u0004H\u0007J\b\u0010\u0010\u001a\u00020\u0004H\u0007J\b\u0010\u0011\u001a\u00020\u0004H\u0007J\b\u0010\u0012\u001a\u00020\u0004H\u0007J\b\u0010\u0013\u001a\u00020\u0004H\u0007\u00a8\u0006\u0014"}, d2 = {"Lcom/jworks/kanjilens/domain/models/JapaneseTextUtilTest;", "", "()V", "containsHiragana detects hiragana", "", "containsJapanese returns false for ASCII only", "containsJapanese returns true for hiragana", "containsJapanese returns true for kanji", "containsJapanese returns true for katakana", "containsJapanese returns true for mixed text", "containsKanji returns false for ASCII", "containsKanji returns false for kana only", "containsKanji returns true for CJK unified ideographs", "containsKatakana detects katakana", "japaneseRatio below threshold filters URLs and numbers", "japaneseRatio returns 0 for empty string", "japaneseRatio returns 0 for non-Japanese text", "japaneseRatio returns 1 for all-Japanese text", "japaneseRatio returns correct ratio for mixed text", "kanjiCount returns correct count", "app_debugUnitTest"})
public final class JapaneseTextUtilTest {
    
    public JapaneseTextUtilTest() {
        super();
    }
}
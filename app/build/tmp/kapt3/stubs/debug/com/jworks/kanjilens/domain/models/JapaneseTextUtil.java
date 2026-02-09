package com.jworks.kanjilens.domain.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\r\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\u000e\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\n\u001a\u00020\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/jworks/kanjilens/domain/models/JapaneseTextUtil;", "", "()V", "HIRAGANA_RANGE", "Lkotlin/text/Regex;", "JAPANESE_RANGE", "KANJI_RANGE", "KATAKANA_RANGE", "containsHiragana", "", "text", "", "containsJapanese", "containsKanji", "containsKatakana", "japaneseRatio", "", "kanjiCount", "", "app_debug"})
public final class JapaneseTextUtil {
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex KANJI_RANGE = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex HIRAGANA_RANGE = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex KATAKANA_RANGE = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.text.Regex JAPANESE_RANGE = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.jworks.kanjilens.domain.models.JapaneseTextUtil INSTANCE = null;
    
    private JapaneseTextUtil() {
        super();
    }
    
    public final boolean containsKanji(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
        return false;
    }
    
    public final boolean containsJapanese(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
        return false;
    }
    
    public final boolean containsHiragana(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
        return false;
    }
    
    public final boolean containsKatakana(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
        return false;
    }
    
    public final int kanjiCount(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
        return 0;
    }
    
    public final float japaneseRatio(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
        return 0.0F;
    }
}
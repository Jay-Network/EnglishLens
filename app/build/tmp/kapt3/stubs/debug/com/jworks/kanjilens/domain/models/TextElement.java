package com.jworks.kanjilens.domain.models;

import android.graphics.Rect;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u00c6\u0003JE\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u00c6\u0001J\u0013\u0010\u001c\u001a\u00020\u00072\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001e\u001a\u00020\u001fH\u00d6\u0001J\t\u0010 \u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0013\u0010\b\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0014\u00a8\u0006!"}, d2 = {"Lcom/jworks/kanjilens/domain/models/TextElement;", "", "text", "", "bounds", "Landroid/graphics/Rect;", "containsKanji", "", "reading", "kanjiSegments", "", "Lcom/jworks/kanjilens/domain/models/KanjiSegment;", "(Ljava/lang/String;Landroid/graphics/Rect;ZLjava/lang/String;Ljava/util/List;)V", "getBounds", "()Landroid/graphics/Rect;", "getContainsKanji", "()Z", "getKanjiSegments", "()Ljava/util/List;", "getReading", "()Ljava/lang/String;", "getText", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class TextElement {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String text = null;
    @org.jetbrains.annotations.Nullable()
    private final android.graphics.Rect bounds = null;
    private final boolean containsKanji = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String reading = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.jworks.kanjilens.domain.models.KanjiSegment> kanjiSegments = null;
    
    public TextElement(@org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.Nullable()
    android.graphics.Rect bounds, boolean containsKanji, @org.jetbrains.annotations.Nullable()
    java.lang.String reading, @org.jetbrains.annotations.NotNull()
    java.util.List<com.jworks.kanjilens.domain.models.KanjiSegment> kanjiSegments) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getText() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.graphics.Rect getBounds() {
        return null;
    }
    
    public final boolean getContainsKanji() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getReading() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.jworks.kanjilens.domain.models.KanjiSegment> getKanjiSegments() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.graphics.Rect component2() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.jworks.kanjilens.domain.models.KanjiSegment> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.jworks.kanjilens.domain.models.TextElement copy(@org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.Nullable()
    android.graphics.Rect bounds, boolean containsKanji, @org.jetbrains.annotations.Nullable()
    java.lang.String reading, @org.jetbrains.annotations.NotNull()
    java.util.List<com.jworks.kanjilens.domain.models.KanjiSegment> kanjiSegments) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}
package com.jworks.kanjilens.data.nlp;

import android.util.Log;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.jworks.kanjilens.domain.models.JapaneseTextUtil;
import com.jworks.kanjilens.domain.models.JapaneseToken;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0007\u001a\u00020\bJ\u0006\u0010\t\u001a\u00020\u0004J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0002J$\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010\u0010\u001a\u00020\u000b2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u000eH\u0002J\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010\f\u001a\u00020\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/jworks/kanjilens/data/nlp/KuromojiTokenizer;", "", "()V", "initialized", "", "tokenizer", "Lcom/atilika/kuromoji/ipadic/Tokenizer;", "initialize", "", "isReady", "katakanaToHiragana", "", "text", "mapTokens", "", "Lcom/jworks/kanjilens/domain/models/JapaneseToken;", "originalText", "tokens", "Lcom/atilika/kuromoji/ipadic/Token;", "tokenize", "Companion", "app_debug"})
public final class KuromojiTokenizer {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "KuromojiTokenizer";
    @org.jetbrains.annotations.Nullable()
    private com.atilika.kuromoji.ipadic.Tokenizer tokenizer;
    private boolean initialized = false;
    @org.jetbrains.annotations.NotNull()
    public static final com.jworks.kanjilens.data.nlp.KuromojiTokenizer.Companion Companion = null;
    
    @javax.inject.Inject()
    public KuromojiTokenizer() {
        super();
    }
    
    public final boolean isReady() {
        return false;
    }
    
    public final void initialize() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.jworks.kanjilens.domain.models.JapaneseToken> tokenize(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
        return null;
    }
    
    private final java.util.List<com.jworks.kanjilens.domain.models.JapaneseToken> mapTokens(java.lang.String originalText, java.util.List<? extends com.atilika.kuromoji.ipadic.Token> tokens) {
        return null;
    }
    
    private final java.lang.String katakanaToHiragana(java.lang.String text) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/jworks/kanjilens/data/nlp/KuromojiTokenizer$Companion;", "", "()V", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
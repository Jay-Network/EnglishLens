package com.jworks.kanjilens.data.local;

import androidx.room.Dao;
import androidx.room.Query;
import com.jworks.kanjilens.data.local.entities.FuriganaEntry;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\bg\u0018\u00002\u00020\u0001J\"\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010\b\u001a\u0004\u0018\u00010\u00042\u0006\u0010\t\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcom/jworks/kanjilens/data/local/JMDictDao;", "", "batchGetFurigana", "", "Lcom/jworks/kanjilens/data/local/entities/FuriganaEntry;", "words", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getFurigana", "word", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface JMDictDao {
    
    @androidx.room.Query(value = "SELECT * FROM furigana WHERE word = :word LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getFurigana(@org.jetbrains.annotations.NotNull()
    java.lang.String word, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.jworks.kanjilens.data.local.entities.FuriganaEntry> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM furigana WHERE word IN (:words)")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object batchGetFurigana(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> words, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.jworks.kanjilens.data.local.entities.FuriganaEntry>> $completion);
}
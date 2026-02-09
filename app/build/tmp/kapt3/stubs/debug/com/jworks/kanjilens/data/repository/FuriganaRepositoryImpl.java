package com.jworks.kanjilens.data.repository;

import com.jworks.kanjilens.data.local.JMDictDao;
import com.jworks.kanjilens.data.remote.FuriganaRequest;
import com.jworks.kanjilens.data.remote.KuroshiroApi;
import com.jworks.kanjilens.domain.models.FuriganaResult;
import com.jworks.kanjilens.domain.repository.FuriganaRepository;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J6\u0010\u000b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\r0\f2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\t0\u000fH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0010\u0010\u0011J\b\u0010\u0012\u001a\u00020\u0013H\u0016J$\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\n0\f2\u0006\u0010\u0015\u001a\u00020\tH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0018"}, d2 = {"Lcom/jworks/kanjilens/data/repository/FuriganaRepositoryImpl;", "Lcom/jworks/kanjilens/domain/repository/FuriganaRepository;", "jmDictDao", "Lcom/jworks/kanjilens/data/local/JMDictDao;", "kuroshiroApi", "Lcom/jworks/kanjilens/data/remote/KuroshiroApi;", "(Lcom/jworks/kanjilens/data/local/JMDictDao;Lcom/jworks/kanjilens/data/remote/KuroshiroApi;)V", "memoryCache", "Ljava/util/concurrent/ConcurrentHashMap;", "", "Lcom/jworks/kanjilens/domain/models/FuriganaResult;", "batchGetFurigana", "Lkotlin/Result;", "", "texts", "", "batchGetFurigana-gIAlu-s", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearCache", "", "getFurigana", "text", "getFurigana-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class FuriganaRepositoryImpl implements com.jworks.kanjilens.domain.repository.FuriganaRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.jworks.kanjilens.data.local.JMDictDao jmDictDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.jworks.kanjilens.data.remote.KuroshiroApi kuroshiroApi = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.ConcurrentHashMap<java.lang.String, com.jworks.kanjilens.domain.models.FuriganaResult> memoryCache = null;
    
    @javax.inject.Inject()
    public FuriganaRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.data.local.JMDictDao jmDictDao, @org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.data.remote.KuroshiroApi kuroshiroApi) {
        super();
    }
    
    @java.lang.Override()
    public void clearCache() {
    }
}
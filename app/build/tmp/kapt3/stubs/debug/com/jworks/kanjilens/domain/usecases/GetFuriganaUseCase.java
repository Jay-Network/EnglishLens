package com.jworks.kanjilens.domain.usecases;

import com.jworks.kanjilens.domain.models.FuriganaResult;
import com.jworks.kanjilens.domain.repository.FuriganaRepository;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010$\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J$\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\tH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\n\u0010\u000bJ6\u0010\f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00070\r0\u00062\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\t0\u000fH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0012"}, d2 = {"Lcom/jworks/kanjilens/domain/usecases/GetFuriganaUseCase;", "", "repository", "Lcom/jworks/kanjilens/domain/repository/FuriganaRepository;", "(Lcom/jworks/kanjilens/domain/repository/FuriganaRepository;)V", "execute", "Lkotlin/Result;", "Lcom/jworks/kanjilens/domain/models/FuriganaResult;", "text", "", "execute-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "executeBatch", "", "texts", "", "executeBatch-gIAlu-s", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class GetFuriganaUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.jworks.kanjilens.domain.repository.FuriganaRepository repository = null;
    
    @javax.inject.Inject()
    public GetFuriganaUseCase(@org.jetbrains.annotations.NotNull()
    com.jworks.kanjilens.domain.repository.FuriganaRepository repository) {
        super();
    }
}
package com.jworks.kanjilens.di;

import com.jworks.kanjilens.data.local.JMDictDao;
import com.jworks.kanjilens.data.local.JMDictDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class DatabaseModule_ProvideJMDictDaoFactory implements Factory<JMDictDao> {
  private final Provider<JMDictDatabase> databaseProvider;

  public DatabaseModule_ProvideJMDictDaoFactory(Provider<JMDictDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public JMDictDao get() {
    return provideJMDictDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideJMDictDaoFactory create(
      Provider<JMDictDatabase> databaseProvider) {
    return new DatabaseModule_ProvideJMDictDaoFactory(databaseProvider);
  }

  public static JMDictDao provideJMDictDao(JMDictDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideJMDictDao(database));
  }
}

package com.jworks.englishlens.di

import android.content.Context
import androidx.room.Room
import com.jworks.englishlens.data.local.WordNetDatabase
import com.jworks.englishlens.data.local.WordNetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WordNetDatabase {
        return Room.databaseBuilder(
            context,
            WordNetDatabase::class.java,
            "wordnet.db"
        ).createFromAsset("wordnet.db").build()
    }

    @Provides
    fun provideWordNetDao(database: WordNetDatabase): WordNetDao {
        return database.wordNetDao()
    }
}

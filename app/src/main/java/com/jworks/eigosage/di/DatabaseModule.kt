package com.jworks.eigosage.di

import android.content.Context
import androidx.room.Room
import com.jworks.eigosage.data.local.BookmarkDao
import com.jworks.eigosage.data.local.ChatDao
import com.jworks.eigosage.data.local.HistoryDao
import com.jworks.eigosage.data.local.SrsDao
import com.jworks.eigosage.data.local.UserDatabase
import com.jworks.eigosage.data.local.WordNetDatabase
import com.jworks.eigosage.data.local.WordNetDao
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
        ).createFromAsset("wordnet.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideWordNetDao(database: WordNetDatabase): WordNetDao {
        return database.wordNetDao()
    }

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "eigosage_user.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideHistoryDao(database: UserDatabase): HistoryDao {
        return database.historyDao()
    }

    @Provides
    fun provideBookmarkDao(database: UserDatabase): BookmarkDao {
        return database.bookmarkDao()
    }

    @Provides
    fun provideSrsDao(database: UserDatabase): SrsDao {
        return database.srsDao()
    }

    @Provides
    fun provideChatDao(database: UserDatabase): ChatDao {
        return database.chatDao()
    }
}

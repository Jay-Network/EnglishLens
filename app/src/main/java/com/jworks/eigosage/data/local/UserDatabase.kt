package com.jworks.eigosage.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jworks.eigosage.data.local.entities.BookmarkedWordEntity
import com.jworks.eigosage.data.local.entities.ChatMessageEntity
import com.jworks.eigosage.data.local.entities.ChatSessionEntity
import com.jworks.eigosage.data.local.entities.LookupHistoryEntity
import com.jworks.eigosage.data.local.entities.SrsCardEntity
import com.jworks.eigosage.data.local.entities.StudySessionEntity

@Database(
    entities = [
        LookupHistoryEntity::class,
        BookmarkedWordEntity::class,
        SrsCardEntity::class,
        StudySessionEntity::class,
        ChatSessionEntity::class,
        ChatMessageEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun srsDao(): SrsDao
    abstract fun chatDao(): ChatDao
}

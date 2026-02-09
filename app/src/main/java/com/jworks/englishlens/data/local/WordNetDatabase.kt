package com.jworks.englishlens.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jworks.englishlens.data.local.entities.WordEntry
import com.jworks.englishlens.data.local.entities.DefinitionEntry

@Database(
    entities = [WordEntry::class, DefinitionEntry::class],
    version = 1,
    exportSchema = false
)
abstract class WordNetDatabase : RoomDatabase() {
    abstract fun wordNetDao(): WordNetDao
}

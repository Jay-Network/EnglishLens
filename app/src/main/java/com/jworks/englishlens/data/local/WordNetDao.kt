package com.jworks.englishlens.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.jworks.englishlens.data.local.entities.WordEntry
import com.jworks.englishlens.data.local.entities.DefinitionEntry
import com.jworks.englishlens.data.local.entities.WordWithDefinitions

@Dao
interface WordNetDao {

    @Query("SELECT * FROM words WHERE word = :word LIMIT 1")
    suspend fun getWord(word: String): WordEntry?

    @Query("SELECT * FROM words WHERE word IN (:words)")
    suspend fun batchGetWords(words: List<String>): List<WordEntry>

    @Query("""
        SELECT d.* FROM definitions d
        JOIN words w ON d.word_id = w.word_id
        WHERE w.word = :word
        ORDER BY d.def_id
    """)
    suspend fun getDefinitions(word: String): List<DefinitionEntry>

    @Transaction
    @Query("SELECT * FROM words WHERE word = :word LIMIT 1")
    suspend fun lookupWithDefinitions(word: String): WordWithDefinitions?

    @Query("SELECT * FROM words WHERE word LIKE :prefix || '%' ORDER BY frequency ASC LIMIT :limit")
    suspend fun searchByPrefix(prefix: String, limit: Int = 20): List<WordEntry>

    @Query("SELECT * FROM words ORDER BY frequency ASC LIMIT :limit")
    suspend fun getTopFrequentWords(limit: Int = 100): List<WordEntry>
}

package com.jworks.eigosage.domain.nlp

import com.jworks.eigosage.data.local.WordNetDao
import com.jworks.eigosage.data.local.entities.DefinitionEntry
import com.jworks.eigosage.data.local.entities.WordEntry
import com.jworks.eigosage.data.local.entities.WordWithDefinitions
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EnglishLemmatizerTest {

    private lateinit var lemmatizer: EnglishLemmatizer

    /** Fake DAO that recognizes a fixed vocabulary of base words. */
    private val knownWords = mutableSetOf(
        // Common base verbs
        "walk", "run", "make", "take", "like", "stop", "carry", "die", "lie",
        "box", "watch", "plate", "cat", "dog", "big", "nice", "quick", "happy",
        "write", "drive", "swim", "eat", "go", "see", "have", "do", "be",
        "think", "give", "find", "tell", "feel", "leave", "keep", "hold",
        "bring", "begin", "stand", "lose", "pay", "meet", "send", "build",
        "fall", "cut", "read", "lead", "understand", "speak", "break", "set",
        "sit", "spend", "grow", "draw", "buy", "rise", "wear", "choose",
        "catch", "teach", "sing", "sleep", "wake", "fly", "throw", "bite",
        "hide", "shake", "blow", "forget", "freeze", "tear", "hang", "dig",
        "bind", "strike", "wind", "light", "mean", "shoot", "deal", "spin",
        // Common base nouns
        "man", "woman", "child", "tooth", "foot", "goose", "mouse", "person",
        "story", "wolf", "knife", "life", "wife", "self", "shelf", "loaf",
        "leaf", "calf", "scarf", "thief", "elf", "sheep", "deer", "fish",
        // Common adjectives
        "good", "bad", "far", "old", "little", "much",
        // Words for suffix-stripping tests
        "basic", "final"
    )

    private val fakeDao = object : WordNetDao {
        override suspend fun getWord(word: String): WordEntry? {
            return if (word in knownWords) {
                WordEntry(wordId = 1, word = word, lemma = word, frequency = null, phonetic = null, cefrLevel = null)
            } else null
        }
        override suspend fun batchGetWords(words: List<String>): List<WordEntry> = emptyList()
        override suspend fun getDefinitions(word: String): List<DefinitionEntry> = emptyList()
        override suspend fun lookupWithDefinitions(word: String): WordWithDefinitions? = null
        override suspend fun searchByPrefix(prefix: String, limit: Int): List<WordEntry> = emptyList()
        override suspend fun getTopFrequentWords(limit: Int): List<WordEntry> = emptyList()
        override suspend fun getWordMetadataBatch(words: List<String>): List<WordEntry> = emptyList()
        override suspend fun getFirstMeaning(word: String): String? = null
    }

    @Before
    fun setup() {
        lemmatizer = EnglishLemmatizer(fakeDao)
    }

    // -- Base form passthrough --

    @Test
    fun `base form word returns unchanged`() = runTest {
        assertEquals("walk", lemmatizer.lemmatize("walk"))
        assertEquals("cat", lemmatizer.lemmatize("cat"))
    }

    @Test
    fun `empty string returns unchanged`() = runTest {
        assertEquals("", lemmatizer.lemmatize(""))
    }

    @Test
    fun `uppercase is lowercased`() = runTest {
        assertEquals("walk", lemmatizer.lemmatize("WALK"))
        assertEquals("cat", lemmatizer.lemmatize("Cat"))
    }

    // -- Irregular verbs --

    @Test
    fun `irregular verb - be forms`() = runTest {
        assertEquals("be", lemmatizer.lemmatize("was"))
        assertEquals("be", lemmatizer.lemmatize("were"))
        assertEquals("be", lemmatizer.lemmatize("been"))
        assertEquals("be", lemmatizer.lemmatize("am"))
        assertEquals("be", lemmatizer.lemmatize("is"))
        assertEquals("be", lemmatizer.lemmatize("are"))
    }

    @Test
    fun `irregular verb - have forms`() = runTest {
        assertEquals("have", lemmatizer.lemmatize("had"))
        assertEquals("have", lemmatizer.lemmatize("has"))
    }

    @Test
    fun `irregular verb - go forms`() = runTest {
        assertEquals("go", lemmatizer.lemmatize("went"))
        assertEquals("go", lemmatizer.lemmatize("gone"))
    }

    @Test
    fun `irregular verb - past participles`() = runTest {
        assertEquals("write", lemmatizer.lemmatize("written"))
        assertEquals("speak", lemmatizer.lemmatize("spoken"))
        assertEquals("break", lemmatizer.lemmatize("broken"))
        assertEquals("give", lemmatizer.lemmatize("given"))
        assertEquals("take", lemmatizer.lemmatize("taken"))
        assertEquals("see", lemmatizer.lemmatize("seen"))
        assertEquals("eat", lemmatizer.lemmatize("eaten"))
    }

    @Test
    fun `irregular verb - past tense`() = runTest {
        assertEquals("think", lemmatizer.lemmatize("thought"))
        assertEquals("bring", lemmatizer.lemmatize("brought"))
        assertEquals("catch", lemmatizer.lemmatize("caught"))
        assertEquals("teach", lemmatizer.lemmatize("taught"))
        assertEquals("buy", lemmatizer.lemmatize("bought"))
        assertEquals("find", lemmatizer.lemmatize("found"))
        assertEquals("feel", lemmatizer.lemmatize("felt"))
        assertEquals("leave", lemmatizer.lemmatize("left"))
        assertEquals("keep", lemmatizer.lemmatize("kept"))
        assertEquals("send", lemmatizer.lemmatize("sent"))
        assertEquals("build", lemmatizer.lemmatize("built"))
        assertEquals("lose", lemmatizer.lemmatize("lost"))
        assertEquals("pay", lemmatizer.lemmatize("paid"))
        assertEquals("meet", lemmatizer.lemmatize("met"))
        assertEquals("spend", lemmatizer.lemmatize("spent"))
        assertEquals("deal", lemmatizer.lemmatize("dealt"))
        assertEquals("mean", lemmatizer.lemmatize("meant"))
        assertEquals("sleep", lemmatizer.lemmatize("slept"))
    }

    @Test
    fun `irregular verb - vowel change past`() = runTest {
        assertEquals("swim", lemmatizer.lemmatize("swam"))
        assertEquals("sing", lemmatizer.lemmatize("sang"))
        assertEquals("run", lemmatizer.lemmatize("ran"))
        assertEquals("sit", lemmatizer.lemmatize("sat"))
        assertEquals("begin", lemmatizer.lemmatize("began"))
        assertEquals("draw", lemmatizer.lemmatize("drew"))
        assertEquals("grow", lemmatizer.lemmatize("grew"))
        assertEquals("fly", lemmatizer.lemmatize("flew"))
        assertEquals("blow", lemmatizer.lemmatize("blew"))
        assertEquals("fall", lemmatizer.lemmatize("fell"))
        assertEquals("rise", lemmatizer.lemmatize("rose"))
        assertEquals("drive", lemmatizer.lemmatize("drove"))
        assertEquals("wear", lemmatizer.lemmatize("wore"))
        assertEquals("choose", lemmatizer.lemmatize("chose"))
        assertEquals("freeze", lemmatizer.lemmatize("froze"))
        assertEquals("shake", lemmatizer.lemmatize("shook"))
        assertEquals("tear", lemmatizer.lemmatize("tore"))
        assertEquals("hide", lemmatizer.lemmatize("hid"))
        assertEquals("bite", lemmatizer.lemmatize("bit"))
        assertEquals("dig", lemmatizer.lemmatize("dug"))
        assertEquals("hang", lemmatizer.lemmatize("hung"))
        assertEquals("spin", lemmatizer.lemmatize("spun"))
        assertEquals("strike", lemmatizer.lemmatize("struck"))
        assertEquals("hold", lemmatizer.lemmatize("held"))
    }

    // -- Irregular nouns --

    @Test
    fun `irregular noun plurals`() = runTest {
        assertEquals("man", lemmatizer.lemmatize("men"))
        assertEquals("woman", lemmatizer.lemmatize("women"))
        assertEquals("child", lemmatizer.lemmatize("children"))
        assertEquals("tooth", lemmatizer.lemmatize("teeth"))
        assertEquals("foot", lemmatizer.lemmatize("feet"))
        assertEquals("goose", lemmatizer.lemmatize("geese"))
        assertEquals("mouse", lemmatizer.lemmatize("mice"))
        assertEquals("person", lemmatizer.lemmatize("people"))
    }

    @Test
    fun `irregular noun - uncountable`() = runTest {
        assertEquals("sheep", lemmatizer.lemmatize("sheep"))
        assertEquals("deer", lemmatizer.lemmatize("deer"))
        assertEquals("fish", lemmatizer.lemmatize("fish"))
    }

    // -- Irregular adjectives --

    @Test
    fun `irregular adjective comparatives`() = runTest {
        assertEquals("good", lemmatizer.lemmatize("better"))
        assertEquals("good", lemmatizer.lemmatize("best"))
        assertEquals("bad", lemmatizer.lemmatize("worse"))
        assertEquals("bad", lemmatizer.lemmatize("worst"))
        assertEquals("far", lemmatizer.lemmatize("farther"))
        assertEquals("far", lemmatizer.lemmatize("farthest"))
    }

    // -- Suffix stripping: -s --

    @Test
    fun `regular plural -s`() = runTest {
        assertEquals("cat", lemmatizer.lemmatize("cats"))
        assertEquals("dog", lemmatizer.lemmatize("dogs"))
    }

    // -- Suffix stripping: -es --

    @Test
    fun `plural -es`() = runTest {
        assertEquals("box", lemmatizer.lemmatize("boxes"))
        assertEquals("watch", lemmatizer.lemmatize("watches"))
    }

    // -- Suffix stripping: -ies --

    @Test
    fun `plural -ies to -y`() = runTest {
        assertEquals("story", lemmatizer.lemmatize("stories"))
    }

    // -- Suffix stripping: -ing --

    @Test
    fun `gerund -ing simple drop`() = runTest {
        assertEquals("walk", lemmatizer.lemmatize("walking"))
    }

    @Test
    fun `gerund -ing add e`() = runTest {
        assertEquals("make", lemmatizer.lemmatize("making"))
        assertEquals("like", lemmatizer.lemmatize("liking"))
    }

    @Test
    fun `gerund -ing doubled consonant`() = runTest {
        assertEquals("run", lemmatizer.lemmatize("running"))
        assertEquals("stop", lemmatizer.lemmatize("stopping"))
        assertEquals("swim", lemmatizer.lemmatize("swimming"))
    }

    @Test
    fun `gerund -ying to -ie`() = runTest {
        assertEquals("die", lemmatizer.lemmatize("dying"))
        assertEquals("lie", lemmatizer.lemmatize("lying"))
    }

    // -- Suffix stripping: -ed --

    @Test
    fun `past tense -ed simple drop`() = runTest {
        assertEquals("walk", lemmatizer.lemmatize("walked"))
    }

    @Test
    fun `past tense -ed drop d only`() = runTest {
        assertEquals("like", lemmatizer.lemmatize("liked"))
    }

    @Test
    fun `past tense -ed doubled consonant`() = runTest {
        assertEquals("stop", lemmatizer.lemmatize("stopped"))
    }

    @Test
    fun `past tense -ied to -y`() = runTest {
        assertEquals("carry", lemmatizer.lemmatize("carried"))
    }

    // -- Suffix stripping: -er, -est --

    @Test
    fun `comparative -er`() = runTest {
        assertEquals("big", lemmatizer.lemmatize("bigger"))
        assertEquals("nice", lemmatizer.lemmatize("nicer"))
    }

    @Test
    fun `superlative -est`() = runTest {
        assertEquals("big", lemmatizer.lemmatize("biggest"))
        assertEquals("nice", lemmatizer.lemmatize("nicest"))
    }

    // -- Suffix stripping: -ly --

    @Test
    fun `adverb -ly`() = runTest {
        assertEquals("quick", lemmatizer.lemmatize("quickly"))
    }

    @Test
    fun `adverb -ily to -y`() = runTest {
        assertEquals("happy", lemmatizer.lemmatize("happily"))
    }

    // -- Unknown word passthrough --

    @Test
    fun `unknown word returns unchanged`() = runTest {
        assertEquals("xyzzy", lemmatizer.lemmatize("xyzzy"))
    }
}

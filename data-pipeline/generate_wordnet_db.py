#!/usr/bin/env python3
"""
generate_wordnet_db.py - Generate WordNet SQLite database for EnglishLens

Creates an offline English dictionary database from WordNet 3.1 (via NLTK)
with frequency rankings derived from NLTK's word frequency data.

Output: ../app/src/main/assets/wordnet.db

Usage:
    python3 generate_wordnet_db.py [--output PATH] [--coca-file PATH]

Requirements:
    pip install nltk pandas
"""

import argparse
import logging
import os
import sqlite3
import sys
import time
from pathlib import Path

import nltk
import pandas as pd

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(message)s",
)
log = logging.getLogger(__name__)

# WordNet POS tag mapping
POS_MAP = {
    "n": "noun",
    "v": "verb",
    "a": "adjective",
    "r": "adverb",
    "s": "adjective",  # satellite adjective -> adjective
}

# Default output path
DEFAULT_OUTPUT = Path(__file__).parent.parent / "app" / "src" / "main" / "assets" / "wordnet.db"


def ensure_nltk_data():
    """Download required NLTK data packages."""
    packages = ["wordnet", "omw-1.4", "brown", "reuters", "gutenberg", "webtext", "inaugural"]
    for pkg in packages:
        try:
            nltk.data.find(f"corpora/{pkg}")
            log.info(f"NLTK package '{pkg}' already available")
        except LookupError:
            log.info(f"Downloading NLTK package '{pkg}'...")
            nltk.download(pkg, quiet=True)


def build_frequency_rankings():
    """
    Build word frequency rankings from multiple NLTK corpora.

    Since COCA data requires a license, we approximate frequency using
    freely available NLTK corpora (Brown, Reuters, Gutenberg, etc.)
    combined with WordNet polysemy count as a secondary signal.

    Returns dict mapping lowercase word -> frequency rank (1 = most common).
    """
    from nltk.corpus import brown, reuters, gutenberg, webtext, inaugural

    log.info("Building frequency rankings from NLTK corpora...")

    word_counts = {}

    corpora = [
        ("Brown", brown.words),
        ("Reuters", reuters.words),
        ("Gutenberg", gutenberg.words),
        ("Webtext", webtext.words),
        ("Inaugural", inaugural.words),
    ]

    for name, words_fn in corpora:
        log.info(f"  Processing {name} corpus...")
        for word in words_fn():
            w = word.lower().strip()
            if w.isalpha() and len(w) > 1:
                word_counts[w] = word_counts.get(w, 0) + 1

    log.info(f"  Total unique words from corpora: {len(word_counts):,}")

    # Sort by frequency (descending) and assign ranks
    sorted_words = sorted(word_counts.items(), key=lambda x: -x[1])
    frequency_rank = {}
    for rank, (word, _count) in enumerate(sorted_words, start=1):
        frequency_rank[word] = rank

    log.info(f"  Ranked {len(frequency_rank):,} words by frequency")
    return frequency_rank


def load_coca_file(coca_path):
    """
    Load COCA frequency data from a file if provided.

    Expected format: one word per line, ranked by frequency (most frequent first),
    or TSV with columns: rank, word, frequency.
    """
    log.info(f"Loading COCA frequency data from {coca_path}...")
    frequency_rank = {}

    with open(coca_path, "r", encoding="utf-8") as f:
        for rank, line in enumerate(f, start=1):
            parts = line.strip().split("\t")
            if len(parts) >= 2:
                # TSV format: rank/freq word ...
                word = parts[1].lower().strip()
            else:
                word = parts[0].lower().strip()

            if word and word.isalpha():
                frequency_rank[word] = rank

    log.info(f"  Loaded {len(frequency_rank):,} COCA entries")
    return frequency_rank


def generate_database(output_path, coca_path=None):
    """Generate the WordNet SQLite database."""
    from nltk.corpus import wordnet as wn

    start_time = time.time()

    # Step 1: Build frequency rankings
    if coca_path and os.path.exists(coca_path):
        frequency_rank = load_coca_file(coca_path)
    else:
        frequency_rank = build_frequency_rankings()

    # Step 2: Set up SQLite database
    output_path = Path(output_path)
    output_path.parent.mkdir(parents=True, exist_ok=True)

    if output_path.exists():
        output_path.unlink()
        log.info(f"Removed existing database at {output_path}")

    log.info(f"Creating database at {output_path}")
    conn = sqlite3.connect(str(output_path))
    conn.execute("PRAGMA journal_mode=WAL")
    conn.execute("PRAGMA synchronous=NORMAL")
    cursor = conn.cursor()

    # Step 3: Create schema
    # Room requires android_metadata and room_master_table for createFromAsset
    cursor.executescript("""
        CREATE TABLE IF NOT EXISTS android_metadata (
            locale TEXT DEFAULT 'en_US'
        );
        INSERT INTO android_metadata VALUES ('en_US');

        CREATE TABLE IF NOT EXISTS room_master_table (
            id INTEGER PRIMARY KEY,
            identity_hash TEXT
        );

        CREATE TABLE IF NOT EXISTS words (
            word_id INTEGER PRIMARY KEY,
            word TEXT NOT NULL,
            lemma TEXT NOT NULL,
            frequency INTEGER DEFAULT 999999
        );

        CREATE TABLE IF NOT EXISTS definitions (
            def_id INTEGER PRIMARY KEY AUTOINCREMENT,
            word_id INTEGER NOT NULL,
            pos TEXT,
            meaning TEXT NOT NULL,
            example TEXT,
            synonyms TEXT,
            antonyms TEXT,
            FOREIGN KEY(word_id) REFERENCES words(word_id)
        );

        CREATE TABLE IF NOT EXISTS etymology (
            word_id INTEGER PRIMARY KEY,
            origin TEXT,
            root TEXT,
            history TEXT,
            FOREIGN KEY(word_id) REFERENCES words(word_id)
        );
    """)

    # Step 4: Extract all words and definitions from WordNet
    log.info("Extracting WordNet data...")

    # Collect all unique lemma names
    all_lemmas = set()
    for synset in wn.all_synsets():
        for lemma in synset.lemmas():
            name = lemma.name().replace("_", " ").lower()
            if any(c.isalpha() for c in name):
                all_lemmas.add(name)

    log.info(f"  Found {len(all_lemmas):,} unique lemma forms")

    # Build word -> synsets mapping
    word_synsets = {}
    for synset in wn.all_synsets():
        for lemma in synset.lemmas():
            name = lemma.name().replace("_", " ").lower()
            if any(c.isalpha() for c in name):
                if name not in word_synsets:
                    word_synsets[name] = []
                word_synsets[name].append((synset, lemma))

    # Step 5: Insert words and definitions
    log.info("Inserting words into database...")
    word_id_map = {}
    words_batch = []
    defs_batch = []
    word_id = 0
    def_id = 0

    for word in sorted(all_lemmas):
        word_id += 1
        # Lemma: base form (first synset's first lemma as approximation)
        lemma = word  # For English, word form is often the lemma itself
        freq = frequency_rank.get(word.split()[0] if " " in word else word, 999999)

        words_batch.append((word_id, word, lemma, freq))
        word_id_map[word] = word_id

        # Process synsets for this word
        seen_meanings = set()
        synsets_for_word = word_synsets.get(word, [])

        for synset, lemma_obj in synsets_for_word:
            meaning = synset.definition()
            if meaning in seen_meanings:
                continue
            seen_meanings.add(meaning)

            def_id += 1
            pos = POS_MAP.get(synset.pos(), synset.pos())
            examples = synset.examples()
            example = "; ".join(examples) if examples else None

            # Collect synonyms (other lemma names in the same synset)
            syns = [
                l.name().replace("_", " ")
                for l in synset.lemmas()
                if l.name().replace("_", " ").lower() != word
            ]
            synonyms = ", ".join(syns[:10]) if syns else None

            # Collect antonyms
            ants = []
            for l in synset.lemmas():
                if l.name().replace("_", " ").lower() == word:
                    for ant in l.antonyms():
                        ants.append(ant.name().replace("_", " "))
            antonyms = ", ".join(ants) if ants else None

            defs_batch.append((def_id, word_id_map[word], pos, meaning, example, synonyms, antonyms))

        # Batch insert periodically
        if len(words_batch) >= 10000:
            cursor.executemany(
                "INSERT INTO words (word_id, word, lemma, frequency) VALUES (?, ?, ?, ?)",
                words_batch,
            )
            cursor.executemany(
                "INSERT INTO definitions (def_id, word_id, pos, meaning, example, synonyms, antonyms) VALUES (?, ?, ?, ?, ?, ?, ?)",
                defs_batch,
            )
            conn.commit()
            log.info(f"  Inserted {word_id:,} words, {def_id:,} definitions...")
            words_batch = []
            defs_batch = []

    # Insert remaining batch
    if words_batch:
        cursor.executemany(
            "INSERT INTO words (word_id, word, lemma, frequency) VALUES (?, ?, ?, ?)",
            words_batch,
        )
    if defs_batch:
        cursor.executemany(
            "INSERT INTO definitions (def_id, word_id, pos, meaning, example, synonyms, antonyms) VALUES (?, ?, ?, ?, ?, ?, ?)",
            defs_batch,
        )
    conn.commit()

    log.info(f"  Total: {word_id:,} words, {def_id:,} definitions")

    # Step 6: Create indexes for fast lookup
    log.info("Creating indexes...")
    cursor.executescript("""
        CREATE UNIQUE INDEX IF NOT EXISTS idx_words_word ON words(word);
        CREATE INDEX IF NOT EXISTS idx_words_lemma ON words(lemma);
        CREATE INDEX IF NOT EXISTS idx_words_frequency ON words(frequency);
        CREATE INDEX IF NOT EXISTS idx_definitions_word_id ON definitions(word_id);
    """)
    conn.commit()

    # Step 7: Generate Room identity hash
    # This hash must match what Room generates from the Entity annotations.
    # We'll compute it based on the schema.
    identity_hash = compute_room_identity_hash()
    cursor.execute(
        "INSERT OR REPLACE INTO room_master_table (id, identity_hash) VALUES (42, ?)",
        (identity_hash,),
    )
    conn.commit()

    # Step 8: Optimize database
    log.info("Optimizing database (VACUUM + ANALYZE)...")
    conn.execute("VACUUM")
    conn.execute("ANALYZE")
    conn.commit()
    conn.close()

    elapsed = time.time() - start_time
    file_size_mb = output_path.stat().st_size / (1024 * 1024)

    log.info(f"Database generated successfully!")
    log.info(f"  Path: {output_path}")
    log.info(f"  Size: {file_size_mb:.1f} MB")
    log.info(f"  Words: {word_id:,}")
    log.info(f"  Definitions: {def_id:,}")
    log.info(f"  Time: {elapsed:.1f}s")

    return output_path


def compute_room_identity_hash():
    """
    Compute a Room-compatible identity hash for the schema.

    Note: This is a placeholder. The actual hash will need to match
    what Room generates from the Kotlin Entity annotations when we
    create the Android-side code. For now we use a fixed hash that
    we'll update when we define the Room entities.
    """
    import hashlib

    schema_str = (
        "words(word_id:INTEGER,word:TEXT,lemma:TEXT,frequency:INTEGER)|"
        "definitions(def_id:INTEGER,word_id:INTEGER,pos:TEXT,meaning:TEXT,example:TEXT,synonyms:TEXT,antonyms:TEXT)|"
        "etymology(word_id:INTEGER,origin:TEXT,root:TEXT,history:TEXT)"
    )
    return hashlib.md5(schema_str.encode()).hexdigest()


def verify_database(db_path):
    """Run verification queries on the generated database."""
    log.info("\n" + "=" * 60)
    log.info("VERIFICATION REPORT")
    log.info("=" * 60)

    conn = sqlite3.connect(str(db_path))
    cursor = conn.cursor()

    # Total counts
    cursor.execute("SELECT COUNT(*) FROM words")
    total_words = cursor.fetchone()[0]

    cursor.execute("SELECT COUNT(*) FROM definitions")
    total_defs = cursor.fetchone()[0]

    cursor.execute("SELECT COUNT(DISTINCT word_id) FROM definitions")
    words_with_defs = cursor.fetchone()[0]

    log.info(f"\n  Total words:            {total_words:,}")
    log.info(f"  Total definitions:      {total_defs:,}")
    log.info(f"  Words with definitions: {words_with_defs:,}")
    log.info(f"  Avg defs per word:      {total_defs / max(words_with_defs, 1):.1f}")

    # POS distribution
    log.info("\n  POS Distribution:")
    cursor.execute("SELECT pos, COUNT(*) as cnt FROM definitions GROUP BY pos ORDER BY cnt DESC")
    for pos, cnt in cursor.fetchall():
        log.info(f"    {pos:12s}: {cnt:,}")

    # Frequency coverage
    cursor.execute("SELECT COUNT(*) FROM words WHERE frequency <= 10000")
    top10k = cursor.fetchone()[0]
    cursor.execute("SELECT COUNT(*) FROM words WHERE frequency <= 60000")
    top60k = cursor.fetchone()[0]
    cursor.execute("SELECT COUNT(*) FROM words WHERE frequency < 999999")
    has_freq = cursor.fetchone()[0]

    log.info(f"\n  Frequency Coverage:")
    log.info(f"    Words with frequency data: {has_freq:,} ({100 * has_freq / max(total_words, 1):.1f}%)")
    log.info(f"    In top 10,000:             {top10k:,}")
    log.info(f"    In top 60,000:             {top60k:,}")

    # Sample lookups with timing
    test_words = ["hello", "run", "beautiful", "algorithm", "serendipity",
                  "the", "is", "computer", "love", "happy"]
    log.info(f"\n  Sample Lookups:")

    for word in test_words:
        t0 = time.time()
        cursor.execute("""
            SELECT w.word, w.frequency, d.pos, d.meaning, d.synonyms
            FROM words w
            JOIN definitions d ON w.word_id = d.word_id
            WHERE w.word = ?
            LIMIT 3
        """, (word,))
        results = cursor.fetchall()
        elapsed_ms = (time.time() - t0) * 1000

        if results:
            log.info(f"\n    '{word}' (rank: {results[0][1]}, {elapsed_ms:.1f}ms):")
            for _, _, pos, meaning, syns in results:
                syn_str = f" [syn: {syns}]" if syns else ""
                log.info(f"      [{pos}] {meaning[:80]}{syn_str}")
        else:
            log.info(f"\n    '{word}': NOT FOUND ({elapsed_ms:.1f}ms)")

    # Database file size
    file_size_mb = Path(db_path).stat().st_size / (1024 * 1024)
    log.info(f"\n  Database size: {file_size_mb:.1f} MB")

    # Check if compression is needed
    if file_size_mb > 100:
        log.warning(f"  WARNING: Database exceeds 100MB - consider ZSTD compression")
    elif file_size_mb > 60:
        log.info(f"  NOTE: Database is over 60MB target but under 100MB - acceptable")
    else:
        log.info(f"  OK: Database within 60MB target")

    conn.close()
    log.info("\n" + "=" * 60)


def main():
    parser = argparse.ArgumentParser(description="Generate WordNet database for EnglishLens")
    parser.add_argument(
        "--output", "-o",
        type=str,
        default=str(DEFAULT_OUTPUT),
        help=f"Output path for SQLite database (default: {DEFAULT_OUTPUT})",
    )
    parser.add_argument(
        "--coca-file",
        type=str,
        default=None,
        help="Path to COCA frequency file (optional, uses NLTK corpora if not provided)",
    )
    parser.add_argument(
        "--verify-only",
        action="store_true",
        help="Only run verification on existing database",
    )
    args = parser.parse_args()

    if args.verify_only:
        if not Path(args.output).exists():
            log.error(f"Database not found at {args.output}")
            sys.exit(1)
        verify_database(args.output)
        return

    log.info("EnglishLens WordNet Database Generator")
    log.info("=" * 40)

    # Download NLTK data
    ensure_nltk_data()

    # Generate database
    db_path = generate_database(args.output, args.coca_file)

    # Verify
    verify_database(db_path)

    log.info("\nDone! Database ready for EnglishLens.")


if __name__ == "__main__":
    main()

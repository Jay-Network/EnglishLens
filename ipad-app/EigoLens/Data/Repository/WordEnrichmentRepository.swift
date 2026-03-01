import Foundation
import CoreGraphics

final class WordEnrichmentRepository {
    private let db: WordNetDatabase
    private let lemmatizer: EnglishLemmatizer
    private var cache: [String: WordMeta] = [:]
    private let cacheLimit = 500

    init(db: WordNetDatabase, lemmatizer: EnglishLemmatizer) {
        self.db = db
        self.lemmatizer = lemmatizer
    }

    func enrichWords(from ocrResult: OCRResult) -> [EnrichedWord] {
        // Collect unique words with their bounding boxes
        var wordBounds: [(text: String, box: CGRect)] = []
        for line in ocrResult.lines {
            for word in line.words {
                let cleaned = word.text.lowercased().trimmingCharacters(in: .punctuationCharacters)
                guard cleaned.count >= 2, cleaned.contains(where: \.isLetter) else { continue }
                wordBounds.append((cleaned, word.boundingBox))
            }
        }

        // Find uncached words
        let uniqueWords = Array(Set(wordBounds.map(\.text)))
        let uncached = uniqueWords.filter { cache[$0] == nil }

        // Batch query uncached words
        if !uncached.isEmpty {
            // Try direct lookup
            let metadata = db.batchGetMetadata(words: uncached)
            for (word, meta) in metadata {
                cache[word] = WordMeta(
                    ipa: meta.phonetic,
                    cefr: CefrLevel.from(meta.cefr),
                    briefDef: meta.briefDef
                )
            }

            // Try lemmatization for words not found
            let notFound = uncached.filter { metadata[$0] == nil }
            if !notFound.isEmpty {
                var lemmaWords: [String] = []
                var lemmaMap: [String: String] = [:] // lemma -> original
                for word in notFound {
                    let lemma = lemmatizer.lemmatize(word)
                    if lemma != word {
                        lemmaWords.append(lemma)
                        lemmaMap[lemma] = word
                    }
                }
                if !lemmaWords.isEmpty {
                    let lemmaMetadata = db.batchGetMetadata(words: lemmaWords)
                    for (lemma, meta) in lemmaMetadata {
                        if let original = lemmaMap[lemma] {
                            cache[original] = WordMeta(
                                ipa: meta.phonetic,
                                cefr: CefrLevel.from(meta.cefr),
                                briefDef: meta.briefDef
                            )
                        }
                    }
                }
            }

            evictIfNeeded()
        }

        // Build enriched words
        return wordBounds.compactMap { (text, box) in
            guard let meta = cache[text] else { return nil }
            return EnrichedWord(
                text: text,
                boundingBox: box,
                ipa: meta.ipa,
                cefr: meta.cefr,
                briefDefinition: meta.briefDef
            )
        }
    }

    private func evictIfNeeded() {
        if cache.count > cacheLimit {
            let excess = cache.count - cacheLimit / 2
            let keysToRemove = Array(cache.keys.prefix(excess))
            for key in keysToRemove {
                cache.removeValue(forKey: key)
            }
        }
    }
}

private struct WordMeta {
    let ipa: String?
    let cefr: CefrLevel?
    let briefDef: String?
}

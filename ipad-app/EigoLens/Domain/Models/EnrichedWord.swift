import Foundation
import CoreGraphics

struct EnrichedWord: Identifiable, Equatable {
    let id = UUID()
    let text: String
    let boundingBox: CGRect
    let ipa: String?
    let cefr: CefrLevel?
    let briefDefinition: String?

    static func == (lhs: EnrichedWord, rhs: EnrichedWord) -> Bool {
        lhs.text == rhs.text && lhs.boundingBox == rhs.boundingBox
    }
}

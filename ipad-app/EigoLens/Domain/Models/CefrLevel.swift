import SwiftUI

enum CefrLevel: String, CaseIterable, Equatable {
    case a1 = "A1"
    case a2 = "A2"
    case b1 = "B1"
    case b2 = "B2"
    case c1 = "C1"
    case c2 = "C2"

    var label: String {
        switch self {
        case .a1: return "Beginner"
        case .a2: return "Elementary"
        case .b1: return "Intermediate"
        case .b2: return "Upper-Int"
        case .c1: return "Advanced"
        case .c2: return "Proficiency"
        }
    }

    var ordinalIndex: Int {
        switch self {
        case .a1: return 0
        case .a2: return 1
        case .b1: return 2
        case .b2: return 3
        case .c1: return 4
        case .c2: return 5
        }
    }

    var color: Color {
        switch self {
        case .a1, .a2: return .clear
        case .b1: return Color(hex: 0xFFC107)   // amber
        case .b2: return Color(hex: 0xFF9800)   // orange
        case .c1: return Color(hex: 0xF44336)   // red
        case .c2: return Color(hex: 0xB71C1C)   // dark red
        }
    }

    static func from(_ string: String?) -> CefrLevel? {
        guard let str = string?.uppercased().trimmingCharacters(in: .whitespaces) else { return nil }
        return CefrLevel(rawValue: str)
    }
}

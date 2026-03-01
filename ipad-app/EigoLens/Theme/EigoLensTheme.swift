import SwiftUI

enum EigoLensTheme {
    // Brand colors — neutral to let the image be the hero
    static let primary = Color(hex: 0x1B6B93)          // Deep teal
    static let primaryDark = Color(hex: 0x145374)
    static let onPrimary = Color.white

    static let secondary = Color(hex: 0x468B97)        // Muted teal
    static let secondaryDark = Color(hex: 0x2D6A72)
    static let onSecondary = Color.white

    static let tertiary = Color(hex: 0xEF6262)         // Warm coral
    static let onTertiary = Color.white

    // Glass UI — near-black base, translucent surfaces
    static let background = Color(hex: 0x050508)
    static let surface = Color.white.opacity(0.06)
    static let surfaceVariant = Color.white.opacity(0.08)
    static let onSurface = Color(hex: 0xE8E8E8)
    static let onSurfaceVariant = Color(hex: 0xA0A0A0)
    static let outline = Color(hex: 0x4338CA).opacity(0.28) // Indigo-tinted border

    static let error = Color(hex: 0xEF4444)
    static let success = Color(hex: 0x22C55E)
    static let warning = Color(hex: 0xF59E0B)

    // Scope level colors (matching Android)
    static let scopeWord = Color(hex: 0x1B6B93)
    static let scopePhrase = Color(hex: 0x7B2D8E)
    static let scopeSentence = Color(hex: 0xC2571A)
    static let scopeParagraph = Color(hex: 0x2E7D32)
    static let scopeFullText = Color(hex: 0x37474F)

    // POS tag colors
    static let posNoun = Color(hex: 0x3B82F6)
    static let posVerb = Color(hex: 0xEF4444)
    static let posAdj = Color(hex: 0x22C55E)
    static let posAdv = Color(hex: 0xA855F7)

    // Typography
    static let headlineLarge = Font.system(.title, design: .default, weight: .bold)
    static let headlineMedium = Font.system(.title2, design: .default, weight: .semibold)
    static let titleLarge = Font.system(.title3, design: .default, weight: .semibold)
    static let titleMedium = Font.system(.headline)
    static let bodyLarge = Font.system(.body)
    static let bodyMedium = Font.system(.callout)
    static let bodySmall = Font.system(.footnote)
    static let labelMedium = Font.system(.caption, weight: .medium)
    static let labelSmall = Font.system(.caption2)

    // Spacing
    static let spacingXS: CGFloat = 4
    static let spacingS: CGFloat = 8
    static let spacingM: CGFloat = 16
    static let spacingL: CGFloat = 24
    static let spacingXL: CGFloat = 32

    // Corner radius
    static let radiusS: CGFloat = 8
    static let radiusM: CGFloat = 12
    static let radiusL: CGFloat = 20
    static let radiusXL: CGFloat = 28
}

// MARK: - Glass Card Modifier

struct GlassCardModifier: ViewModifier {
    var cornerRadius: CGFloat = EigoLensTheme.radiusM

    func body(content: Content) -> some View {
        content
            .background(
                LinearGradient(
                    colors: [Color.white.opacity(0.12), Color.white.opacity(0.05)],
                    startPoint: .topLeading,
                    endPoint: .bottomTrailing
                )
            )
            .overlay(
                RoundedRectangle(cornerRadius: cornerRadius)
                    .stroke(Color(hex: 0x4338CA).opacity(0.28), lineWidth: 1)
            )
            .clipShape(RoundedRectangle(cornerRadius: cornerRadius))
    }
}

extension View {
    func glassCard(cornerRadius: CGFloat = EigoLensTheme.radiusM) -> some View {
        modifier(GlassCardModifier(cornerRadius: cornerRadius))
    }
}

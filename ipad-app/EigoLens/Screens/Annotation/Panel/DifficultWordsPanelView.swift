import SwiftUI

struct DifficultWordsPanelView: View {
    let words: [EnrichedWord]
    let threshold: CefrLevel
    var onWordTapped: (String) -> Void
    var onDismiss: () -> Void

    var body: some View {
        VStack(spacing: 0) {
            // Header
            HStack {
                Text("Difficult Words")
                    .font(EigoLensTheme.titleMedium)
                    .foregroundStyle(EigoLensTheme.onSurface)

                Text("\(words.count)")
                    .font(EigoLensTheme.labelMedium)
                    .foregroundStyle(.white)
                    .padding(.horizontal, 8)
                    .padding(.vertical, 2)
                    .background(EigoLensTheme.primary, in: Capsule())

                Text("\(threshold.rawValue)+")
                    .font(EigoLensTheme.labelMedium)
                    .foregroundStyle(.white)
                    .padding(.horizontal, 8)
                    .padding(.vertical, 2)
                    .background(threshold.color.opacity(threshold.color == .clear ? 0 : 1), in: Capsule())

                Spacer()

                Button(action: onDismiss) {
                    Image(systemName: "xmark")
                        .foregroundStyle(EigoLensTheme.onSurfaceVariant)
                        .padding(8)
                }
            }
            .padding(.horizontal, 16)
            .padding(.top, 8)

            Divider()
                .background(Color.white.opacity(0.1))
                .padding(.vertical, 4)

            // Word list
            if words.isEmpty {
                VStack(spacing: 12) {
                    Spacer()
                    Image(systemName: "checkmark.circle")
                        .font(.system(size: 40))
                        .foregroundStyle(EigoLensTheme.success)
                    Text("No difficult words at \(threshold.rawValue)+ level")
                        .font(EigoLensTheme.bodyMedium)
                        .foregroundStyle(EigoLensTheme.onSurfaceVariant)
                    Spacer()
                }
                .frame(maxWidth: .infinity)
            } else {
                ScrollView {
                    LazyVStack(spacing: 8) {
                        ForEach(words) { word in
                            DifficultWordRow(word: word) {
                                onWordTapped(word.text)
                            }
                        }
                    }
                    .padding(.horizontal, 16)
                    .padding(.bottom, 16)
                }
            }
        }
    }
}

// MARK: - Word Row

private struct DifficultWordRow: View {
    let word: EnrichedWord
    var onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            HStack(spacing: 12) {
                // CEFR badge
                if let cefr = word.cefr {
                    Text(cefr.rawValue)
                        .font(.system(.caption2, weight: .bold))
                        .foregroundStyle(.white)
                        .frame(width: 32, height: 22)
                        .background(cefr.color.opacity(cefr.color == .clear ? 0.3 : 1), in: RoundedRectangle(cornerRadius: 4))
                }

                VStack(alignment: .leading, spacing: 2) {
                    HStack(spacing: 8) {
                        Text(word.text)
                            .font(EigoLensTheme.titleMedium)
                            .foregroundStyle(EigoLensTheme.onSurface)

                        if let ipa = word.ipa {
                            Text(ipa)
                                .font(EigoLensTheme.labelSmall)
                                .italic()
                                .foregroundStyle(Color(hex: 0x00E6FF))
                        }
                    }

                    if let def = word.briefDefinition {
                        Text(def)
                            .font(EigoLensTheme.bodySmall)
                            .foregroundStyle(EigoLensTheme.onSurfaceVariant)
                            .lineLimit(2)
                    }
                }

                Spacer()

                Image(systemName: "chevron.right")
                    .font(.caption)
                    .foregroundStyle(EigoLensTheme.onSurfaceVariant)
            }
            .padding(12)
            .glassCard()
        }
        .buttonStyle(.plain)
    }
}

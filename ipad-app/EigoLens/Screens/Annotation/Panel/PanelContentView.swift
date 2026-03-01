import SwiftUI

struct PanelContentView: View {
    let state: PanelState
    var interactionMode: InteractionMode = .tap
    var onInteractionModeChange: (InteractionMode) -> Void = { _ in }
    var onDismiss: () -> Void
    var onToggleBookmark: () -> Void
    var isBookmarked: Bool

    var body: some View {
        switch state {
        case .idle:
            EmptyView()
        case .loading:
            DefinitionSkeletonView()
        case .wordDefinition(let definition, let insight):
            DefinitionPanelView(
                definition: definition,
                insight: insight,
                isBookmarked: isBookmarked,
                onDismiss: onDismiss,
                onToggleBookmark: onToggleBookmark
            )
        case .aiLoading(let scope, let text, let readability):
            AiAnalysisPanelView(
                viewState: .loading(text, scope, readability),
                interactionMode: interactionMode,
                onInteractionModeChange: onInteractionModeChange,
                onDismiss: onDismiss
            )
        case .aiAnalysis(let scope, let text, let response, let readability):
            AiAnalysisPanelView(
                viewState: .loaded(text, scope, response, readability),
                interactionMode: interactionMode,
                onInteractionModeChange: onInteractionModeChange,
                onDismiss: onDismiss
            )
        case .notFound(let word):
            NotFoundPanelView(word: word, onDismiss: onDismiss)
        case .error(let message):
            ErrorPanelView(message: message, onDismiss: onDismiss)
        }
    }
}

// MARK: - Not Found Panel

struct NotFoundPanelView: View {
    let word: String
    var onDismiss: () -> Void

    var body: some View {
        VStack(spacing: 16) {
            HStack {
                Spacer()
                Button(action: onDismiss) {
                    Image(systemName: "xmark")
                        .foregroundStyle(.secondary)
                        .padding(8)
                }
            }

            Image(systemName: "book.closed")
                .font(.system(size: 48))
                .foregroundStyle(EigoLensTheme.onSurfaceVariant)

            Text("Not Found")
                .font(EigoLensTheme.titleLarge)
                .foregroundStyle(EigoLensTheme.onSurface)

            Text("No definition found for \"\(word)\"")
                .font(EigoLensTheme.bodyMedium)
                .foregroundStyle(EigoLensTheme.onSurfaceVariant)
                .multilineTextAlignment(.center)

            Spacer()
        }
        .padding()
    }
}

// MARK: - Error Panel

struct ErrorPanelView: View {
    let message: String
    var onDismiss: () -> Void

    var body: some View {
        VStack(spacing: 16) {
            HStack {
                Spacer()
                Button(action: onDismiss) {
                    Image(systemName: "xmark")
                        .foregroundStyle(.secondary)
                        .padding(8)
                }
            }

            Image(systemName: "exclamationmark.triangle")
                .font(.system(size: 48))
                .foregroundStyle(EigoLensTheme.error)

            Text("Error")
                .font(EigoLensTheme.titleLarge)
                .foregroundStyle(EigoLensTheme.onSurface)

            Text(message)
                .font(EigoLensTheme.bodyMedium)
                .foregroundStyle(EigoLensTheme.onSurfaceVariant)
                .multilineTextAlignment(.center)

            Spacer()
        }
        .padding()
    }
}

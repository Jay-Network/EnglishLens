import SwiftUI
import SwiftData

@main
struct EigoLensApp: App {
    @StateObject private var container = AppContainer()

    var body: some Scene {
        WindowGroup {
            if let error = container.initError {
                ErrorInitView(message: error)
            } else if container.authManager.isRestoringSession {
                ProgressView("Restoring session...")
                    .environmentObject(container)
            } else if container.authManager.authState.isLoggedIn || container.authManager.isGuest {
                AppNavigation()
                    .environmentObject(container)
            } else {
                LoginScreen(
                    onLoginSuccess: { },
                    onContinueAsGuest: { container.authManager.continueAsGuest() }
                )
                .environmentObject(container)
            }
        }
        .modelContainer(for: [LookupHistoryEntry.self, BookmarkedWord.self])
    }
}

struct ErrorInitView: View {
    let message: String

    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "exclamationmark.triangle.fill")
                .font(.system(size: 48))
                .foregroundStyle(.red)
            Text("Initialization Error")
                .font(EigoLensTheme.headlineMedium)
            Text(message)
                .font(EigoLensTheme.bodyMedium)
                .foregroundStyle(.secondary)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 32)
        }
    }
}

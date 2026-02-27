import SwiftUI

@MainActor
final class SettingsViewModel: ObservableObject {
    @Published var claudeApiKey = ""
    @Published var geminiApiKey = ""
    @Published var activeProvider: String? = nil
    @Published var claudeInputTokens = 0
    @Published var claudeOutputTokens = 0
    @Published var geminiInputTokens = 0
    @Published var geminiOutputTokens = 0

    private var keychainStore: KeychainStore?
    private var aiProviderManager: AiProviderManager?
    private var authManager: AuthManager?
    private let tokenUsageStore = TokenUsageStore()

    func configure(container: AppContainer) {
        keychainStore = container.keychainStore
        aiProviderManager = container.aiProviderManager
        authManager = container.authManager

        claudeApiKey = keychainStore?.load(key: KeychainStore.Keys.claudeApiKey)
            ?? Configuration.builtInClaudeApiKey
        geminiApiKey = keychainStore?.load(key: KeychainStore.Keys.geminiApiKey)
            ?? Configuration.builtInGeminiApiKey
        activeProvider = UserDefaults.standard.string(forKey: "active_provider") ?? aiProviderManager?.activeProviderName
        refreshTokenUsage()
    }

    func saveClaudeKey() {
        try? keychainStore?.save(key: KeychainStore.Keys.claudeApiKey, value: claudeApiKey)
        reconfigureAi()
    }

    func saveGeminiKey() {
        try? keychainStore?.save(key: KeychainStore.Keys.geminiApiKey, value: geminiApiKey)
        reconfigureAi()
    }

    func setActiveProvider(_ provider: String?) {
        activeProvider = provider
        UserDefaults.standard.set(provider, forKey: "active_provider")
        reconfigureAi()
    }

    func resetTokenUsage(provider: String?) {
        tokenUsageStore.resetTokenUsage(provider: provider)
        refreshTokenUsage()
    }

    private func refreshTokenUsage() {
        claudeInputTokens = tokenUsageStore.claudeInputTokens
        claudeOutputTokens = tokenUsageStore.claudeOutputTokens
        geminiInputTokens = tokenUsageStore.geminiInputTokens
        geminiOutputTokens = tokenUsageStore.geminiOutputTokens
    }

    func signOut() {
        Task {
            await authManager?.signOut()
        }
    }

    private func reconfigureAi() {
        aiProviderManager?.configure(
            claudeKey: claudeApiKey,
            geminiKey: geminiApiKey,
            preferred: activeProvider
        )
    }
}

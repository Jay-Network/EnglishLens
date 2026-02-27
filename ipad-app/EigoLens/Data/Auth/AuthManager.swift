import Foundation

@MainActor
final class AuthManager: ObservableObject {
    @Published var authState = AuthState()
    @Published var isRestoringSession = true
    @Published var isGuest = false

    private let client: SupabaseAuthClient
    private let keychain: KeychainStore

    init(client: SupabaseAuthClient, keychain: KeychainStore) {
        self.client = client
        self.keychain = keychain

        Task { await restoreSession() }
    }

    // MARK: - Session Restoration

    private func restoreSession() async {
        defer { isRestoringSession = false }

        guard client.isConfigured else { return }

        guard let accessToken = keychain.load(key: KeychainStore.Keys.supabaseAccessToken),
              let refreshToken = keychain.load(key: KeychainStore.Keys.supabaseRefreshToken) else {
            return
        }

        // Try validating current token
        do {
            let user = try await client.getUser(accessToken: accessToken)
            authState = AuthState(isLoggedIn: true, userId: user.id, email: user.email)
            return
        } catch {
            // Token may be expired, try refresh
        }

        // Try refreshing
        do {
            let session = try await client.refreshToken(refreshToken)
            saveSession(session)
            authState = AuthState(isLoggedIn: true, userId: session.user.id, email: session.user.email)
        } catch {
            clearSession()
        }
    }

    // MARK: - Sign In

    func signIn(email: String, password: String) async throws {
        let session = try await client.signIn(email: email, password: password)
        saveSession(session)
        isGuest = false
        authState = AuthState(isLoggedIn: true, userId: session.user.id, email: session.user.email)
    }

    // MARK: - Sign Up

    func signUp(email: String, password: String) async throws {
        let session = try await client.signUp(email: email, password: password)
        saveSession(session)
        isGuest = false
        authState = AuthState(isLoggedIn: true, userId: session.user.id, email: session.user.email)
    }

    // MARK: - Sign Out

    func signOut() async {
        if let token = keychain.load(key: KeychainStore.Keys.supabaseAccessToken) {
            await client.signOut(accessToken: token)
        }
        clearSession()
        isGuest = false
        authState = AuthState()
    }

    // MARK: - Guest

    func continueAsGuest() {
        isGuest = true
        isRestoringSession = false
    }

    // MARK: - Private

    private func saveSession(_ session: AuthSession) {
        try? keychain.save(key: KeychainStore.Keys.supabaseAccessToken, value: session.accessToken)
        try? keychain.save(key: KeychainStore.Keys.supabaseRefreshToken, value: session.refreshToken)
        try? keychain.save(key: KeychainStore.Keys.supabaseUserId, value: session.user.id)
        if let email = session.user.email {
            try? keychain.save(key: KeychainStore.Keys.supabaseUserEmail, value: email)
        }
    }

    private func clearSession() {
        keychain.delete(key: KeychainStore.Keys.supabaseAccessToken)
        keychain.delete(key: KeychainStore.Keys.supabaseRefreshToken)
        keychain.delete(key: KeychainStore.Keys.supabaseUserId)
        keychain.delete(key: KeychainStore.Keys.supabaseUserEmail)
    }
}

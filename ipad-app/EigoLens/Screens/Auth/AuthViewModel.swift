import Foundation

@MainActor
final class AuthViewModel: ObservableObject {
    @Published var isLoading = false
    @Published var errorMessage: String?
    @Published var isLoggedIn = false
    @Published var isSignUpMode = false

    func signIn(email: String, password: String, authManager: AuthManager) {
        guard !email.isEmpty, !password.isEmpty else {
            errorMessage = "Please enter both email and password."
            return
        }

        isLoading = true
        errorMessage = nil

        Task {
            do {
                try await authManager.signIn(email: email, password: password)
                isLoggedIn = true
            } catch {
                errorMessage = (error as? AuthError)?.errorDescription ?? error.localizedDescription
            }
            isLoading = false
        }
    }

    func signUp(email: String, password: String, authManager: AuthManager) {
        guard !email.isEmpty, !password.isEmpty else {
            errorMessage = "Please enter both email and password."
            return
        }

        guard password.count >= 6 else {
            errorMessage = "Password must be at least 6 characters."
            return
        }

        isLoading = true
        errorMessage = nil

        Task {
            do {
                try await authManager.signUp(email: email, password: password)
                isLoggedIn = true
            } catch {
                errorMessage = (error as? AuthError)?.errorDescription ?? error.localizedDescription
            }
            isLoading = false
        }
    }

    func toggleSignUpMode() {
        isSignUpMode.toggle()
        errorMessage = nil
    }

    func clearError() {
        errorMessage = nil
    }
}

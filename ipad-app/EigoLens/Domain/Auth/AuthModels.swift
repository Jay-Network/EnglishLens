import Foundation

struct AuthState {
    var isLoggedIn = false
    var userId: String?
    var email: String?
}

struct AuthSession: Decodable {
    let accessToken: String
    let refreshToken: String
    let expiresAt: Int
    let user: SupabaseUser

    enum CodingKeys: String, CodingKey {
        case accessToken = "access_token"
        case refreshToken = "refresh_token"
        case expiresAt = "expires_at"
        case user
    }
}

struct SupabaseUser: Decodable {
    let id: String
    let email: String?
    let createdAt: String?

    enum CodingKeys: String, CodingKey {
        case id
        case email
        case createdAt = "created_at"
    }
}

enum AuthError: LocalizedError {
    case notConfigured
    case invalidCredentials
    case emailNotConfirmed
    case userAlreadyExists
    case networkError(Error)
    case serverError(Int)

    var errorDescription: String? {
        switch self {
        case .notConfigured:
            return "Authentication is not configured."
        case .invalidCredentials:
            return "Invalid email or password."
        case .emailNotConfirmed:
            return "Please confirm your email before signing in."
        case .userAlreadyExists:
            return "An account with this email already exists."
        case .networkError:
            return "Network error. Please check your connection."
        case .serverError(let code):
            return "Server error (\(code)). Please try again."
        }
    }
}

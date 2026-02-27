import Foundation

final class SupabaseAuthClient {
    private let baseURL: String
    private let anonKey: String

    var isConfigured: Bool {
        !baseURL.isEmpty && !anonKey.isEmpty
    }

    init(baseURL: String, anonKey: String) {
        self.baseURL = baseURL
        self.anonKey = anonKey
    }

    // MARK: - Sign Up

    func signUp(email: String, password: String) async throws -> AuthSession {
        let body: [String: String] = ["email": email, "password": password]
        let data = try await post(path: "/auth/v1/signup", body: body)

        // Supabase returns user without session if email confirmation is required
        if let json = try? JSONSerialization.jsonObject(with: data) as? [String: Any],
           json["access_token"] == nil {
            throw AuthError.emailNotConfirmed
        }

        return try JSONDecoder().decode(AuthSession.self, from: data)
    }

    // MARK: - Sign In

    func signIn(email: String, password: String) async throws -> AuthSession {
        let body: [String: String] = ["email": email, "password": password]
        let data = try await post(path: "/auth/v1/token?grant_type=password", body: body)
        return try JSONDecoder().decode(AuthSession.self, from: data)
    }

    // MARK: - Sign Out

    func signOut(accessToken: String) async {
        var request = makeRequest(path: "/auth/v1/logout", method: "POST")
        request.setValue("Bearer \(accessToken)", forHTTPHeaderField: "Authorization")
        _ = try? await URLSession.shared.data(for: request)
    }

    // MARK: - Refresh Token

    func refreshToken(_ refreshToken: String) async throws -> AuthSession {
        let body: [String: String] = ["refresh_token": refreshToken]
        let data = try await post(path: "/auth/v1/token?grant_type=refresh_token", body: body)
        return try JSONDecoder().decode(AuthSession.self, from: data)
    }

    // MARK: - Get User

    func getUser(accessToken: String) async throws -> SupabaseUser {
        var request = makeRequest(path: "/auth/v1/user", method: "GET")
        request.setValue("Bearer \(accessToken)", forHTTPHeaderField: "Authorization")

        let (data, response) = try await URLSession.shared.data(for: request)
        try validateResponse(response, data: data)
        return try JSONDecoder().decode(SupabaseUser.self, from: data)
    }

    // MARK: - Private

    private func makeRequest(path: String, method: String) -> URLRequest {
        let url = URL(string: baseURL + path)!
        var request = URLRequest(url: url)
        request.httpMethod = method
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue(anonKey, forHTTPHeaderField: "apikey")
        request.timeoutInterval = 15
        return request
    }

    private func post(path: String, body: [String: String]) async throws -> Data {
        var request = makeRequest(path: path, method: "POST")
        request.httpBody = try JSONSerialization.data(withJSONObject: body)

        let (data, response): (Data, URLResponse)
        do {
            (data, response) = try await URLSession.shared.data(for: request)
        } catch {
            throw AuthError.networkError(error)
        }

        try validateResponse(response, data: data)
        return data
    }

    private func validateResponse(_ response: URLResponse, data: Data) throws {
        guard let http = response as? HTTPURLResponse else {
            throw AuthError.serverError(0)
        }

        guard (200...299).contains(http.statusCode) else {
            throw mapError(statusCode: http.statusCode, data: data)
        }
    }

    private func mapError(statusCode: Int, data: Data) -> AuthError {
        // Try to parse Supabase error message
        let json = try? JSONSerialization.jsonObject(with: data) as? [String: Any]
        let message = (json?["error_description"] as? String)
            ?? (json?["msg"] as? String)
            ?? (json?["message"] as? String)
            ?? ""

        let lower = message.lowercased()

        if lower.contains("invalid login credentials") || lower.contains("invalid grant") {
            return .invalidCredentials
        }
        if lower.contains("email not confirmed") {
            return .emailNotConfirmed
        }
        if lower.contains("already registered") || lower.contains("already been registered") {
            return .userAlreadyExists
        }
        if statusCode == 422 || statusCode == 400 {
            return .invalidCredentials
        }

        return .serverError(statusCode)
    }
}

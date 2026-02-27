import Foundation
import CoreGraphics

enum Configuration {
    static let wordNetDbName = "wordnet"
    static let wordNetDbExtension = "db"
    static let definitionCacheSize = 300
    static let maxImageDimension: CGFloat = 2048
    static let preloadWordCount = 100
    static let tapTolerancePoints: CGFloat = 20.0

    // Build-time API keys from xcconfig → Info.plist
    static var builtInGeminiApiKey: String {
        Bundle.main.infoDictionary?["GEMINI_API_KEY"] as? String ?? ""
    }

    static var builtInClaudeApiKey: String {
        Bundle.main.infoDictionary?["CLAUDE_API_KEY"] as? String ?? ""
    }

    static var builtInSupabaseUrl: String {
        Bundle.main.infoDictionary?["SUPABASE_URL"] as? String ?? ""
    }

    static var builtInSupabaseAnonKey: String {
        Bundle.main.infoDictionary?["SUPABASE_ANON_KEY"] as? String ?? ""
    }
}

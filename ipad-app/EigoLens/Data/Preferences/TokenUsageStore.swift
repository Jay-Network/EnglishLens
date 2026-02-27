import Foundation

final class TokenUsageStore {
    private enum Keys {
        static let claudeInputTokens = "claude_input_tokens"
        static let claudeOutputTokens = "claude_output_tokens"
        static let geminiInputTokens = "gemini_input_tokens"
        static let geminiOutputTokens = "gemini_output_tokens"
    }

    struct Pricing {
        let inputRatePerMillion: Double
        let outputRatePerMillion: Double
    }

    static let claudePricing = Pricing(inputRatePerMillion: 0.80, outputRatePerMillion: 4.00)
    static let geminiPricing = Pricing(inputRatePerMillion: 0.15, outputRatePerMillion: 0.60)

    private let defaults = UserDefaults.standard

    var claudeInputTokens: Int {
        defaults.integer(forKey: Keys.claudeInputTokens)
    }
    var claudeOutputTokens: Int {
        defaults.integer(forKey: Keys.claudeOutputTokens)
    }
    var geminiInputTokens: Int {
        defaults.integer(forKey: Keys.geminiInputTokens)
    }
    var geminiOutputTokens: Int {
        defaults.integer(forKey: Keys.geminiOutputTokens)
    }

    func addTokenUsage(provider: String, inputTokens: Int, outputTokens: Int) {
        switch provider {
        case "Claude":
            defaults.set(claudeInputTokens + inputTokens, forKey: Keys.claudeInputTokens)
            defaults.set(claudeOutputTokens + outputTokens, forKey: Keys.claudeOutputTokens)
        case "Gemini":
            defaults.set(geminiInputTokens + inputTokens, forKey: Keys.geminiInputTokens)
            defaults.set(geminiOutputTokens + outputTokens, forKey: Keys.geminiOutputTokens)
        default:
            break
        }
    }

    func resetTokenUsage(provider: String?) {
        switch provider {
        case "Claude":
            defaults.set(0, forKey: Keys.claudeInputTokens)
            defaults.set(0, forKey: Keys.claudeOutputTokens)
        case "Gemini":
            defaults.set(0, forKey: Keys.geminiInputTokens)
            defaults.set(0, forKey: Keys.geminiOutputTokens)
        case nil:
            defaults.set(0, forKey: Keys.claudeInputTokens)
            defaults.set(0, forKey: Keys.claudeOutputTokens)
            defaults.set(0, forKey: Keys.geminiInputTokens)
            defaults.set(0, forKey: Keys.geminiOutputTokens)
        default:
            break
        }
    }

    static func estimatedCost(inputTokens: Int, outputTokens: Int, pricing: Pricing) -> Double {
        (Double(inputTokens) * pricing.inputRatePerMillion + Double(outputTokens) * pricing.outputRatePerMillion) / 1_000_000.0
    }
}

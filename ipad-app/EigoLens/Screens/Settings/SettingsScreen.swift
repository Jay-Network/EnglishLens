import SwiftUI

struct SettingsScreen: View {
    var onBack: () -> Void
    @EnvironmentObject var container: AppContainer
    @StateObject private var viewModel = SettingsViewModel()

    var body: some View {
        List {
            // Account Section
            Section("Account") {
                if let email = container.authManager.authState.email {
                    HStack {
                        Text("Signed in as")
                        Spacer()
                        Text(email)
                            .foregroundStyle(.secondary)
                    }
                    Button("Sign Out", role: .destructive) {
                        viewModel.signOut()
                    }
                } else {
                    Text("Guest Mode")
                        .foregroundStyle(.secondary)
                }
            }

            // AI Analysis Section
            Section("AI Analysis") {
                // Provider selection
                Picker("Active Provider", selection: $viewModel.activeProvider) {
                    ForEach(["Claude", "Gemini"], id: \.self) { provider in
                        Text(provider).tag(provider as String?)
                    }
                }
                .onChange(of: viewModel.activeProvider) { _, newValue in
                    viewModel.setActiveProvider(newValue)
                }
            }

            // API Keys Section
            Section("API Keys") {
                apiKeyEditor(
                    label: "Claude API Key",
                    key: $viewModel.claudeApiKey,
                    placeholder: "sk-ant-...",
                    onSave: { viewModel.saveClaudeKey() }
                )

                apiKeyEditor(
                    label: "Gemini API Key",
                    key: $viewModel.geminiApiKey,
                    placeholder: "AI...",
                    onSave: { viewModel.saveGeminiKey() }
                )
            }

            // Token Usage Section
            Section("Token Usage") {
                tokenUsageCard(
                    providerName: "Claude Haiku",
                    inputTokens: viewModel.claudeInputTokens,
                    outputTokens: viewModel.claudeOutputTokens,
                    pricing: TokenUsageStore.claudePricing,
                    onReset: { viewModel.resetTokenUsage(provider: "Claude") }
                )
                tokenUsageCard(
                    providerName: "Gemini 2.5 Flash",
                    inputTokens: viewModel.geminiInputTokens,
                    outputTokens: viewModel.geminiOutputTokens,
                    pricing: TokenUsageStore.geminiPricing,
                    onReset: { viewModel.resetTokenUsage(provider: "Gemini") }
                )
            }

            // About Section
            Section("About") {
                infoRow(label: "Version", value: Bundle.main.infoPlistString(for: "CFBundleShortVersionString") ?? "1.0.0")
                infoRow(label: "Build", value: Bundle.main.infoPlistString(for: "CFBundleVersion") ?? "1")
                infoRow(label: "OCR Engine", value: "Apple Vision Framework")
                infoRow(label: "Dictionary", value: "WordNet")
            }

            // Features Section
            Section("Features") {
                featureRow(icon: "camera.fill", title: "Camera OCR", description: "Capture text from photos")
                featureRow(icon: "textformat.abc", title: "Word Definitions", description: "WordNet with NLP lemmatization")
                featureRow(icon: "brain", title: "AI Analysis", description: "Claude & Gemini integration")
                featureRow(icon: "chart.bar.fill", title: "Readability", description: "FK Grade, SMOG, Coleman-Liau, Flesch")
            }
        }
        .navigationTitle("Settings")
        .navigationBarTitleDisplayMode(.large)
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: onBack) {
                    Image(systemName: "chevron.left")
                }
            }
        }
        .task {
            viewModel.configure(container: container)
        }
        .preferredColorScheme(.dark)
    }

    // MARK: - API Key Editor

    private func apiKeyEditor(label: String, key: Binding<String>, placeholder: String, onSave: @escaping () -> Void) -> some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(label)
                .font(EigoLensTheme.titleMedium)
            HStack {
                SecureField(placeholder, text: key)
                    .textFieldStyle(.roundedBorder)
                    .font(EigoLensTheme.bodyMedium)

                Button("Save", action: onSave)
                    .buttonStyle(.borderedProminent)
                    .tint(EigoLensTheme.primary)
                    .disabled(key.wrappedValue.isEmpty)

                if !key.wrappedValue.isEmpty {
                    Button(action: {
                        key.wrappedValue = ""
                        onSave()
                    }) {
                        Image(systemName: "trash")
                            .foregroundStyle(EigoLensTheme.error)
                    }
                }
            }
        }
        .padding(.vertical, 4)
    }

    private func infoRow(label: String, value: String) -> some View {
        HStack {
            Text(label)
            Spacer()
            Text(value)
                .foregroundStyle(.secondary)
        }
    }

    private func tokenUsageCard(providerName: String, inputTokens: Int, outputTokens: Int, pricing: TokenUsageStore.Pricing, onReset: @escaping () -> Void) -> some View {
        let cost = TokenUsageStore.estimatedCost(inputTokens: inputTokens, outputTokens: outputTokens, pricing: pricing)
        let hasUsage = inputTokens > 0 || outputTokens > 0

        return VStack(alignment: .leading, spacing: 8) {
            Text(providerName)
                .font(EigoLensTheme.titleMedium)

            HStack {
                Text("Input")
                    .font(EigoLensTheme.bodySmall)
                    .foregroundStyle(.secondary)
                Spacer()
                Text(inputTokens.formatted())
                    .font(EigoLensTheme.bodyMedium)
                    .monospacedDigit()
            }
            HStack {
                Text("Output")
                    .font(EigoLensTheme.bodySmall)
                    .foregroundStyle(.secondary)
                Spacer()
                Text(outputTokens.formatted())
                    .font(EigoLensTheme.bodyMedium)
                    .monospacedDigit()
            }
            HStack {
                Text("Est. cost")
                    .font(EigoLensTheme.bodySmall)
                    .foregroundStyle(.secondary)
                Spacer()
                Text(String(format: "$%.4f", cost))
                    .font(EigoLensTheme.bodyMedium)
                    .monospacedDigit()
            }

            if hasUsage {
                Button("Reset", role: .destructive, action: onReset)
                    .font(EigoLensTheme.bodySmall)
            }
        }
        .padding(.vertical, 4)
    }

    private func featureRow(icon: String, title: String, description: String) -> some View {
        HStack(spacing: 12) {
            Image(systemName: icon)
                .font(.title3)
                .foregroundStyle(EigoLensTheme.primary)
                .frame(width: 32)
            VStack(alignment: .leading) {
                Text(title)
                    .font(EigoLensTheme.bodyLarge)
                Text(description)
                    .font(EigoLensTheme.bodySmall)
                    .foregroundStyle(.secondary)
            }
        }
    }
}

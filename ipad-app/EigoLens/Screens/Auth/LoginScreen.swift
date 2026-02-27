import SwiftUI

struct LoginScreen: View {
    var onLoginSuccess: () -> Void
    var onContinueAsGuest: () -> Void

    @EnvironmentObject var container: AppContainer
    @StateObject private var viewModel = AuthViewModel()
    @State private var email = ""
    @State private var password = ""

    var body: some View {
        GeometryReader { geo in
            ScrollView {
                VStack(spacing: EigoLensTheme.spacingL) {
                    Spacer().frame(height: geo.size.height * 0.08)

                    // App title
                    VStack(spacing: EigoLensTheme.spacingS) {
                        Text("EigoLens")
                            .font(EigoLensTheme.headlineLarge)
                            .foregroundStyle(EigoLensTheme.primary)

                        Text("Scan. Understand. Learn.")
                            .font(EigoLensTheme.bodyLarge)
                            .foregroundStyle(.secondary)
                    }

                    Spacer().frame(height: EigoLensTheme.spacingM)

                    // Sign-in card
                    VStack(spacing: EigoLensTheme.spacingM) {
                        Text(viewModel.isSignUpMode ? "Create Account" : "Sign In with Email")
                            .font(EigoLensTheme.titleLarge)

                        TextField("Email", text: $email)
                            .textFieldStyle(.roundedBorder)
                            .textContentType(.emailAddress)
                            .keyboardType(.emailAddress)
                            .autocorrectionDisabled()
                            .textInputAutocapitalization(.never)

                        SecureField("Password", text: $password)
                            .textFieldStyle(.roundedBorder)
                            .textContentType(.password)

                        if let error = viewModel.errorMessage {
                            Text(error)
                                .font(EigoLensTheme.bodySmall)
                                .foregroundStyle(EigoLensTheme.error)
                                .multilineTextAlignment(.center)
                        }

                        Button {
                            if viewModel.isSignUpMode {
                                viewModel.signUp(email: email, password: password, authManager: container.authManager)
                            } else {
                                viewModel.signIn(email: email, password: password, authManager: container.authManager)
                            }
                        } label: {
                            if viewModel.isLoading {
                                ProgressView()
                                    .tint(.white)
                                    .frame(maxWidth: .infinity)
                                    .frame(height: 20)
                            } else {
                                Text(viewModel.isSignUpMode ? "Create Account" : "Sign In")
                                    .frame(maxWidth: .infinity)
                            }
                        }
                        .buttonStyle(.borderedProminent)
                        .tint(EigoLensTheme.primary)
                        .disabled(email.isEmpty || password.isEmpty || viewModel.isLoading)

                        Button {
                            viewModel.toggleSignUpMode()
                        } label: {
                            Text(viewModel.isSignUpMode
                                 ? "Already have an account? Sign In"
                                 : "New here? Create an account")
                                .font(EigoLensTheme.bodySmall)
                                .foregroundStyle(EigoLensTheme.secondary)
                        }
                    }
                    .padding(EigoLensTheme.spacingL)
                    .background(.regularMaterial, in: RoundedRectangle(cornerRadius: 16))

                    // Divider
                    HStack {
                        Rectangle().frame(height: 1).foregroundStyle(EigoLensTheme.outline)
                        Text("or")
                            .font(EigoLensTheme.bodySmall)
                            .foregroundStyle(.secondary)
                            .padding(.horizontal, EigoLensTheme.spacingS)
                        Rectangle().frame(height: 1).foregroundStyle(EigoLensTheme.outline)
                    }

                    // Guest button
                    Button {
                        onContinueAsGuest()
                    } label: {
                        Text("Try as Guest")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.bordered)
                    .tint(EigoLensTheme.secondary)

                    Text("Continue without an account.\nSign in anytime for cloud features.")
                        .font(EigoLensTheme.bodySmall)
                        .foregroundStyle(.secondary)
                        .multilineTextAlignment(.center)

                    Spacer()
                }
                .frame(maxWidth: 500)
                .frame(maxWidth: .infinity)
                .padding(.horizontal, EigoLensTheme.spacingXL)
            }
        }
        .background(EigoLensTheme.background.ignoresSafeArea())
        .onChange(of: viewModel.isLoggedIn) { _, loggedIn in
            if loggedIn { onLoginSuccess() }
        }
    }
}

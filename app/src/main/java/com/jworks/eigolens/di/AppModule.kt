package com.jworks.eigolens.di

import android.content.Context
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.jworks.eigolens.data.feedback.FeedbackRepositoryImpl
import com.jworks.eigolens.data.preferences.SettingsDataStore
import com.jworks.eigolens.data.repository.SettingsRepositoryImpl
import com.jworks.eigolens.domain.repository.FeedbackRepository
import com.jworks.eigolens.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTextRecognizer(): TextRecognizer {
        // English/Latin text recognition (default, bundled with Play Services)
        return TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): SettingsDataStore {
        return SettingsDataStore(context)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideFeedbackRepository(): FeedbackRepository {
        return FeedbackRepositoryImpl()
    }
}

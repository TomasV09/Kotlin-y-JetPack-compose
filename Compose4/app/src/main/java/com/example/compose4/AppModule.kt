package com.example.compose4

import android.content.Context
import androidx.room.Room
import com.example.compose4.AppDatabase
import com.example.compose4.TransaccionDao
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "finanzas_db"
        ).build()
    }

    @Provides
    fun provideDao(db: AppDatabase): TransaccionDao = db.transaccionDao()
}
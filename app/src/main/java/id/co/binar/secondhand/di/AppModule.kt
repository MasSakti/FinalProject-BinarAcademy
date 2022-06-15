package id.co.binar.secondhand.di

import android.app.Notification
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.co.binar.secondhand.data.remote.*
import id.co.binar.secondhand.util.DataStoreManager
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideBuyerApi(retrofit: Retrofit): BuyerApi {
        return retrofit.create(BuyerApi::class.java)
    }

    @Singleton
    @Provides
    fun provideHistoryApi(retrofit: Retrofit): HistoryApi {
        return retrofit.create(HistoryApi::class.java)
    }

    @Singleton
    @Provides
    fun provideNotificationApi(retrofit: Retrofit): NotificationApi {
        return retrofit.create(NotificationApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSellerApi(retrofit: Retrofit): SellerApi {
        return retrofit.create(SellerApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }
}
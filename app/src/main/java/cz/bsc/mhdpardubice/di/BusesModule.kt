package cz.bsc.mhdpardubice.di

import cz.bsc.mhdpardubice.data.BusesRepo
import cz.bsc.mhdpardubice.data.BusesRepoImpl
import cz.bsc.mhdpardubice.network.BusesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 *@author Josef Novotný on 30.03.2022.
 */
@Module
@InstallIn(SingletonComponent::class)
object BusesModule {

    @Provides
    @Singleton
    fun busesRepo(busesApi: BusesApi): BusesRepo = BusesRepoImpl(busesApi, "869f749a-1b72-4ccb-b888-cb0aef8e0aea")

    @Provides
    @Singleton
    fun busesApi(retrofit: Retrofit): BusesApi = retrofit.create(BusesApi::class.java)
}
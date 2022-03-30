package cz.bsc.mhdpardubice.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 *@author Josef Novotný on 30.03.2022.
 */
interface BusesApi {

    @Headers("content-type: text/plain")
    @POST("buses")
    suspend fun fetchBuses(@Body key: Key): Response<Buses>
}
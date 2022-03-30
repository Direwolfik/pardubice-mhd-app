package cz.bsc.mhdpardubice.data

import androidx.compose.runtime.Composable
import cz.bsc.mhdpardubice.network.Buses
import cz.bsc.mhdpardubice.network.BusesApi
import cz.bsc.mhdpardubice.network.Key
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.coroutines.coroutineContext

/**
 *@author Josef Novotný on 30.03.2022.
 */
interface BusesRepo {
    val buses: Flow<List<Buses.Bus>>

    suspend fun startRefresh()

    fun stopRefresh()
}

class BusesRepoImpl(
        private val busesApi: BusesApi,
        private val apiKey: String
) : BusesRepo {

    private var busesRefreshJob: Job? = null

    private val _buses: MutableStateFlow<List<Buses.Bus>> = MutableStateFlow(emptyList())

    override val buses: Flow<List<Buses.Bus>>
        get() = _buses.asSharedFlow()

    override suspend fun startRefresh() {
        busesRefreshJob?.cancel()
        busesRefreshJob = startChecking(CoroutineScope(coroutineContext))
    }

    override fun stopRefresh() {
        busesRefreshJob?.cancel()
    }

    private fun startChecking(scope: CoroutineScope): Job =
        scope.launch {
            while (true) {
                val result = busesApi.fetchBuses(Key(apiKey))
                if (result.isSuccessful) {
                    _buses.emit(result.body()?.data ?: emptyList())
                } else {
                    _buses.emit(emptyList())
                }
                delay(DEFAULT_REFRESH_INTERVAL)
            }
        }

    companion object {
        private const val DEFAULT_REFRESH_INTERVAL = 5_000L
    }
}

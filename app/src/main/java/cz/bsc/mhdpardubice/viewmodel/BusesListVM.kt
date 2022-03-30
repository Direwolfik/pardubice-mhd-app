package cz.bsc.mhdpardubice.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.bsc.mhdpardubice.data.BusesRepo
import cz.bsc.mhdpardubice.network.Buses
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 *@author Josef Novotný on 30.03.2022.
 */
@HiltViewModel
class BusesListVM @Inject constructor(
        busesRepo: BusesRepo
) : ViewModel() {

    val allBuses = mutableStateOf(emptyList<Buses.Bus>())
    val filteredBuses = mutableStateOf(allBuses.value)
    val query = mutableStateOf("")

    init {
        busesRepo.buses.onEach {
            allBuses.value = it
            updateFilteredBuses()
        }.launchIn(viewModelScope)
    }


    fun updateSearch(newQuery: String) {
        query.value = newQuery
        updateFilteredBuses()
    }

    private fun updateFilteredBuses() {
        filteredBuses.value = (if (query.value.isEmpty()) allBuses.value else allBuses.value.filter { it.lineName?.contains(query.value) == true }).sortedBy { it.lineName?.toIntOrNull() }
    }
}
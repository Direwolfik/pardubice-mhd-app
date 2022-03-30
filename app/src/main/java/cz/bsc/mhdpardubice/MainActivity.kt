package cz.bsc.mhdpardubice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import cz.bsc.mhdpardubice.data.BusesRepo
import cz.bsc.mhdpardubice.network.Buses
import cz.bsc.mhdpardubice.ui.theme.MhdPardubiceTheme
import cz.bsc.mhdpardubice.viewmodel.BusesListVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var busesRepo: BusesRepo

    val vm by viewModels<BusesListVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MhdPardubiceTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Tabs(vm = vm)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            busesRepo.startRefresh()
        }
    }

    override fun onPause() {
        super.onPause()
        busesRepo.stopRefresh()
    }
}

@Composable
fun Tabs(vm: BusesListVM) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("List", "Map")

    Column { // 2.
        TabRow(selectedTabIndex = tabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        text = { Text(text = title) })
            }
        }
        when (tabIndex) { // 6.
            0 -> BusesScreen(busesListVM = vm)
            1 -> Map(vm = vm)
            2 -> Text("World content")
        }
    }
}

@Composable
fun BusesScreen(busesListVM: BusesListVM) {

    BusSearch(busesListVM::updateSearch, busesListVM.query.value)
    BusesList(buses = busesListVM.filteredBuses.value)
}

@Composable
fun BusSearch(onValueChange: (String) -> Unit, query: String) {

    TextField(
            value = query,
            onValueChange = {
                onValueChange(it)
            },
            label = { Text("Search") },
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp))
}

@Composable
fun BusesList(buses: List<Buses.Bus>) {
    Column {
        LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.weight(1f)
        ) {
            items(items = buses) {
                BusesRow(bus = it, modifier = Modifier.fillParentMaxWidth())
            }
        }
    }
}

@Composable
fun BusesRow(bus: Buses.Bus, modifier: Modifier) {
    Card(
            elevation = 8.dp,
            modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            Icon(
                    modifier = Modifier
                            .padding(end = 8.dp)
                            .align(CenterVertically),
                    painter = painterResource(
                            id = if (bus.door == true) R.drawable.ic_baseline_accessible_24 else R.drawable.ic_baseline_not_accessible_24),
                    contentDescription = ""
            )
            Column {
                Row(modifier = Modifier.padding(bottom = 4.dp)) {
                    Text(
                            modifier = Modifier
                                    .weight(1f),
                            text = bus.lineName ?: "No name"
                    )
                    Text(
                            modifier = Modifier
                                    .padding(start = 8.dp)
                                    .wrapContentWidth(),
                            text = bus.timeDifference ?: "No diff"
                    )
                }
                Row {
                    Text(
                            modifier = Modifier
                                    .weight(1f),
                            text = bus.currentStopName ?: "No current stop"
                    )
                    Text(
                            modifier = Modifier
                                    .padding(start = 8.dp)
                                    .wrapContentWidth(),
                            text = bus.currentStopScheduledDeparture ?: "No departure time"
                    )
                }
            }
        }
    }
}

@Composable
fun Map(vm: BusesListVM) {
    BusesMap(buses = vm.allBuses.value)
}

@Composable
fun BusesMap(buses: List<Buses.Bus>) {
    val home = LatLng(50.029666, 15.775078)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(home, 12f)
    }

    GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
    ) {
        buses.forEach {
            if (it.gpsLatitude != null && it.gpsLongitude != null) {
                Marker(
                        state = MarkerState(position = LatLng(it.gpsLatitude.toDouble(), it.gpsLongitude.toDouble())),
                        title = it.lineName
                )
            }
        }
    }
}


@Preview
@Composable
fun Preview() {
    BusesList(buses = listOf(
            Buses.Bus(lineName = "jednička"),
            Buses.Bus(lineName = "dvojka")
    ))
}
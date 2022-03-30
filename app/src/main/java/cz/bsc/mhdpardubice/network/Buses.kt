package cz.bsc.mhdpardubice.network


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Buses(
        @Json(name = "data")
        val `data`: List<Bus> = listOf(),
        @Json(name = "success")
        val success: Boolean? = false
) {
    @JsonClass(generateAdapter = true)
    data class Bus(
            @Json(name = "connection_no")
            val connectionNo: Int? = null,
            @Json(name = "current_stop_name")
            val currentStopName: String? = null,
            @Json(name = "current_stop_number")
            val currentStopNumber: String? = null,
            @Json(name = "current_stop_scheduled_departure")
            val currentStopScheduledDeparture: String? = null,
            @Json(name = "destination_name")
            val destinationName: String? = null,
            @Json(name = "door")
            val door: Boolean? = null,
            @Json(name = "gps_course")
            val gpsCourse: String? = null,
            @Json(name = "gps_latitude")
            val gpsLatitude: String? = null,
            @Json(name = "gps_longitude")
            val gpsLongitude: String? = null,
            @Json(name = "last_stop_name")
            val lastStopName: String? = null,
            @Json(name = "last_stop_number")
            val lastStopNumber: String? = null,
            @Json(name = "line_direction")
            val lineDirection: String? = null,
            @Json(name = "line_name")
            val lineName: String? = null,
            @Json(name = "state_dtime")
            val stateDtime: String? = null,
            @Json(name = "time_difference")
            val timeDifference: String? = null,
            @Json(name = "vid")
            val vid: String? = null
    )
}
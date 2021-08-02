package project.main.api.model

import com.google.gson.annotations.SerializedName

data class TestModel(
    /** Arrival time of this route */
    @SerializedName("ended_on")
    var endedOn: Long = 0,
    /** Estimated time of this route (unit: minute) */
    @SerializedName("estimated_time")
    var estimatedTime: Int = 0,
    /** The fare status,（1=All segments have tickets, 2=Partial tickets, 3=No tickets) */
    @SerializedName("fare_status")
    var fareStatus: Int = 0,
    /**  */
    @SerializedName("mode")
    var mode: String = "",
    /** Departure time of this route */
    @SerializedName("started_on")
    var startedOn: Long = 0,
    @SerializedName("steps")
    var steps: List<Step> = listOf(),
    /** Suggested total price for this route */
    @SerializedName("total_price")
    var totalPrice: Float = 0f,
    /** The trip detail uuid */
    @SerializedName("trip_detail_uuid")
    var tripDetailUuid: String = ""
) {
    data class Step(
        /** How long will the next bus come  */
        @SerializedName("arrive")
        var arrive: Int = 0,
        /** Delay time of the ride (unit: minute) */
        @SerializedName("delay")
        var delay: Int = 0,
        /** The parking lot other information */
        @SerializedName("destination_lat")
        var destinationLat: Double = 0.0,
        /** The parking lng other information */
        @SerializedName("destination_lng")
        var destinationLng: Double = 0.0,
        /** The destination name */
        @SerializedName("destination_name")
        var destinationName: String = "",
        /** The destination station number */
        @SerializedName("destination_no")
        var destinationNo: String = "",
        /** The steps distance (unit: meter) */
        @SerializedName("distance")
        var distance: Int = 0,
        /** Arrival time of the ride */
        @SerializedName("ended_on")
        var endedOn: Long = 0,
        /** Estimated time of this steps (unit: second) */
        @SerializedName("estimated_time")
        var estimatedTime: Int = 0,
        /** If true is equal to starting station, it must show arrive */
        @SerializedName("firststop")
        var firststop: Boolean = false,
        /** Is it a Puyuma=true; Is not a Puyuma=false */
        @SerializedName("is_puyuma")
        var isPuyuma: Boolean = false,
        /** 0=Not provided (Fuxing), 1=Buy tickets now, 2=Ticket sales have not started, 3=Tickets are sold out */
        @SerializedName("is_ticket")
        var isTicket: Int = 0,
        /** The steps traval mode (options: "train", "tram", "bus", "driving", "cycling", "walking", "subway") */
        @SerializedName("mode")
        var mode: String = "",
        /**  */
        @SerializedName("mode_type")
        var modeType: String = "",
        /** Number of stations */
        @SerializedName("num_stops")
        var numStops: Int = 0,
        /** The parking lot other information */
        @SerializedName("origin_lat")
        var originLat: Double = 0.0,
        /** The parking lng other information */
        @SerializedName("origin_lng")
        var originLng: Double = 0.0,
        /** The origin name */
        @SerializedName("origin_name")
        var originName: String = "",
        /** The origin station number */
        @SerializedName("origin_no")
        var originNo: String = "",
        /** The roadmap for this route */
        @SerializedName("polyline")
        var polyline: String = "",
        /** The price for this ride */
        @SerializedName("price")
        var price: Float = 0f,
        /** If mode = 'bus' and is_ticket=1, use product_id to go to the ticket folder to find if there is a ticket */
        @SerializedName("product_id")
        var productId: String = "",
        /**  */
        @SerializedName("product_name")
        var productName: String = "",
        /**  */
        @SerializedName("ringtong_setting")
        var ringtongSetting: List<Any> = listOf(),
        /** The name of the ride */
        @SerializedName("short_name")
        var shortName: String = "",
        /** Number of the ride  */
        @SerializedName("short_name_no")
        var shortNameNo: String = "",
        /** Departure time of the ride */
        @SerializedName("started_on")
        var startedOn: Long = 0,
        /** Stop in the middle */
        @SerializedName("station_arr")
        var stationArr: List<Any> = listOf(),
        /** Detail of the steps */
        @SerializedName("steps_detail")
        var stepsDetail: List<StepsDetail> = listOf(),
        /**  */
        @SerializedName("ticket_status")
        var ticketStatus: String = "",
        /**  */
        @SerializedName("ticket_uuid")
        var ticketUuid: String = ""
    )

    data class StepsDetail(
        @SerializedName("arrival_stop")
        var arrivalStop: ArrivalStop = ArrivalStop(),
        @SerializedName("arrival_time")
        var arrivalTime: ArrivalTime = ArrivalTime(),
        @SerializedName("building_level")
        var buildingLevel: BuildingLevel = BuildingLevel(),
        @SerializedName("departure_stop")
        var departureStop: DepartureStop = DepartureStop(),
        @SerializedName("departure_time")
        var departureTime: DepartureTime = DepartureTime(),
        @SerializedName("distance")
        var distance: Distance = Distance(),
        @SerializedName("duration")
        var duration: Duration = Duration(),
        @SerializedName("end_location")
        var endLocation: Location = Location(),
        @SerializedName("headsign")
        var headsign: String = "",
        @SerializedName("html_instructions")
        var htmlInstructions: String = "",
        @SerializedName("line")
        var line: Line = Line(),
        @SerializedName("maneuver")
        var maneuver: String = "",
        @SerializedName("num_stops")
        var numStops: Int = 0,
        @SerializedName("polyline")
        var polyline: Polyline = Polyline(),
        @SerializedName("start_location")
        var startLocation: Location = Location(),
        /** Which travel mode is this (options: 2=public transit, 5=intermodal) */
        @SerializedName("travel_mode")
        var travelMode: String = "",
        @SerializedName("trip_short_name")
        var tripShortName: String = ""
    )

    data class ArrivalStop(
        @SerializedName("location")
        var location: Location = Location(),
        @SerializedName("name")
        var name: String = ""
    )

    data class ArrivalTime(
        @SerializedName("text")
        var text: String = "",
        @SerializedName("time_zone")
        var timeZone: String = "",
        @SerializedName("value")
        var value: Int = 0
    )

    data class BuildingLevel(
        @SerializedName("number")
        var number: Int = 0
    )

    data class DepartureStop(
        @SerializedName("location")
        var location: Location = Location(),
        @SerializedName("name")
        var name: String = ""
    )

    data class DepartureTime(
        @SerializedName("text")
        var text: String = "",
        @SerializedName("time_zone")
        var timeZone: String = "",
        @SerializedName("value")
        var value: Int = 0
    )

    data class Distance(
        @SerializedName("text")
        var text: String = "",
        @SerializedName("value")
        var value: Int = 0
    )

    data class Duration(
        @SerializedName("text")
        var text: String = "",
        @SerializedName("value")
        var value: Int = 0
    )

    data class Line(
        @SerializedName("agencies")
        var agencies: List<Agency> = listOf(),
        @SerializedName("color")
        var color: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("short_name")
        var shortName: String = "",
        @SerializedName("text_color")
        var textColor: String = "",
        @SerializedName("url")
        var url: String = "",
        @SerializedName("vehicle")
        var vehicle: Vehicle = Vehicle()
    )

    data class Polyline(
        @SerializedName("points")
        var points: String = ""
    )

    data class Location(
        @SerializedName("lat")
        var lat: Double = 0.0,
        @SerializedName("lng")
        var lng: Double = 0.0
    )

    data class Agency(
        @SerializedName("name")
        var name: String = "",
        @SerializedName("phone")
        var phone: String = "",
        @SerializedName("url")
        var url: String = ""
    )

    data class Vehicle(
        @SerializedName("icon")
        var icon: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("type")
        var type: String = ""
    )
}
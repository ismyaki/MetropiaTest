package project.main.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * sno(站點代號)、sna(中文場站名稱)、tot(場站總停車格)、
 * sbi(可借車位數)、sarea(中文場站區域)、mday(資料更新時間)、
 * lat(緯度)、lng(經度)、ar(中文地址)、
 * sareaen(英文場站區域)、snaen(英文場站名稱)、aren(英文地址)、
 * bemp(可還空位數)、act(場站是否暫停營運)
 * */
@Entity(tableName = "BikeStation")
data class BikeStationEntity(
    @PrimaryKey()
    @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "address")
    var address: String = "",
    @ColumnInfo(name = "area")
    var area: String = "",
    @ColumnInfo(name = "e_name")
    var e_name: String = "",
    @ColumnInfo(name = "e_address")
    var e_address: String = "",
    @ColumnInfo(name = "e_area")
    var e_area: String = "",

    @ColumnInfo(name = "open")
    var open: Int = 1,

    @ColumnInfo(name = "total")
    var total: Int = 0,
    @ColumnInfo(name = "available")
    var available: Int = 0,
    @ColumnInfo(name = "empty")
    var empty: Int = 0,
    @ColumnInfo(name = "lat")
    var lat: Double = 0.0,
    @ColumnInfo(name = "lng")
    var lng: Double = 0.0,

    @ColumnInfo(name = "city")
    var city: String = "", // 台北, 新北, ...
    @ColumnInfo(name = "system")
    var system: String = "", // youbike, ibike, tbike
    @ColumnInfo(name = "update_time")
    var update_time: Long = 0
){
    constructor() : this("")
}
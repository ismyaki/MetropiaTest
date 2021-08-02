package project.main.api.model

import com.google.gson.annotations.SerializedName

/**
 * 新北市
 * sno(站點代號)、sna(中文場站名稱)、tot(場站總停車格)、
 * sbi(可借車位數)、sarea(中文場站區域)、mday(資料更新時間)、
 * lat(緯度)、lng(經度)、ar(中文地址)、
 * sareaen(英文場站區域)、snaen(英文場站名稱)、aren(英文地址)、
 * bemp(可還空位數)、act(場站是否暫停營運)
 * */
data class NewTaipeiYouBikeStationModel(
    @SerializedName("sno")
    var sno: String = "",
    @SerializedName("sna")
    var sna: String = "",
    @SerializedName("tot")
    var tot: String = "",
    @SerializedName("sbi")
    var sbi: String = "",
    @SerializedName("sarea")
    var sarea: String = "",
    @SerializedName("mday")
    var mday: String = "",
    @SerializedName("lat")
    var lat: String = "",
    @SerializedName("lng")
    var lng: String = "",
    @SerializedName("ar")
    var ar: String = "",
    @SerializedName("sareaen")
    var sareaen: String = "",
    @SerializedName("snaen")
    var snaen: String = "",
    @SerializedName("aren")
    var aren: String = "",
    @SerializedName("bemp")
    var bemp: String = "",
    @SerializedName("act")
    var act: String = ""
)
package project.main.api.model

import com.google.gson.annotations.SerializedName

/**
 * 台南 TBike
 * https://quality.data.gov.tw/dq_download_json.php?nid=53909&md5_url=85771848d4534bcab2a1c69983c52092
 * T-Bike 臺南市公共自行車租賃站資訊
 * Id(編號)、StationName(站名)、Address(地址)、
 * Capacity(格位數)、AvaliableBikeCount(可借車輛數)、AvaliableSpaceCount(可停空位數)、
 * UpdateTime(更新時間)、Latitude(緯度)、Longitude(經度)
 * */
//{
//    "Id": "3",
//    "StationName": "保安轉運站",
//    "Address": "保安轉運站公車侯車亭旁 (文賢路一段)",
//    "EnglishStationName": "Bao'an Bus Station",
//    "EnglishAddress": "Besides the shelter of Bao’an Bus Station (on Wenxian Rd. Section 1)",
//    "Capacity": "32",
//    "AvaliableBikeCount": "5",
//    "AvaliableSpaceCount": "27",
//    "UpdateTime": "2020-11-12T15:54:07.173",
//    "Latitude": "22.932706",
//    "Longitude": "120.230637",
//    "District": "仁德區"
//},
data class TainanBikeStationModel(
    @SerializedName("Id")
    var id: String = "",
    @SerializedName("StationName")
    var stationName: String = "",
    @SerializedName("Address")
    var address: String = "",
    @SerializedName("EnglishStationName")
    var englishStationName: String = "",
    @SerializedName("EnglishAddress")
    var englishAddress: String = "",
    @SerializedName("Capacity")
    var capacity: String = "",
    @SerializedName("AvaliableBikeCount")
    var avaliableBikeCount: String = "",
    @SerializedName("AvaliableSpaceCount")
    var avaliableSpaceCount: String = "",
    @SerializedName("UpdateTime")
    var updateTime: String = "",
    @SerializedName("Latitude")
    var latitude: String = "",
    @SerializedName("Longitude")
    var longitude: String = "",
    @SerializedName("District")
    var district: String = ""
) {

}


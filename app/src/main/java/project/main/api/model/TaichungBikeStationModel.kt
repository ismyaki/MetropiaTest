package project.main.api.model

import com.google.gson.annotations.SerializedName

/**
 * 台中 iBike
 * https://quality.data.gov.tw/dq_download_json.php?nid=84502&md5_url=80160bfb1234ac4be1d1ed9cd43b10be
 * ID、Position、EName、X、Y、CArea、EArea、CAddress、EAddress、AvailableCNT、EmpCNT、UpdateTime
 * */
//{
//    "ID": "3001",
//    "Position": "逢甲大學",
//    "EName": "Feng Chia University",
//    "X": "120.645",
//    "Y": "24.1787",
//    "CArea": "西屯區",
//    "EArea": "Xitun Dist.",
//    "CAddress": "台中市福星路/逢甲路口(潮洋機車停車場)",
//    "EAddress": "Fuxing Rd & Fengjia Rd Intersection",
//    "AvailableCNT": "52",
//    "EmpCNT": "44",
//    "UpdateTime": "2020-05-20 10:39:35"
//},
data class TaichungBikeStationModel(
    @SerializedName("ID")
    var id: String = "",
    @SerializedName("Position")
    var position: String = "",
    @SerializedName("EName")
    var eName: String = "",
    @SerializedName("CArea")
    var cArea: String = "",
    @SerializedName("X")
    var x: String = "",
    @SerializedName("Y")
    var y: String = "",
    @SerializedName("EArea")
    var eArea: String = "",
    @SerializedName("CAddress")
    var cAddress: String = "",
    @SerializedName("EAddress")
    var eAddress: String = "",
    @SerializedName("AvailableCNT")
    var availableCNT: String = "",
    @SerializedName("EmpCNT")
    var empCNT: String = "",
    @SerializedName("UpdateTime")
    var updateTime: String = ""
) {

}


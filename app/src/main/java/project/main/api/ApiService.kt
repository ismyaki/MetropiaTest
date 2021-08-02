package project.main.api

import project.main.api.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    /** 台北市youbike */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("blobyoubike/YouBikeTP.json")
    fun getTaipeiBikeStation(): Call<TaipeiYouBikeStationModel>

    /** 新北市youbike */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("71CD1490-A2DF-4198-BEF1-318479775E8A/json?page=0&size=${Int.MAX_VALUE}")
    fun getNewTaipeiBikeStation(): Call<List<NewTaipeiYouBikeStationModel>>

    /** 桃園youbike */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("download?id=5ca2bfc7-9ace-4719-88ae-4034b9a5a55c&rid=a1b4714b-3b75-4ff8-a8f2-cc377e4eaa0f")
    fun getTaoyuanBikeStation(): Call<TaoyuanYouBikeStationModel>

    /** 台中iBike */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("dq_download_json.php?nid=84502&md5_url=80160bfb1234ac4be1d1ed9cd43b10be")
    fun getTaichungBikeStation(): Call<List<TaichungBikeStationModel>>

    /** 台南Tbike */
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("dq_download_json.php?nid=53909&md5_url=85771848d4534bcab2a1c69983c52092")
    fun getTainanBikeStation(): Call<List<TainanBikeStationModel>>
}
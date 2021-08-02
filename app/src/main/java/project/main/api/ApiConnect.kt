package project.main.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

enum class ApiServer(val url: String) {
    /** 台北市 */
    TAIPEI("https://tcgbusfs.blob.core.windows.net/"),
    /** 新北市 */
    NEW_TAIPEI("https://data.ntpc.gov.tw/api/datasets/"),
    /** 桃園 */
    TAOYUAN("https://data.tycg.gov.tw/opendata/datalist/datasetMeta/"),
    /** 台中 */
    TAICHUNG("https://quality.data.gov.tw/"),
    /** 台南 */
    TAINAN("https://quality.data.gov.tw/")
}
object ApiConnect {
    private const val URL = "https://tcgbusfs.blob.core.windows.net/"
//    private var apiService: ApiService? = null
    private var apiService = mutableMapOf<String, ApiService>()

    fun getService(server: ApiServer): ApiService {
//        if (apiService == null) {
//            apiService = init(server)
//        }
//        return apiService ?: init(server)
        apiService[server.url] = apiService[server.url] ?: init(server)
        return apiService[server.url] ?: init(server)
    }

    private fun init(server: ApiServer): ApiService {
        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(120,TimeUnit.SECONDS)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(server.url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        return retrofit.create(ApiService::class.java)
    }
}
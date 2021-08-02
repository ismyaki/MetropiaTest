package project.main.tools

import okhttp3.ResponseBody
import retrofit2.Response


enum class ErrorType {
    TIME_OUT,
    STREAM,
    CONNECT,
    OTHER,
}
class ResponseError(val code: Int, val responseBody: ResponseBody?, val type: ErrorType, val e: Exception?)
fun <T> Response<T>.getError(type: ErrorType = ErrorType.OTHER): ResponseError? {
    if (errorBody() != null){
        return ResponseError(code(), errorBody(), type, null)
    }
    return null
}
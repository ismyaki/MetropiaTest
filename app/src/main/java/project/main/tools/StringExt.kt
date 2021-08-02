package project.main.tools

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

inline fun <reified T>String.toGsonData(): T? {
    var data: T? = null
    try {
        data = Gson().fromJson(this, T::class.java)
    } catch (e: JsonSyntaxException) {
        e.printStackTrace()
    }
    return data
}

inline fun <reified T>String.toGsonDataNonNull(): T {
    return Gson().fromJson(this, T::class.java)
}
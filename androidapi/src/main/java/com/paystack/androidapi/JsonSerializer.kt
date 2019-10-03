package com.paystack.androidapi

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException
import java.lang.reflect.Type

internal class JsonSerializer private constructor(private val moshi: Moshi) {

    fun <T> serializeToJson(type: Class<T>, kotlinObject: T): String {
        val jsonAdapter = moshi.adapter(type)
        return jsonAdapter.toJson(kotlinObject)
    }

    @Throws(IOException::class, JsonDataException::class)
    fun <T> serializeFromJson(type: Type, jsonString: String): T? {
        val jsonAdapter = moshi.adapter<T>(type)
        return jsonAdapter.fromJson(jsonString)
    }

    companion object {
        private var instance: JsonSerializer? = null
        fun getInstance(): JsonSerializer {
            if (instance == null) {
                val moshi = Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                instance = JsonSerializer(moshi)
            }
            return instance!!
        }
    }
}
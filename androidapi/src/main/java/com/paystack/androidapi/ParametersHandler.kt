package com.paystack.androidapi

import io.reactivex.Single
import java.util.*

/**
 * @author Oloruntobi Allen
 *
 * */
class ParametersHandler private constructor(private val serializer: JsonSerializer) {

    /**
     * Convert Parameters json String to a Case Insensitive Tree Map
     * @return A Single of the case insensitive tree map
     * */
    fun getParameters(jsonString: String): Single<TreeMap<String, String>> {
        return Single.create { emitter ->
            val settingsMap = serializer.serializeFromJson<Map<String, String>>(Map::class.java, jsonString)
            if (settingsMap == null) {
                emitter.onError(SerializationError())
                return@create
            }
            val settingsTreeMap = TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER)
            for ((key, value) in settingsMap.entries) {
                settingsTreeMap[key] = value
            }

            emitter.onSuccess(settingsTreeMap)
        }
    }

    companion object {
        private var instance: ParametersHandler? = null

        /**
         * @return Instance of the class ParametersHandler
        * */
        fun getInstance(): ParametersHandler {
            if (instance == null) {
                val serializer = JsonSerializer.getInstance()
                instance = ParametersHandler(serializer)
            }
            return instance!!
        }
    }
}
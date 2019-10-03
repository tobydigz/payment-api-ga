package com.paystack.androidapi

import com.squareup.moshi.Json

class SerializationError() : Throwable()

data class CardTransaction(
        @Json(name = "statuscode") val statuscode: String,
        @Json(name = "message") val message: String,
        @Json(name = "terminalID") val terminalID: String,
        @Json(name = "rrn") val rrn: String,
        @Json(name = "stan") val stan: String,
        @Json(name = "amount") val amount: String,
        @Json(name = "datetime") val datetime: String,
        @Json(name = "authcode") val authcode: String = "",
        @Json(name = "cardHolderName") val cardHolderName: String,
        @Json(name = "maskedPan") val maskedPan: String,
        @Json(name = "appLabel") val appLabel: String,
        @Json(name = "cardExpireDate") val cardExpireDate: String,
        @Json(name = "aid") val aid: String,
        @Json(name = "nuban") val nuban: String = "",
        @Json(name = "pinType") val pinType: String = "",
        @Json(name = "statusDescription") val statusDescription: String = ""
)

data class CardTransactionError(
        @Json(name = "message") val message: String,
        @Json(name = "statuscode") val statuscode: String
)

data class Receipt(@Json(name = "Receipt") val printFields: List<PrintField>)

data class PrintField(
        @Json(name = "Bitmap") val filename: String = "",
        @Json(name = "letterSpacing") val letterSpacing: Int = 5,
        @Json(name = "String") val stringFields: List<StringField> = ArrayList()
)

data class StringField(
        @Json(name = "isMultiline") val isMultiline: Boolean = false,
        @Json(name = "isQR") val isQR: Boolean = false,
        @Json(name = "qrString") val qrString: String = "",
        @Json(name = "header") val header: TextField = TextField(),
        @Json(name = "body") val body: TextField = TextField()
)

data class TextField(
        @Json(name = "text") val text: String = "",
        @Json(name = "align") val align: String? = null,
        @Json(name = "size") val size: String? = null,
        @Json(name = "isBold") val isBold: Boolean? = null
)
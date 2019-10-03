package com.paystack.androidapi

import android.app.Activity
import android.content.Intent

/**
 * @author Oloruntobi Allen
* */
class Operations {
    companion object {
        private const val PRINTER_INTENT = "com.globalaccelerex.printer"
        private const val KEY_EXCHANGE_INTENT = "com.globalaccelerex.keyexchange"
        private const val SETTINGS_INTENT = "com.globalaccelerex.settings"
        private const val PURCHASE_INTENT = "com.globalaccelerex.transaction"
        private const val PARAMETER_INTENT = "com.globalaccelerex.utility"
        private const val REPRINT_INTENT = "com.globalaccelerex.reprint"

        /**
         * Get the Intent for a plain purchase
         * @param amount Amount to pay
         * @param print Determine if the POS should print a receipt automatically
         * @return Purchase Intent
        * */
        fun getPurchaseIntent(amount: Double, print: Boolean) = Intent(PURCHASE_INTENT)
                .putExtra("requestData", "{ \"transType\": \"PURCHASE\", \"amount\":\"$amount\", \"print\":\"$print\" }")

        /**
         * Get the Intent for a cashback purchase
         * @param amount Amount to pay
         * @param print Determine if the POS should print a receipt automatically
         * @return Purchase Intent
         * */
        fun getPurchaseCashBackIntent(amount: Double, print: Boolean) = Intent(PURCHASE_INTENT).apply {
            putExtra("requestData", "{\"transType\":\"PURCHASEWITHCB\", \"$amount\":\"$amount\", \"print\": \"$print\"}")
        }

        /**
         * Get the Intent for card balance
         * @return Card Balance Intent
         * */
        fun getBalanceIntent() = Intent(PURCHASE_INTENT).apply {
            putExtra("requestData", "{\"transType\":\"BALANCE\", \"amount\":\"2.00\", \"print\": \"true\"}")
        }

        /**
         * Get the Intent for  retrieving parameters
         * @return Get Parameters Intent
         * */
        fun getParameterIntent() = Intent(PARAMETER_INTENT).apply {
            putExtra("requestData", "{  \"action\":\"PARAMETER\"  }")
        }

        /**
         * Get the Intent for printing
         * @param printData Json data conforming to the {@link Receipt}
         * @return Print Intent
         * */
        fun getPrinterIntent(printData: String) = Intent(PRINTER_INTENT).apply {
            putExtra("jsonData", printData)
        }

        /**
         * Get the Intent for reprint
         * @return Reprint Intent
         * */
        fun getReprintIntent() = Intent(REPRINT_INTENT)

        /**
         * Get the Intent for keyexchange
         * @return KeyExchange Intent
         * */
        fun getKeyExchangeIntent() = Intent(KEY_EXCHANGE_INTENT)

        /**
         * Get the Intent for settings
         * @return Settings Intent
         * */
        fun getSettingsIntent() = Intent(SETTINGS_INTENT)

        /**
         * Get the Intent for settings
         * @param resultCode from {}
         * @return Settings Intent
         * */
        fun handlePurchaseResponse(resultCode: Int, data: Intent?, error: (status: String, message: String) -> Unit, success: (status: String, jsonData: String) -> Unit) {
            var status = "03"
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    error(status, getStatusMessage(status))
                    return
                }

                status = data.extras?.getString("status") ?: "03"
                val jsonData = data.extras?.getString("data") ?: ""

                if (status != "00" && status != "02") {
                    error(status, getStatusMessage(status))
                    return
                }

                if (jsonData.isEmpty()) {
                    error("99", getStatusMessage("99"))
                    return
                }

                success(status, jsonData)
                return
            }
            error(status, getStatusMessage(status))
        }

        fun handleParameterResponse(resultCode: Int, data: Intent?, error: (status: String, message: String) -> Unit, success: (status: String, jsonData: String) -> Unit) {
            var status = "09"
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    error(status, getStatusMessage(status))
                    return
                }

                status = data.extras?.getString("status") ?: "02"
                val jsonData = data.extras?.getString("data") ?: ""

                if ((status == "00") && jsonData.isNotEmpty()) {
                    success(status, jsonData)
                    return
                }
            }
            error(status, getStatusMessage(status))
        }

        fun handleGenericResponse(resultCode: Int, data: Intent?, error: (status: String, message: String) -> Unit, success: () -> Unit) {
            var status = "09"
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    error(status, getStatusMessage(status))
                    return
                }
                status = data.extras?.getString("status") ?: "09"

                if (status.toLowerCase() == "00") {
                    success()
                    return
                }
            }
            error(status, getStatusMessage(status))
        }

        fun getStatusMessage(status: String) = when (status) {
            "02" -> "Failed"
            "03" -> "Transaction Cancelled"
            "04" -> "Invalid Format"
            "06" -> "Transaction Timed out"
            "09" -> "Activity Cancelled"
            "99" -> "An Error Occurred"
            else -> ""
        }
    }
}
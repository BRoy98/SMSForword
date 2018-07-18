package sms.forword.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast

class SmsReceiver : BroadcastReceiver() {

    lateinit var sharedpreferences: SharedPreferences

    internal var mypreference = "mypref"
    internal var pref_number = "PREF_NUM"
    internal var pref_words = "PREF_WORDS"
    internal var pref_search_type = "SRCH_TYPE"

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            var smsSender = ""
            var smsBody = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    smsSender = smsMessage.displayOriginatingAddress
                    smsBody += smsMessage.messageBody
                }
            } else {
                val smsBundle = intent.extras
                if (smsBundle != null) {
                    val pdus = smsBundle.get("pdus") as Array<Any>
                    if (pdus == null) {
                        // Display some error to the user
                        Log.e(TAG, "SmsBundle had no pdus key")
                        return
                    }
                    val messages = arrayOfNulls<SmsMessage>(pdus.size)
                    for (i in messages.indices) {
                        messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        smsBody += messages[i]?.messageBody
                    }
                    smsSender = messages[0]!!.originatingAddress

                    Log.i(TAG, smsSender)
                }
            }

            Log.i(TAG, "$smsSender : $smsBody")

            sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE)
            if (sharedpreferences.contains(pref_number)) {
                if (sharedpreferences.contains(pref_words)) {
                    if (sharedpreferences.contains(pref_search_type)) {

                        Log.i(TAG, "${sharedpreferences.getString(pref_number, "")} " +
                                "${sharedpreferences.getString(pref_words, "")} " +
                                "${sharedpreferences.getInt(pref_search_type, -1)}")

                        when (sharedpreferences.getInt(pref_search_type, -1)) {
                            0 -> {
                                Toast.makeText(context, "Sending 0...", Toast.LENGTH_LONG).show()
                                val searchWords = sharedpreferences.getString(pref_words, "")
                                val arrOfWords = searchWords.split(",")
                                val wordSize = arrOfWords.size

                                var foundAll = true
                                var wordCount = 0

                                for (i in arrOfWords) {
                                    if (!smsBody.contains(i))
                                        foundAll = false
                                    wordCount++
                                    Log.i(TAG, "$wordCount : $wordSize : $foundAll")
                                    if (wordSize == wordCount && foundAll) {
                                        SmsManager.getDefault().sendTextMessage(sharedpreferences.getString(pref_number, ""),
                                                null, smsBody, null, null)
                                    }
                                }
                            }
                            1 -> {
                                Toast.makeText(context, "Sending 1...", Toast.LENGTH_LONG).show()
                                val searchWords = sharedpreferences.getString(pref_words, "")
                                val arrOfWords = searchWords.split(",")

                                for (i in arrOfWords) {
                                    if (!smsBody.contains(i))
                                        SmsManager.getDefault().sendTextMessage(sharedpreferences.getString(pref_number, ""),
                                                null, smsBody, null, null)
                                }
                            }
                        }

                        //SmsManager.getDefault().sendTextMessage(sharedpreferences.getString(pref_number, ""), null, smsBody, null, null)
                    }
                }
            }

            /*if (smsBody.contains("OTP") && smsBody.contains("Aadhaar")) {
                Log.i(TAG, "Sending SMS...")
                SmsManager.getDefault().sendTextMessage("+917278872669", null, smsBody, null, null)
            }*/

        }
    }

    companion object {

        private val TAG = "SmsBroadcastReceiver"

        //interface
        private var mListener: SmsListener? = null

        fun bindListener(listener: SmsListener) {
            mListener = listener
        }
    }
}
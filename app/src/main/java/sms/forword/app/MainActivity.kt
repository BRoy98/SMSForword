package sms.forword.app

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.buttonSave
import kotlinx.android.synthetic.main.activity_main.input_number
import kotlinx.android.synthetic.main.activity_main.input_words
import kotlinx.android.synthetic.main.activity_main.type_radio_group
import android.content.SharedPreferences
import android.provider.ContactsContract.CommonDataKinds.Email
import android.R.id.edit
import android.widget.RadioButton


class MainActivity : AppCompatActivity() {

    lateinit var sharedpreferences: SharedPreferences

    val mypreference = "mypref"
    val pref_number = "PREF_NUM"
    val pref_words = "PREF_WORDS"
    val pref_search_type = "SRCH_TYPE"
    val pref_search_id = "SRCH_ID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedpreferences = getSharedPreferences (mypreference, Context.MODE_PRIVATE)

        ReadData()

        buttonSave.setOnClickListener {
            if(!input_number.text.isEmpty()) {
                if(!input_words.text.isEmpty()) {
                    if(type_radio_group.checkedRadioButtonId != -1) {
                        SaveData()
                    }
                } else {
                    Toast.makeText(this, "Please enter at least one word to check", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please enter a phone number with country code", Toast.LENGTH_LONG).show()
            }
        }

        val i = Intent(this, MySmsService::class.java)
        startService(i)

        //registerReceiver(SmsReceiver(), IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
        /*SmsReceiver.bindListener { messageSender, messageText ->
            Log.i("SMS Forword", "$messageSender : $messageText")
        }*/
    }

    fun ReadData() {
        if(sharedpreferences.contains(pref_number)) {
            input_number.setText(sharedpreferences.getString(pref_number, ""))
        }
        if(sharedpreferences.contains(pref_words)) {
            input_words.setText(sharedpreferences.getString(pref_words, ""))
        }
        if(sharedpreferences.contains(pref_search_type)) {
            type_radio_group.check(sharedpreferences.getInt(pref_search_id, -1))
        }
    }

    fun SaveData() {
        val num = input_number.text.toString()
        val words = input_words.text.toString()
        var searchType = -1
        var searchId = type_radio_group.checkedRadioButtonId

        val radioButton = type_radio_group.findViewById(type_radio_group.checkedRadioButtonId) as RadioButton
        when (radioButton.text.toString()) {
            "If found all" ->
                searchType = 0
            "If found any" ->
                searchType = 1
        }

        val editor = sharedpreferences.edit()
        editor.putString(pref_number, num)
        editor.putString(pref_words, words)
        editor.putInt(pref_search_type, searchType)
        editor.putInt(pref_search_id, searchId)
        editor.apply()
        Toast.makeText(this, "Data saved!", Toast.LENGTH_LONG).show()
    }
}

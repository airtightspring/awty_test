package fadingtextview.kjerauld.washington.edu.awty

import android.app.AlarmManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.TimePicker
import android.app.PendingIntent
import android.content.Intent
import android.R.string.cancel


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val start: Button = findViewById(R.id.button)
        val message: EditText = findViewById(R.id.editText)
        val phone_number: EditText = findViewById(R.id.editText2)
        val frequency: EditText = findViewById(R.id.editText3)

        val errorText: Button = findViewById(R.id.button2)

        var message_content = ""
        var message_truth = false

        var phone_number_content = ""
        var phone_truth = false

        var frequency_content = 0
        var frequency_truth = false

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val timePicker: TimePicker

        errorText.text = whatIsWrong(message_truth, phone_truth, frequency_truth)

        start.setEnabled(false)

        message.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val content = message.getText().toString()
                message_content = content
                if(content != "") {
                    message_truth = true
                } else {
                    message_truth = false
                }

                if(message_truth && phone_truth && frequency_truth) {
                    start.setEnabled(true)
                    errorText.setEnabled(false)
                } else {
                    start.setEnabled(false)
                    errorText.setEnabled(true)
                }
            }
        })

        phone_number.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(phone_number.getText().toString().length == 10) {
                    phone_truth = true
                    phone_number_content = phone_number.getText().toString()
                } else {
                    phone_truth = false
                }

                if(message_truth && phone_truth && frequency_truth) {
                    start.setEnabled(true)
                    errorText.setEnabled(false)
                } else {
                    start.setEnabled(false)
                    errorText.setEnabled(true)
                }
            }
        })

        frequency.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (frequency.getText().toString().matches((("^0").toRegex())) ) {
                    frequency.setText("");
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if(frequency.getText().isNotEmpty()) {
                    frequency_truth = true
                    frequency_content = frequency.getText().toString().toInt()
                } else {
                    frequency_truth = false
                }

                if(message_truth && phone_truth && frequency_truth) {
                    start.setEnabled(true)
                    errorText.setEnabled(false)
                } else {
                    start.setEnabled(false)
                    errorText.setEnabled(true)
                }
            }
        })



        start.text = "Start"
        start.setOnClickListener(View.OnClickListener {
            if(start.text == "Start") {
                start.text = "Stop"
                val phoneNumberHolder = makePhoneNumber(phone_number_content)
                if(phoneNumberHolder != "Invalid Phone Number") {
                    val toastHolder: String = phoneNumberHolder + ": " + message_content

                    message.setEnabled(false)
                    phone_number.setEnabled(false)
                    frequency.setEnabled(false)

                    prefs.checkTimer = frequency_content.toLong()
                    prefs.checkMessageString = toastHolder
                    setAlarm(true)
                }
            } else {
                start.text = "Start"

                message.setEnabled(true)
                phone_number.setEnabled(true)
                frequency.setEnabled(true)

                setAlarm(false)
            }
        })

        errorText.setOnClickListener(){
            Toast.makeText(this, whatIsWrong(message_truth, phone_truth, frequency_truth), Toast.LENGTH_LONG).show()
        }



    }

    fun makePhoneNumber(phoneNumber: String): String {
        var phoneNumberHolder = ""
        if(phoneNumber.length == 10) {
            phoneNumberHolder += "(" + phoneNumber.substring(0,3) + ") "
            phoneNumberHolder += phoneNumber.substring(3,6) + "-" + phoneNumber.substring(6, 10)
            return phoneNumberHolder
        } else {
            return "Invalid Phone Number"
        }
    }

    fun whatIsWrong(messageCheck: Boolean, phoneCheck: Boolean, freqCheck: Boolean): String {
        var returnCheck: String = "Needs Fixing: "
        if (messageCheck == false) {
            returnCheck += "Invalid message. "
        }

        if (phoneCheck == false) {
            returnCheck += "Invalid phone number. "
        }

        if (freqCheck == false) {
            returnCheck += "Invalid frequency time."
        }

        if (messageCheck && phoneCheck && freqCheck) {
            return ""
        } else {
            return returnCheck
        }
    }

    private fun setAlarm( status: Boolean) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, MyAlarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        //val triggerNumber = (60000 * prefs.checkTimer).toLong()
        if(status) {
            alarmManager.setRepeating(AlarmManager.RTC, 0, prefs.checkTimer, pendingIntent)
        } else {
            alarmManager.cancel(pendingIntent)
        }
    }
}

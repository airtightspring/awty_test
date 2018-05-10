package fadingtextview.kjerauld.washington.edu.awty

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

class MyAlarm : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        var logMessage = "No Message Set"

        if (ThereApp.prefs?.checkMessageString != null) {
            logMessage = ThereApp.prefs?.checkMessageString.toString()
        }
        Toast.makeText(context, logMessage, Toast.LENGTH_LONG).show()

        Log.d("AlarmLogTest", logMessage)
    }

}
package fadingtextview.kjerauld.washington.edu.awty

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

val prefs: Prefs by lazy {
    ThereApp.prefs!!
}

class ThereApp: Application() {

    companion object Companion {
        fun create(): ThereApp = ThereApp()

        var prefs: Prefs? = null
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
    }
}

class Prefs (context: Context) {
    val PREFS_FILENAME = "quizdroid.kjerauld.washington.edu.awty.prefs"
    val messageString = "Message String"
    val timer = "Timer Value"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var checkMessageString: String
        get() = prefs.getString(messageString, "")
        set(value) = prefs.edit().putString(messageString, value).apply()

    var checkTimer: Long
        get() = prefs.getLong(timer, 1 * 60000)
        set(value) = prefs.edit().putLong(timer, value * 60000).apply()
}
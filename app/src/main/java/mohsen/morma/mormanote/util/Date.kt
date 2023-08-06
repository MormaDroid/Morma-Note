package mohsen.morma.mormanote.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.util.Calendar

object Date {

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateDateAndTime(): String {
        val calender = Calendar.getInstance().time
        return DateFormat.getDateTimeInstance().format(calender)
    }


    fun calculateHour(): Int {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    }

}
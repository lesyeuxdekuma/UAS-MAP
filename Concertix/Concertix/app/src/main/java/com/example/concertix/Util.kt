package com.example.concertix

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.concertix.Util.parseDate
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Util {
    fun getCircularProgressDrawable(context: Context): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.setColorSchemeColors(R.color.orange)
        circularProgressDrawable.start()

        return circularProgressDrawable
    }

    fun formatCurrency(
        amount: Long
    ): String {
        amount.let {
            val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                .apply {
                    minimumFractionDigits = 0
                    maximumFractionDigits = 0
                }
            return formatter.format(
                amount
            )
        }
    }

    fun String?.parseDate(): Date {
        if (this.isNullOrEmpty()) return Date()
        return try {
            val date = SimpleDateFormat("yyyy-MM-dd").parse(this)

            val calendar = Calendar.getInstance()
            calendar.time = date!!
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)

            return calendar.time
        } catch (e: Exception) {
            Date()
        }
    }

    fun String?.formatDate(): String {
        val parsedDate = this.parseDate()
        return try {
            SimpleDateFormat("EEEE, dd MMM YYY").format(parsedDate)
        } catch (e: Exception) {
            ""
        }
    }
}
package com.astronout.testalgostudio.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.astronout.testalgostudio.BuildConfig
import es.dmoral.toasty.Toasty
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import timber.log.Timber
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

fun visible() = View.VISIBLE
fun invisible() = View.INVISIBLE
fun gone() = View.GONE

fun logDebug(message: String) {
    if (BuildConfig.DEBUG)
        Timber.d(message)
}

fun logError( message: String, throwable : Throwable? = null) {
    if (BuildConfig.DEBUG) {
        Timber.e(message)
    }
}

val cal = Calendar.getInstance()

val timeStampFormat = "yyyy-MM-dd"
val dateTimeFormat = "dd-MM-yyyy"

val completeDateFormat = "EEEE, d MMMM yyyy"
val dateOnlyFormat = "d MMMM yyyy"
val dayOnlyFormat = "EEEE"

fun dialogDate(context: Context, dateResult: (dateResult: String) -> Unit) {
    val dateNow = DateTime.now()
    MaterialDialog(context).show {
        datePicker(maxDate = dateNow.toCalendar(Locale.getDefault())) { dialog, date ->
            val dateTime = DateTime(date)
            dateResult(dateTime.toString(timeStampFormat))
        }
    }
}

fun getDateFromString(date: String): DateTime {
    return DateTime.parse(date, DateTimeFormat.forPattern(timeStampFormat))
}

private val VALID_EMAIL_ADDRESS_REGEX: Pattern =
    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

fun isValidEmail(emailStr: String?): Boolean {
    val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr)
    return matcher.find().not()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showSuccessToasty(message: String) {
    Toasty.success(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Context.showErrorToasty(message: String) {
    Toasty.error(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Context.showInfoToasty(message: String) {
    Toasty.info(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Context.showWarningToasty(message: String) {
    Toasty.warning(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Activity.checkPermissionForLocation(context: Context, REQUEST_PERMISSION_LOCATION : Int): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            // Show the permission request
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSION_LOCATION)
            false
        }
    } else {
        true
    }
}

fun Activity.checkPermissionForStorage(context: Context, REQUEST_PERMISSION_STORAGE : Int): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            // Show the permission request
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_STORAGE)
            false
        }
    } else {
        true
    }
}
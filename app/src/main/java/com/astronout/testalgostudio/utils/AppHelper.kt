package com.astronout.testalgostudio.utils

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

object AppHelper {

    private val PASSWORD_POLICY = """Password should be minimum 8 characters long,
            |should contain at least one capital letter,
            |at least one small letter and
            |at least one number""".trimMargin()

//    fun isLogedIn(): Boolean {
//        val token = Prefs.token
//        return token.isNotEmpty()
//    }

    /**
     * Checks if the password is valid as per the following password policy.
     * Password should be minimum minimum 8 characters long.
     * Password should contain at least one number.
     * Password should contain at least one capital letter.
     * Password should contain at least one small letter.
     * Password should contain at least one special character.
     * Allowed special characters: "~!@#$%^&*()-_=+|/,."';:{}[]<>?"
     *
     * @param data - can be EditText or String
     * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
     * @return - true if the password is valid as per the password policy.
     */
    fun isValidPassword(data: Any, updateUI: Boolean = true): Boolean {
        val str = getText(data)
        var valid = true

        // Password policy check
        // Password should be minimum minimum 8 characters long
        if (str.length < 8) {
            valid = false
        }
        // Password should contain at least one number
        var exp = ".*[0-9].*"
        var pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
        var matcher = pattern.matcher(str)
        if (!matcher.matches()) {
            valid = false
        }

        // Password should contain at least one capital letter
        exp = ".*[A-Z].*"
        pattern = Pattern.compile(exp)
        matcher = pattern.matcher(str)
        if (!matcher.matches()) {
            valid = false
        }

        // Password should contain at least one small letter
        exp = ".*[a-z].*"
        pattern = Pattern.compile(exp)
        matcher = pattern.matcher(str)
        if (!matcher.matches()) {
            valid = false
        }

        // Password should contain at least one special character
        // Allowed special characters : "~!@#$%^&*()-_=+|/,."';:{}[]<>?"
//        exp = ".*[~!@#\$%\\^&*()\\-_=+\\|\\[{\\]};:'\",<.>/?].*"
//        pattern = Pattern.compile(exp)
//        matcher = pattern.matcher(str)
//        if (!matcher.matches()) {
//            valid = false
//        }

        // Set error if required
        if (updateUI) {
            val error: String? = if (valid) null else PASSWORD_POLICY
            setError(data, error)
        }

        return valid
    }

    private fun getText(data: Any): String {
        var str = ""
        if (data is EditText) {
            str = data.text.toString()
        } else if (data is String) {
            str = data
        }
        return str
    }

    private fun setError(data: Any, error: String?) {
        if (data is EditText) {
            if (data.parent.parent is TextInputLayout) {
                (data.parent.parent as TextInputLayout).error = error
            } else {
                data.error = error
            }
        }
    }

}
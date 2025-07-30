package com.example.zenhive.utils

object Validator {

    fun isValidLogin(email: String, password: String): Boolean {
        return email.contains("@") && password.length >= 6
    }

    fun isValidSignup(email: String, password: String, confirmPassword: String): Boolean {
        return isValidLogin(email, password) && password == confirmPassword
    }
}

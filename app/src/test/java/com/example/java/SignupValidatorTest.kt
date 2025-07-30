package com.example.java

import com.example.zenhive.utils.Validator
import org.junit.Assert.*
import org.junit.Test

class SignupValidatorTest {

    @Test
    fun validSignup_returnsTrue() {
        val result = Validator.isValidSignup("user@example.com", "password123", "password123")
        assertTrue(result)
    }

    @Test
    fun passwordMismatch_returnsFalse() {
        val result = Validator.isValidSignup("user@example.com", "password123", "wrongpass")
        assertFalse(result)
    }

    @Test
    fun invalidEmail_returnsFalse() {
        val result = Validator.isValidSignup("userexample.com", "password123", "password123")  // invalid email
        assertFalse(result)
    }

    @Test
    fun shortPassword_returnsFalse() {
        val result = Validator.isValidSignup("user@example.com", "123", "123")
        assertFalse(result)
    }
}

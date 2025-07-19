package com.example.zenhive.utils

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.InputStream
import java.security.MessageDigest

object CloudinaryUploader {
    private const val CLOUD_NAME = "dgodxmwbq"
    private const val API_KEY = "828591465494872 "
    private const val API_SECRET = "Z-w7NPnyzXogjmyu8DkroKhLmG4"
    private const val UPLOAD_URL = "https://api.cloudinary.com/v1_1/$CLOUD_NAME/image/upload"

    fun sha1(input: String): String {
        val md = MessageDigest.getInstance("SHA-1")
        val bytes = md.digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun uploadImage(
        context: Context,
        imageUri: Uri,
        preset: String = "zenhive_unsigned",
        cloudName: String = "dgodxmwbq"
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                val contentResolver = context.contentResolver
                val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
                val fileBytes = inputStream?.readBytes() ?: return@withContext null
                inputStream.close()

                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "file",
                        "profile.jpg",
                        fileBytes.toRequestBody("image/*".toMediaTypeOrNull())
                    )
                    .addFormDataPart("upload_preset", preset)
                    .build()

                val request = Request.Builder()
                    .url("https://api.cloudinary.com/v1_1/$cloudName/image/upload")
                    .post(requestBody)
                    .build()

                val client = OkHttpClient()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val url = org.json.JSONObject(responseBody ?: "{}").optString("secure_url", "")
                    url
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun uploadImageSigned(
        imageFile: File,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val timestamp = (System.currentTimeMillis() / 1000).toString()
            val paramsToSign = "timestamp=$timestamp$API_SECRET"
            val signature = sha1(paramsToSign)

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imageFile.name, imageFile.asRequestBody("image/*".toMediaTypeOrNull()))
                .addFormDataPart("api_key", API_KEY)
                .addFormDataPart("timestamp", timestamp)
                .addFormDataPart("signature", signature)
                .build()

            val request = Request.Builder()
                .url(UPLOAD_URL)
                .post(requestBody)
                .build()

            val client = OkHttpClient()
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    onSuccess(responseBody ?: "")
                } else {
                    onError("Upload failed with code: ${response.code}")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Upload failed")
            }
        }
    }
}

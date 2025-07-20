package com.example.zenhive.utils

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File

object CloudinaryUploader {

    private const val CLOUD_NAME = "dgodxmwbq"
    private const val API_KEY = "828591465494872"
    private const val API_SECRET = "Z-w7NPnyzXogjmyu8DkroKhLmG4"
    private const val UPLOAD_URL = "https://api.cloudinary.com/v1_1/$CLOUD_NAME/image/upload"

    private val client = OkHttpClient()

    suspend fun uploadImage(file: File): String = withContext(Dispatchers.IO) {
        try {
            Log.d("CloudinaryUploader", "Starting upload for file: ${file.absolutePath}")

            if (!file.exists()) {
                val error = "File does not exist: ${file.absolutePath}"
                Log.e("CloudinaryUploader", error)
                throw Exception(error)
            }

            if (file.length() == 0L) {
                val error = "File is empty: ${file.absolutePath}"
                Log.e("CloudinaryUploader", error)
                throw Exception(error)
            }

            Log.d("CloudinaryUploader", "File size: ${file.length()} bytes")

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file",
                    file.name,
                    file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                )
                .addFormDataPart("upload_preset", "ml_default")
                .addFormDataPart("cloud_name", CLOUD_NAME)
                .addFormDataPart("api_key", API_KEY)
                .build()

            val request = Request.Builder()
                .url(UPLOAD_URL)
                .post(requestBody)
                .build()

            Log.d("CloudinaryUploader", "Sending request to Cloudinary")

            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string()
                Log.d("CloudinaryUploader", "Response received: ${response.code} ${response.message}")
                Log.d("CloudinaryUploader", "Response body: $responseBody")

                if (!response.isSuccessful || responseBody == null) {
                    val error = "Upload failed: ${response.code} ${response.message}. Response: $responseBody"
                    Log.e("CloudinaryUploader", error)
                    throw Exception(error)
                }

                try {
                    val jsonResponse = JSONObject(responseBody)
                    val url = jsonResponse.getString("secure_url")
                    Log.d("CloudinaryUploader", "Successfully uploaded. URL: $url")
                    return@withContext url
                } catch (e: Exception) {
                    val error = "Failed to parse response: $responseBody"
                    Log.e("CloudinaryUploader", error, e)
                    throw Exception(error, e)
                }
            }
        } catch (e: Exception) {
            Log.e("CloudinaryUploader", "Image upload failed", e)
            throw Exception("Image upload failed: ${e.message}", e)
        }
    }
}

package pe.edu.pe.certiweb_android.certifications.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import pe.edu.pe.certiweb_android.config.DioClient
import pe.edu.pe.certiweb_android.environments.Environment

class ReservationService {
    private val client: OkHttpClient = DioClient.okHttpClient
    private val JSON = "application/json; charset=utf-8".toMediaType()

    suspend fun getReservations(): List<Map<String, Any?>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("${Environment.baseUrl}/reservations")
                .get()
                .build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    if (body != null) return@withContext JSONArray(body).toList()
                }
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun createReservation(data: Map<String, Any?>): Map<String, Any?>? = withContext(Dispatchers.IO) {
        try {
            val requestBody = JSONObject(data).toString().toRequestBody(JSON)
            val request = Request.Builder()
                .url("${Environment.baseUrl}/reservations")
                .post(requestBody)
                .build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    if (body != null) return@withContext JSONObject(body).toMap()
                }
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun updateReservationStatus(id: String, status: String): Map<String, Any?>? = withContext(Dispatchers.IO) {
        try {
            val body = JSONObject(mapOf("status" to status)).toString().toRequestBody(JSON)
            val request = Request.Builder()
                .url("${Environment.baseUrl}/reservations/$id")
                .patch(body)
                .build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val resp = response.body?.string()
                    if (resp != null) return@withContext JSONObject(resp).toMap()
                }
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun JSONObject.toMap(): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        val keys = keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = get(key)
            map[key] = if (value is JSONObject) value.toMap() else value
        }
        return map
    }

    private fun JSONArray.toList(): List<Map<String, Any?>> {
        val result = mutableListOf<Map<String, Any?>>()
        for (i in 0 until length()) {
            val obj = getJSONObject(i)
            result.add(obj.toMap())
        }
        return result
    }
}

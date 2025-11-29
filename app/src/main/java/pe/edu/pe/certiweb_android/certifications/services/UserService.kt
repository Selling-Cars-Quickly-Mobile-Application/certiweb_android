package pe.edu.pe.certiweb_android.certifications.services

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import pe.edu.pe.certiweb_android.config.DioClient
import pe.edu.pe.certiweb_android.environments.Environment

class UserService(private val context: Context) {
    private val prefs = context.getSharedPreferences("CertiwebPreferences", Context.MODE_PRIVATE)
    private val client: OkHttpClient = DioClient.okHttpClient

    suspend fun findUserBySession(): Result<Map<String, Any?>> = withContext(Dispatchers.IO) {
        val sessionStr = prefs.getString("currentSession", null)
        val session = sessionStr?.let { JSONObject(it).toMap() } ?: emptyMap()
        val email = session["email"]?.toString()
        val userId = session["userId"]
        if (email != null) {
            try {
                val request = Request.Builder()
                    .url("${Environment.baseUrl}/users?email=$email")
                    .get()
                    .build()

                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        if (responseBody != null) {
                            val list = JSONArray(responseBody).toList()
                            if (list.isNotEmpty()) {
                                return@withContext Result.success(list.first())
                            }
                        }
                    }
                }
            } catch (_: Exception) {}
        }

        val name = session["name"]?.toString()
        if (name != null && userId != null) {
            return@withContext Result.success(mapOf(
                "id" to userId,
                "name" to name,
                "email" to (email ?: "")
            ))
        }

        return@withContext Result.failure(Exception("User not found"))
    }

    fun logout() {
        val e = prefs.edit()
        e.remove("currentSession")
        e.remove("authToken")
        e.remove("currentUser")
        e.remove("adminToken")
        e.remove("currentAdmin")
        e.apply()
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

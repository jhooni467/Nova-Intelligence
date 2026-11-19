package com.jhooni467.novaintelligence

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

internal data class NovaSpaceMessage(
    val role: String,
    val content: String
)

internal object NovaSpaceClient {

    fun normalizeBaseUrl(input: String): String {
        val trimmed = input.trim().trimEnd('/')
        if (trimmed.isEmpty()) {
            throw IllegalArgumentException("Escribe la dirección de tu Hugging Face Space.")
        }

        val uri = try {
            Uri.parse(trimmed)
        } catch (_: Exception) {
            throw IllegalArgumentException("La dirección no es válida.")
        }

        val host = uri.host?.lowercase()
        val validAuthority = uri.authority?.lowercase() == host

        if (
            uri.scheme != "https" ||
            host.isNullOrBlank() ||
            !host.endsWith(".hf.space") ||
            !validAuthority ||
            uri.port != -1 ||
            !uri.encodedQuery.isNullOrBlank() ||
            !uri.encodedFragment.isNullOrBlank() ||
            (!uri.path.isNullOrBlank() && uri.path != "/")
        ) {
            throw IllegalArgumentException(
                "Usa la dirección HTTPS de tu Space, por ejemplo https://usuario-nova-core.hf.space"
            )
        }

        return "https://$host"
    }

    suspend fun sendMessage(
        spaceUrl: String,
        message: String,
        history: List<NovaSpaceMessage>
    ): String = withContext(Dispatchers.IO) {
        val baseUrl = normalizeBaseUrl(spaceUrl)
        val historyJson = JSONArray()

        history.takeLast(10).forEach { turn ->
            if (
                (turn.role == "user" || turn.role == "assistant") &&
                turn.content.isNotBlank()
            ) {
                historyJson.put(
                    JSONObject()
                        .put("role", turn.role)
                        .put("content", turn.content.take(1500))
                )
            }
        }

        val input = JSONObject()
            .put("message", message.take(2000))
            .put("history", historyJson)

        val requestBody = JSONObject()
            .put("data", JSONArray().put(input))
            .toString()

        val submitResponse = request(
            url = "$baseUrl/gradio_api/call/chat",
            method = "POST",
            body = requestBody
        )

        val eventId = JSONObject(submitResponse).optString("event_id")
        if (eventId.isBlank()) {
            throw IOException("Nova Core no devolvió un identificador de respuesta.")
        }

        val eventResponse = request(
            url = "$baseUrl/gradio_api/call/chat/$eventId",
            method = "GET",
            body = null
        )

        val eventLines = eventResponse.lineSequence().toList()
        val finalEvent = eventLines
            .lastOrNull { it.startsWith("event:") }
            ?.substringAfter("event:")
            ?.trim()

        if (finalEvent == "error") {
            throw IOException(
                "Nova Core no pudo generar la respuesta. Puede estar iniciando, en cola o sin cuota disponible."
            )
        }

        val resultData = eventLines
            .filter { it.startsWith("data:") }
            .map { it.substringAfter("data:").trim() }
            .lastOrNull { it.isNotBlank() && it != "null" }
            ?: throw IOException("Nova Core terminó sin devolver una respuesta.")

        val resultArray = try {
            JSONArray(resultData)
        } catch (_: Exception) {
            throw IOException("La respuesta de Nova Core llegó en un formato inesperado.")
        }

        val resultObject = resultArray.optJSONObject(0)
            ?: throw IOException("Nova Core devolvió una respuesta vacía.")

        val answer = resultObject.optString("reply").trim()
        if (answer.isBlank()) {
            throw IOException("El modelo no generó texto. Inténtalo otra vez.")
        }

        answer
    }

    private fun request(
        url: String,
        method: String,
        body: String?
    ): String {
        val connection = URL(url).openConnection() as HttpURLConnection

        try {
            connection.requestMethod = method
            connection.connectTimeout = 15_000
            connection.readTimeout = 180_000
            connection.setRequestProperty("Accept", "text/event-stream, application/json")
            connection.setRequestProperty("User-Agent", "Nova-Intelligence/0.2.0")

            if (body != null) {
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                connection.outputStream.use { output ->
                    output.write(body.toByteArray(Charsets.UTF_8))
                }
            }

            val statusCode = connection.responseCode
            val response = (
                if (statusCode in 200..299) connection.inputStream
                else connection.errorStream
            )?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()

            if (statusCode !in 200..299) {
                val message = when (statusCode) {
                    401, 403 -> "El Space rechazó la solicitud. Comprueba que sea público y que la API esté habilitada."
                    404 -> "No se encontró el endpoint de Nova Core. Revisa que hayas publicado los archivos correctos."
                    429 -> "Se alcanzó el límite gratuito de uso. Prueba de nuevo cuando se restablezca la cuota."
                    502, 503, 504 -> "Nova Core está iniciando o no está disponible. Espera un momento y vuelve a intentarlo."
                    else -> "Nova Core respondió con el error HTTP $statusCode."
                }
                throw IOException(message)
            }

            return response
        } finally {
            connection.disconnect()
        }
    }
}

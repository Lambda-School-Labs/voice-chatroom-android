package com.lambda.voicechatroom.network

import androidx.annotation.WorkerThread
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object NetworkAdapter {
    const val GET = "GET"
    const val POST = "POST"
    const val PUT = "PUT"
    const val DELETE = "DELETE"
    private const val TIMEOUT = 10000  //10 seconds


    @WorkerThread
    fun httpRequest(
        stringUrl: String,
        requestType: String,
        jsonBody: String?,
        headerProperties: Map<String, String>? = null
    ): Pair<Boolean, String> {
        var result = ""
        var success = false
        var stream: InputStream? = null
        var connection: HttpURLConnection? = null

        try {
            val url = URL(stringUrl)
            connection = url.openConnection() as HttpURLConnection
            connection.readTimeout = TIMEOUT
            connection.connectTimeout = TIMEOUT
            connection.requestMethod = requestType

            if (headerProperties != null) {
                for ((key, value) in headerProperties) {
                    connection.setRequestProperty(key, value)
                }
            }

            if (requestType == GET || requestType == DELETE) {
                connection.connect()
            } else if (requestType == POST || requestType == PUT) {
                if (jsonBody != null) {
                    val outputStream = connection.outputStream
                    outputStream.write(jsonBody.toByteArray())
                    outputStream.close()
                }
            }
            val connectionTest = "${connection.responseMessage} ${connection.responseCode}"
            if (connection.responseCode == HttpURLConnection.HTTP_OK || connection.responseCode == HttpURLConnection.HTTP_CREATED) {
                stream = connection.inputStream
                if (stream != null) {
                    val reader = BufferedReader(InputStreamReader(stream))
                    val builder = StringBuilder()
                    var line: String? = reader.readLine()
                    while (line != null) {
                        builder.append(line)
                        line = reader.readLine()
                        success = true
                    }
                    result = builder.toString()
                }
            }

        } catch (e: Throwable) {
            e.printStackTrace()
            result = e.message.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            result = e.message.toString()
        } finally {
            connection?.disconnect()

            if (stream != null) {
                try {
                    stream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return success to result
    }
}

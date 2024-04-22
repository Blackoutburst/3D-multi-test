package dev.blackoutburst.game.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class Connection {

    private var socket: Socket? = null
    private var input: InputStream? = null
    private var output: OutputStream? = null

    fun start() {
        try {
            socket = Socket("127.0.0.1", 15000).let {
                input = it.getInputStream()
                output = it.getOutputStream()
                it
            }
        } catch (ignored: Exception) {
            close()
        }
    }

    fun read() {
        try {
            input?.let {
                val data = it.readNBytes(12)
                println(data)
            }
        } catch (ignored: Exception) {
            close()
        }
    }

    fun write(data: ByteArray) {
        try {
            output?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    it.write(data)
                    it.flush()
                }
            }
        } catch (ignored: Exception) {
            close()
        }
    }

    fun close() {
        try {
            input?.close()
            output?.close()
            socket?.close()
        } catch (ignored: Exception) {}
    }
}
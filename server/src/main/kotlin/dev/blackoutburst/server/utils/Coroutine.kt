package dev.blackoutburst.server.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

inline fun main(crossinline block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        block()
    }
}

inline fun io(crossinline block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        block()
    }
}

inline fun default(crossinline block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Default).launch {
        block()
    }
}

inline fun unconfined(crossinline block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Unconfined).launch {
        block()
    }
}
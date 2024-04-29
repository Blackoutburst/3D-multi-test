package dev.blackoutburst.server.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

inline fun Any.main(crossinline block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        block()
    }
}

inline fun Any.io(crossinline block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        block()
    }
}

inline fun Any.default(crossinline block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Default).launch {
        block()
    }
}

inline fun Any.unconfined(crossinline block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Unconfined).launch {
        block()
    }
}
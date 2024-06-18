package dev.blackoutburst.game.utils

import dev.blackoutburst.game.Main
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

val activeCoroutines = AtomicInteger(0)
val activeCoroutinesIO = AtomicInteger(0)
val activeCoroutinesDefault = AtomicInteger(0)
val activeCoroutinesUnconfined = AtomicInteger(0)

inline fun main(crossinline block: () -> Unit) {
    Main.glTaskQueue.add { block() }
}

inline fun io(crossinline block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.IO).launch {
        try {
            activeCoroutinesIO.incrementAndGet()
            activeCoroutines.incrementAndGet()
            block()
        } finally {
            activeCoroutinesIO.decrementAndGet()
            activeCoroutines.decrementAndGet()
        }
    }
}

inline fun default(crossinline block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.Default).launch {
        try {
            activeCoroutinesDefault.incrementAndGet()
            activeCoroutines.incrementAndGet()
            block()
        } finally {
            activeCoroutinesDefault.decrementAndGet()
            activeCoroutines.decrementAndGet()
        }
    }
}

inline fun unconfined(crossinline block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.Unconfined).launch {
        try {
            activeCoroutinesUnconfined.incrementAndGet()
            activeCoroutines.incrementAndGet()
            block()
        } finally {
            activeCoroutinesUnconfined.decrementAndGet()
            activeCoroutines.decrementAndGet()
        }
    }
}
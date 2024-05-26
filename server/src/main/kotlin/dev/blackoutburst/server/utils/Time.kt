package dev.blackoutburst.server.utils

object Time {
    private const val UPDATE = 1e9f / 20.0
    private var init = System.nanoTime()

    fun doUpdate(): Boolean {
        if (System.nanoTime() - init > UPDATE) {
            init += UPDATE.toLong()
            return (true)
        }
        return (false)
    }

}
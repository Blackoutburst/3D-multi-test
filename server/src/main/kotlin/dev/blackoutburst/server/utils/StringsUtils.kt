package dev.blackoutburst.server.utils

fun String.fit(length: Int): String {
    if (this.length == length) return this

    var newString = this
    if (newString.length > length) newString = newString.substring(0, length)

    for (x in newString.length until length)
        newString += '\u0000'

    return newString
}
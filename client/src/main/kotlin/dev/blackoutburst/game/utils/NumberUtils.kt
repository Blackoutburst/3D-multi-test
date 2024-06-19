package dev.blackoutburst.game.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

val formatter = DecimalFormat("#,###", DecimalFormatSymbols(Locale.getDefault()).apply { groupingSeparator = ' ' })

fun FBN(number: Number): String {
    return formatter.format(number)
}
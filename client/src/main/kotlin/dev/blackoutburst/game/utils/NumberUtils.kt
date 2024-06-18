package dev.blackoutburst.game.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

fun FBN(number: Number): String {
    val customSymbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = ' '
    }

    val formatter = DecimalFormat("#,###", customSymbols)
    return formatter.format(number)
}
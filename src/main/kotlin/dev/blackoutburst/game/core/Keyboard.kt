package dev.blackoutburst.game.core

import org.lwjgl.glfw.GLFW

object Keyboard {
    const val UNKNOWN: Int = -1
    const val SPACE: Int = 32
    const val APOSTROPHE: Int = 39
    const val COMMA: Int = 44
    const val MINUS: Int = 45
    const val PERIOD: Int = 46
    const val SLASH: Int = 47
    const val NUM0: Int = 48
    const val NUM1: Int = 49
    const val NUM2: Int = 50
    const val NUM3: Int = 51
    const val NUM4: Int = 52
    const val NUM5: Int = 53
    const val NUM6: Int = 54
    const val NUM7: Int = 55
    const val NUM8: Int = 56
    const val NUM9: Int = 57
    const val SEMICOLON: Int = 59
    const val EQUAL: Int = 61
    const val A: Int = 65
    const val B: Int = 66
    const val C: Int = 67
    const val D: Int = 68
    const val E: Int = 69
    const val F: Int = 70
    const val G: Int = 71
    const val H: Int = 72
    const val I: Int = 73
    const val J: Int = 74
    const val K: Int = 75
    const val L: Int = 76
    const val M: Int = 77
    const val N: Int = 78
    const val O: Int = 79
    const val P: Int = 80
    const val Q: Int = 81
    const val R: Int = 82
    const val S: Int = 83
    const val T: Int = 84
    const val U: Int = 85
    const val V: Int = 86
    const val W: Int = 87
    const val X: Int = 88
    const val Y: Int = 89
    const val Z: Int = 90
    const val LEFT_BRACKET: Int = 91
    const val BACKSLASH: Int = 92
    const val RIGHT_BRACKET: Int = 93
    const val GRAVE_ACCENT: Int = 96
    const val WORLD_1: Int = 161
    const val WORLD_2: Int = 162

    const val ESCAPE: Int = 256
    const val ENTER: Int = 257
    const val TAB: Int = 258
    const val BACKSPACE: Int = 259
    const val INSERT: Int = 260
    const val DELETE: Int = 261
    const val RIGHT: Int = 262
    const val LEFT: Int = 263
    const val DOWN: Int = 264
    const val UP: Int = 265
    const val PAGE_UP: Int = 266
    const val PAGE_DOWN: Int = 267
    const val HOME: Int = 268
    const val END: Int = 269
    const val CAPS_LOCK: Int = 280
    const val SCROLL_LOCK: Int = 281
    const val NUM_LOCK: Int = 282
    const val PRINT_SCREEN: Int = 283
    const val PAUSE: Int = 284
    const val F1: Int = 290
    const val F2: Int = 291
    const val F3: Int = 292
    const val F4: Int = 293
    const val F5: Int = 294
    const val F6: Int = 295
    const val F7: Int = 296
    const val F8: Int = 297
    const val F9: Int = 298
    const val F10: Int = 299
    const val F11: Int = 300
    const val F12: Int = 301
    const val F13: Int = 302
    const val F14: Int = 303
    const val F15: Int = 304
    const val F16: Int = 305
    const val F17: Int = 306
    const val F18: Int = 307
    const val F19: Int = 308
    const val F20: Int = 309
    const val F21: Int = 310
    const val F22: Int = 311
    const val F23: Int = 312
    const val F24: Int = 313
    const val F25: Int = 314
    const val KP_0: Int = 320
    const val KP_1: Int = 321
    const val KP_2: Int = 322
    const val KP_3: Int = 323
    const val KP_4: Int = 324
    const val KP_5: Int = 325
    const val KP_6: Int = 326
    const val KP_7: Int = 327
    const val KP_8: Int = 328
    const val KP_9: Int = 329
    const val KP_DECIMAL: Int = 330
    const val KP_DIVIDE: Int = 331
    const val KP_MULTIPLY: Int = 332
    const val KP_SUBTRACT: Int = 333
    const val KP_ADD: Int = 334
    const val KP_ENTER: Int = 335
    const val KP_EQUAL: Int = 336
    const val LEFT_SHIFT: Int = 340
    const val LEFT_CONTROL: Int = 341
    const val LEFT_ALT: Int = 342
    const val LEFT_SUPER: Int = 343
    const val RIGHT_SHIFT: Int = 344
    const val RIGHT_CONTROL: Int = 345
    const val RIGHT_ALT: Int = 346
    const val RIGHT_SUPER: Int = 347
    const val MENU: Int = 348
    const val LAST: Int = MENU

    fun isKeyDown(key: Int): Boolean {
        return (GLFW.glfwGetKey(Window.id, key) == GLFW.GLFW_PRESS)
    }
}

package leo8

data class KeyPress(val key: Key)

val Key.press get() = KeyPress(this)

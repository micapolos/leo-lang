package leo7.frp

data class KeyPressed(val key: Key)

val Key.pressed get() = KeyPressed(this)

package leo7.frp

sealed class Key

data class LeftKey(val left: Left) : Key()
data class RightKey(val right: Right) : Key()
data class UpKey(val up: Up) : Key()
data class DownKey(val down: Down) : Key()
data class SpaceKey(val space: Space) : Key()

val Left.key: Key get() = LeftKey(this)
val Right.key: Key get() = RightKey(this)
val Up.key: Key get() = UpKey(this)
val Down.key: Key get() = DownKey(this)
val Space.key: Key get() = SpaceKey(this)

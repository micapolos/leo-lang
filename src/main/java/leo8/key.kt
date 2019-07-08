package leo8

sealed class Key

data class SpaceKey(val space: Space) : Key()
data class LeftKey(val left: Left) : Key()
data class RightKey(val right: Right) : Key()

val Space.key: Key get() = SpaceKey(this)
val Left.key: Key get() = LeftKey(this)
val Right.key: Key get() = RightKey(this)

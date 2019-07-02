package leo7.frp

sealed class Bool

data class BooleanBool(val boolean: Boolean) : Bool()
data class KeyPressedBool(val keyPressed: KeyPressed) : Bool()

val Boolean.bool: Bool get() = BooleanBool(this)
val KeyPressed.bool: Bool get() = KeyPressedBool(this)

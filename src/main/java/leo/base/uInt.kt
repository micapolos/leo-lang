package leo.base

data class UInt internal constructor(
	val signed: Int)

val Int.unsigned: UInt
	get() =
		UInt(this)

operator fun UInt.plus(other: UInt) = signed.plus(other.signed).unsigned
operator fun UInt.minus(other: UInt) = signed.minus(other.signed).unsigned
package leo.base

data class UInt internal constructor(
	val signed: Int)

val Int.unsigned: UInt
	get() =
		UInt(this)
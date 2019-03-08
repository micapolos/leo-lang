package leo32

data class Token internal constructor(
	val nonZeroByte: Byte)

val Byte.tokenOrNull
	get() =
		if (this == 0.toByte()) null else Token(this)

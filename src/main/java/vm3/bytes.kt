package vm3

data class Bytes(val byteArray: ByteArray) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Bytes

		if (!byteArray.contentEquals(other.byteArray)) return false

		return true
	}

	override fun hashCode(): Int {
		return byteArray.contentHashCode()
	}

	inline operator fun get(index: Int) = byteArray[index]
}

val ByteArray.bytes get() = Bytes(this)
fun bytes(vararg bytes: Byte) = bytes.bytes
fun bytes(int: Int, vararg byteInts: Int) =
	ByteArray(byteInts.size + 1) { index -> (if (index == 0) int else byteInts[index - 1]).toByte() }.bytes

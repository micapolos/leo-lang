package leo16.compiler

data class Memory(val byteArray: ByteArray) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Memory

		if (!byteArray.contentEquals(other.byteArray)) return false

		return true
	}

	override fun hashCode(): Int {
		return byteArray.contentHashCode()
	}
}

val ByteArray.memory get() = Memory(this)
val Int.sizeMemory get() = ByteArray(this).memory

val Memory.size get() = byteArray.size
operator fun Memory.set(index: Int, byte: Byte) {
	byteArray[index] = byte
}

operator fun Memory.get(index: Int) = byteArray[index]

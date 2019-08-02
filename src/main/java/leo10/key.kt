package leo10

import leo.binary.Bit
import leo.binary.digitChar
import leo4.wordBitSeq

data class Key(val bitList: List<Bit>) {
	override fun toString() = StringBuilder("key(")
		.let { stringBuilder ->
			stringBuilder.fold(bitList) { bit ->
				append(bit.digitChar)
			}
		}
		.append(")")
		.toString()
}

fun key(bitList: List<Bit>) = Key(bitList)
val String.key get() = wordBitSeq.list.let(::Key)

package vm3.value.asm

import vm3.hexString

sealed class Offset {
	data class Direct(val index: Int) : Offset()
	data class Indirect(val index: Int) : Offset()
}

val Offset.code
	get() =
		when (this) {
			is Offset.Direct -> index.hexString
			is Offset.Indirect -> "[${index.hexString}]"
		}
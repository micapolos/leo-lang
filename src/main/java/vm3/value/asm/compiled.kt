package vm3.value.asm

import leo13.base.linesString
import vm3.type.Type
import vm3.disassemble
import vm3.hexString

data class Compiled(
	val code: ByteArray,
	val dataSize: Int,
	val outputType: Type,
	val outputOffset: Offset
)

val Compiled.disassemble: String
	get() =
		linesString(
			"dataSize: ${dataSize.hexString}",
			"outputOffset: ${outputOffset.code}",
			"--- disassembly ---",
			code.disassemble)
package vm3

import leo13.base.linesString

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
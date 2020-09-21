package vm3.asm

import vm3.Bytes

val Iterable<Op>.bytes: Bytes
	get() =
		fold(emptyCompiler, Compiler::emit).bytes

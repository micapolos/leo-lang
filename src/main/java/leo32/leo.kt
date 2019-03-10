package leo32

import leo.binary.Bit

data class Leo(
	val runtime: Runtime)

val emptyLeo = Leo(emptyRuntime)

fun Leo.push(bit: Bit) =
	copy(runtime = runtime.invoke(bit)!!)
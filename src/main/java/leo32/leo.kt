package leo32

data class Leo(
	val runtime: Runtime)

val emptyLeo = Leo(emptyRuntime)

fun Leo.push(arg: Int) =
	copy(runtime = runtime.push(arg)!!)
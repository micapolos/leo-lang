package leo32.treo

data class Call(
	val fn: Treo,
	val param: Treo)

fun call(fn: Treo, param: Treo) =
	Call(fn, param)

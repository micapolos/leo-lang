package leo32.treo

data class Call(
	val fn: Fn,
	val param: Param)

fun call(fn: Fn, param: Param) =
	Call(fn, param)

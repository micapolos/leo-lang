package leo32.treo

data class Expand(
	val fn: Fn,
	val param: Param)

fun expand(fn: Fn, param: Param) = Expand(fn, param)
package leo32.treo

data class Expand(
	val macro: Macro,
	val param: Param)

fun expand(macro: Macro, param: Param) = Expand(macro, param)
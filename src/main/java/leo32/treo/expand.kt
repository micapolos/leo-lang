package leo32.treo

import leo.base.flatSeq

data class Expand(
	val macro: Macro,
	val param: Param)

fun expand(macro: Macro, param: Param) = Expand(macro, param)

val Expand.charSeq
	get() =
		flatSeq(macro.charSeq, param.charSeq)
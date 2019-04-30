package leo32.treo

import leo.base.flatSeq

data class Call(
	val fn: Fn,
	val param: Param)

fun call(fn: Fn, param: Param) =
	Call(fn, param)

val Call.charSeq
	get() =
		flatSeq(fn.charSeq, param.charSeq)

val Call.invoke
	get() =
		fn.invoke(param.treo)
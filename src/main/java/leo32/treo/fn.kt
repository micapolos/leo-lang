package leo32.treo

import leo.base.flatSeq
import leo.base.seq

data class Fn(val treo: Treo)

fun fn(treo: Treo) = Fn(treo)

val Fn.charSeq get() = flatSeq(seq('.'), treo.trailingCharSeq)

fun Fn.invoke(param: Treo, scope: Scope = voidScope): Treo {
	val result = treo.invoke(param, scope)
	result.rewind()
	return result
}

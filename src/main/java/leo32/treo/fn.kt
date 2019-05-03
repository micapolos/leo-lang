package leo32.treo

import leo.base.flatSeq
import leo.base.seq

data class Fn(val treo: Treo)

fun fn(treo: Treo) = Fn(treo)

val Fn.charSeq get() = flatSeq(seq('.'), treo.trailingCharSeq)

fun Fn.invoke(param: Treo, sink: Sink = voidSink): Treo {
	val result = treo.invoke(param, sink)
	result.rewind()
	return result
}

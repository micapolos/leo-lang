package leo32.treo

import leo.base.Seq
import leo.base.flatSeq
import leo.base.seq

data class Param(val treo: Treo)

fun param(treo: Treo) = Param(treo)

val Param.charSeq: Seq<Char>
	get() =
		flatSeq(seq('('), treo.trailingCharSeq, seq(')'))
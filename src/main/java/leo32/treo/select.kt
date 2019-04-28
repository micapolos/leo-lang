package leo32.treo

import leo.base.Seq
import leo.base.flatSeq
import leo.base.notNullIf
import leo.binary.Bit

data class Select(
	val bit: Bit,
	val treo: Treo)

infix fun Bit.select(treo: Treo) =
	Select(this, treo)

fun Select.at(bit: Bit): Treo? =
	notNullIf(this.bit == bit) { treo }

val Select.charSeq: Seq<Char>
	get() =
		flatSeq(bit.charSeq, treo.trailingCharSeq)
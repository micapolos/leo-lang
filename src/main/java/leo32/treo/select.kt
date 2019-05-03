package leo32.treo

import leo.base.Seq
import leo.base.flatSeq
import leo.base.notNullIf
import leo.binary.Bit

data class Select(
	val value: Value,
	val treo: Treo)

infix fun Value.select(treo: Treo) =
	Select(this, treo)

fun Select.at(bit: Bit): Treo? =
	notNullIf(value.bit == bit) { treo }

fun Select.enter(bit: Bit): Treo? =
	notNullIf(value.enter(bit)) { treo }

val Select.charSeq: Seq<Char>
	get() =
		flatSeq(value.bit.charSeq, treo.trailingCharSeq)
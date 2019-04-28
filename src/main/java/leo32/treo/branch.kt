package leo32.treo

import leo.base.Seq
import leo.base.flatSeq
import leo.base.seq
import leo.binary.Bit
import leo.binary.isZero

data class Branch(
	val at0: Treo,
	val at1: Treo)

fun branch(at0: Treo, at1: Treo) =
	Branch(at0, at1)

fun Branch.at(bit: Bit) =
	if (bit.isZero) at0
	else at1

val Branch.charSeq: Seq<Char>
	get() =
		flatSeq(at0.charSeq, seq('|'), at1.charSeq)
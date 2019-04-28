@file:Suppress("unused")

package leo32.treo

import leo.base.Seq
import leo.base.flatSeq
import leo.base.seq
import leo.binary.Bit
import leo.binary.isZero

data class Branch(
	val at0: At0,
	val at1: At1)

fun branch(at0: At0, at1: At1) =
	Branch(at0, at1)

fun Branch.at(bit: Bit) =
	if (bit.isZero) at0.treo
	else at1.treo

val Branch.charSeq: Seq<Char>
	get() =
		flatSeq(at0.treo.trailingCharSeq, seq('|'), at1.treo.trailingCharSeq)

val Branch.shortCharSeq get() = seq('?')
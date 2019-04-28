@file:Suppress("unused")

package leo32.treo

import leo.base.Seq
import leo.base.fail
import leo.base.flatSeq
import leo.base.seq
import leo.binary.Bit
import leo.binary.bit0
import leo.binary.bit1
import leo.binary.isZero

data class Branch(
	val at0: At0,
	val at1: At1)

fun branch(at0: At0, at1: At1) =
	Branch(at0, at1)

fun Branch.at(bit: Bit) =
	if (bit.isZero) at0.treo
	else at1.treo

fun Branch.bit(treo: Treo): Bit =
	if (at0.treo === treo) bit0
	else if (at1.treo === treo) bit1
	else fail()

val Branch.charSeq: Seq<Char>
	get() =
		flatSeq(at0.treo.trailingCharSeq, seq('|'), at1.treo.trailingCharSeq)

val Branch.shortCharSeq get() = seq('?')
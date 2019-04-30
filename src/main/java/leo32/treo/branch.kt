@file:Suppress("unused")

package leo32.treo

import leo.base.Seq
import leo.base.flatSeq
import leo.base.seq
import leo.binary.Bit
import leo.binary.digitChar
import leo.binary.isZero

data class Branch(
	val enteredVar: Var,
	val at0: At0,
	val at1: At1)

fun branch(variable: Var, at0: At0, at1: At1) =
	Branch(variable, at0, at1)

fun branch(at0: At0, at1: At1) =
	Branch(newVar(), at0, at1)

fun branch(bit: Bit, treo: Treo, inverseTreo: Treo) =
	if (bit.isZero) branch(at0(treo), at1(inverseTreo))
	else branch(at0(inverseTreo), at1(treo))

fun Branch.at(bit: Bit) =
	if (bit.isZero) at0.treo
	else at1.treo

fun Branch.select(bit: Bit): Treo {
	enteredVar.bit = bit
	return entered
}

val Branch.entered
	get() =
		at(enteredVar.bit)

val Branch.charSeq: Seq<Char>
	get() =
		flatSeq(at0.treo.trailingCharSeq, seq('|'), at1.treo.trailingCharSeq)

fun Branch.charSeqFrom(treo: Treo) =
	seq(charFrom(treo))

fun Branch.charFrom(treo: Treo) =
	if (entered.exitTrace === treo) enteredVar.bit.digitChar
	else '?'
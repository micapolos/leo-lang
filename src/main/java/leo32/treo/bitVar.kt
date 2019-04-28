package leo32.treo

import leo.base.Seq
import leo.base.charString
import leo.base.onlySeq
import leo.binary.Bit
import leo.binary.bit0
import leo.binary.digitChar

data class Var(var bit: Bit) {
	override fun toString() = charSeq.charString
}

fun newVar(bit: Bit) =
	Var(bit)

fun newVar() =
	newVar(bit0)

val Var.charSeq: Seq<Char>
	get() =
		bit.digitChar.onlySeq

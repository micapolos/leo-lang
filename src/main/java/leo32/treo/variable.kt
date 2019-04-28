package leo32.treo

import leo.base.Seq
import leo.base.charString
import leo.base.onlySeq
import leo.base.then
import leo.binary.Bit
import leo.binary.bit0
import leo.binary.digitChar

data class Variable(
	var bit: Bit) {
	override fun toString() = charSeq.charString
}

fun variable(bit: Bit) =
	Variable(bit)

fun variable() =
	variable(bit0)

fun Variable.set(bit: Bit) =
	apply { this.bit = bit }

val Variable.charSeq: Seq<Char>
	get() =
		Seq { '_' then bit.digitChar.onlySeq }

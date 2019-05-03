package leo32.treo

import leo.base.Seq
import leo.base.map
import leo.binary.Bit
import leo.binary.digitChar

data class Tail(val chainOrNull: Chain?)

fun tail(chainOrNull: Chain?) = Tail(chainOrNull)

fun tail(vararg values: Value): Tail =
	values.foldRight(tail(null)) { value, tail ->
		tail(chain(head(value), tail))
	}

val Tail.valueSeq: Seq<Value>
	get() =
		Seq { chainOrNull?.valueSeqNode }

val Tail.bitSeq: Seq<Bit>
	get() =
		valueSeq.map { bit }

val Tail.charSeq: Seq<Char>
	get() =
		bitSeq.map { digitChar }

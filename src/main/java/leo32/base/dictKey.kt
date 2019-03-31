package leo32.base

import leo.base.*
import leo.binary.Bit
import leo.binary.zero
import leo32.Seq32
import leo32.seq32

data class DictKey(
	val seq32: Seq32)

val Seq32.dictKey
	get() =
		DictKey(this)

val dictKeyEscapeI32 = '\\'.i32
val dictKeyEscapedZeroI32 = '0'.i32

val I32.dictKeyEscape: Seq32
	get() =
		when (this) {
			zero.i32 -> seq(dictKeyEscapeI32, dictKeyEscapedZeroI32)
			dictKeyEscapeI32 -> seq(dictKeyEscapeI32, dictKeyEscapeI32)
			else -> seq(this)
		}

val DictKey.bitSeq: Seq<Bit>
	get() =
		seq32
			.map { dictKeyEscape }.flat
			.then { zero.i32.seq32 }
			.map { bitSeq }.flat

// === core dict keys

val String.dictKey
	get() =
		seq32.dictKey


package leo16

import leo.base.*
import leo.binary.utf8ByteSeq
import leo13.Stack
import leo13.base.Bit
import leo13.base.bit0
import leo13.base.bit1
import leo13.base.bit2
import leo13.base.bit3
import leo13.base.bit4
import leo13.base.bit5
import leo13.base.bit6
import leo13.base.bit7
import leo13.map
import leo13.stack
import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo15.*
import leo15.bitName
import leo15.byteName

val Bit.expandSentence: Sentence
	get() =
		bitName(if (isOne) oneName() else zeroName())

val Byte.expandSentence: Sentence
	get() =
		byteName(
			bitName(
				stack(bit7, bit6, bit5, bit4, bit3, bit2, bit1, bit0)
					.expandSentence { expandSentence }))

val Int.expandSentence: Sentence
	get() =
		intName(
			byteName(
				stack(byte3, byte2, byte1, byte0)
					.expandSentence { expandSentence }))

fun <T> Stack<T>.expandSentence(fn: T.() -> Sentence): Sentence =
	map(fn).expandSentence

val Stack<Sentence>.expandSentence: Sentence
	get() =
		listName(script)

val String.expandSentence: Sentence
	get() =
		stringName(
			utf8ByteSeq.reverseStack
				.expandSentence { expandSentence })

val Literal.expandSentence: Sentence
	get() =
		when (this) {
			is StringLiteral -> string.expandSentence
			is NumberLiteral -> number.bigDecimal.intValueExact().expandSentence
		}

package leo16

import leo.base.*
import leo.binary.utf8ByteSeq
import leo13.base.Bit
import leo13.base.bit0
import leo13.base.bit1
import leo13.base.bit2
import leo13.base.bit3
import leo13.base.bit4
import leo13.base.bit5
import leo13.base.bit6
import leo13.base.bit7
import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo15.*

val Bit.expandSentence: Sentence
	get() =
		bitName(if (isOne) oneName() else zeroName())

val Byte.expandSentence: Sentence
	get() =
		byteName(bitName(listName(
			bit7.expandSentence,
			bit6.expandSentence,
			bit5.expandSentence,
			bit4.expandSentence,
			bit3.expandSentence,
			bit2.expandSentence,
			bit1.expandSentence,
			bit0.expandSentence)))

val Int.expandSentence: Sentence
	get() =
		intName(byteName(listName(
			byte3.expandSentence,
			byte2.expandSentence,
			byte1.expandSentence,
			byte0.expandSentence)))

val String.expandSentence: Sentence
	get() =
		stringName(byteName(listName(utf8ByteSeq.map { expandSentence }.reverse.reverseStack.script)))

val Literal.expandSentence: Sentence
	get() =
		when (this) {
			is StringLiteral -> string.expandSentence
			is NumberLiteral -> number.bigDecimal.toInt().expandSentence
		}

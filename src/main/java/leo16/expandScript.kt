package leo16

import leo.base.*
import leo.binary.utf8ByteSeq
import leo13.EmptyStack
import leo13.LinkStack
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
import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo15.*

val Bit.expandSentence: Sentence
	get() =
		bitName(if (isOne) oneName() else zeroName())

val Byte.expandSentence: Sentence
	get() =
		byteName(
			firstName(bit7.expandSentence),
			secondName(bit6.expandSentence),
			thirdName(bit5.expandSentence),
			fourthName(bit4.expandSentence),
			fifthName(bit3.expandSentence),
			sixthName(bit2.expandSentence),
			seventhName(bit1.expandSentence),
			eighthName(bit0.expandSentence))

val Int.expandSentence: Sentence
	get() =
		intName(
			firstName(byte3.expandSentence),
			secondName(byte2.expandSentence),
			thirdName(byte1.expandSentence),
			fourthName(byte0.expandSentence))

// TODO: Convert to tailrec
fun <V> Stack<V>.expandSentence(scriptFn: V.() -> Sentence): Sentence =
	listName(
		when (this) {
			is EmptyStack -> nothingName()
			is LinkStack -> linkName(
				previousName(link.stack.expandSentence(scriptFn)),
				lastName(link.value.scriptFn()))
		}
	)

val String.expandSentence: Sentence
	get() =
		stringName(utf8ByteSeq.reverseStack.expandSentence { expandSentence })

val Literal.expandSentence: Sentence
	get() =
		when (this) {
			is StringLiteral -> string.expandSentence
			is NumberLiteral -> number.bigDecimal.toInt().expandSentence
		}

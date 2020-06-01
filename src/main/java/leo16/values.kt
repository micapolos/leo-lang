package leo16

import leo.base.byte0
import leo.base.byte1
import leo.base.byte2
import leo.base.byte3
import leo.base.reverseStack
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
import leo13.map
import leo13.reverse
import leo13.stack
import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo16.names.*
import java.math.BigDecimal

val String.value: Value
	get() =
		_text(nativeValue).onlyValue

val BigDecimal.value: Value
	get() =
		_number(nativeValue).onlyValue

val Boolean.sentence: Sentence
	get() =
		_boolean(if (this) _true() else _false())

val Bit.asField: Sentence
	get() =
		_bit(if (isOne) _one() else _zero())

val Byte.asField: Sentence
	get() =
		_byte(
			_bit(
				stack(bit7, bit6, bit5, bit4, bit3, bit2, bit1, bit0)
					.expandSentence { this@asField.asField }))

val Int.asField: Sentence
	get() =
		_int(
			_byte(
				stack(byte3, byte2, byte1, byte0)
					.expandSentence { this@asField.asField }))

fun <T> Stack<T>.expandSentence(fn: T.() -> Sentence): Sentence =
	map(fn).asField

val Stack<Sentence>.asField: Sentence
	get() =
		_list(value)

val Stack<Value>.field: Sentence
	get() =
		when (this) {
			is EmptyStack -> _list(_empty())
			is LinkStack -> _list(map { _item.invoke(this) }.value)
		}

val String.expandSentence: Sentence
	get() =
		_string(
			utf8ByteSeq.reverseStack
				.expandSentence { asField })

val Literal.asSentence: Sentence
	get() =
		when (this) {
			is StringLiteral -> _text(string.nativeValue)
			is NumberLiteral -> _number(number.bigDecimal.nativeValue)
		}

fun Sentence.plusValue(stack: Stack<Value>): Sentence =
	when (stack) {
		is EmptyStack -> this
		is LinkStack ->
			_list(
				_link(
					_previous(this),
					_last(stack.link.value)))
				.plusValue(stack.link.stack)
	}

val Stack<Value>.valueField: Sentence
	get() =
		_list(_empty()).plusValue(reverse)

val Stack<Value>.valueValue: Value
	get() =
		valueField.onlyValue

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

val Boolean.field: Field
	get() =
		_boolean(if (this) _true() else _false())

val Bit.asField: Field
	get() =
		_bit(if (isOne) _one() else _zero())

val Byte.asField: Field
	get() =
		_byte(
			_bit(
				stack(bit7, bit6, bit5, bit4, bit3, bit2, bit1, bit0)
					.expandField { this@asField.asField }))

val Int.asField: Field
	get() =
		_int(
			_byte(
				stack(byte3, byte2, byte1, byte0)
					.expandField { this@asField.asField }))

fun <T> Stack<T>.expandField(fn: T.() -> Field): Field =
	map(fn).asField

val Stack<Field>.asField: Field
	get() =
		_list(value)

val Stack<Value>.field: Field
	get() =
		when (this) {
			is EmptyStack -> _list(_empty())
			is LinkStack -> _list(map { _item.invoke(this) }.value)
		}

val String.expandSentence: Field
	get() =
		_string(
			utf8ByteSeq.reverseStack
				.expandField { asField })

val Literal.asField: Field
	get() =
		when (this) {
			is StringLiteral -> _text(string.nativeField)
			is NumberLiteral -> _number(number.bigDecimal.nativeField)
		}

fun Field.plusValue(stack: Stack<Value>): Field =
	when (stack) {
		is EmptyStack -> this
		is LinkStack ->
			_list(
				_linked(
					_previous(this),
					_last(stack.link.value)))
				.plusValue(stack.link.stack)
	}

val Stack<Value>.valueField: Field
	get() =
		_list(_empty()).plusValue(reverse)

val Stack<Value>.valueValue: Value
	get() =
		valueField.value

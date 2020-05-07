package leo16

import leo.base.*
import leo.binary.utf8ByteSeq
import leo13.*
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
import leo13.stack
import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo15.bitName
import leo15.byteName
import leo15.emptyName
import leo15.intName
import leo15.itemName
import leo15.listName
import leo15.numberName
import leo15.oneName
import leo15.stringName
import leo15.textName
import leo15.zeroName

val Bit.asField: Field
	get() =
		bitName(if (isOne) oneName() else zeroName())

val Byte.asField: Field
	get() =
		byteName(
			bitName(
				stack(bit7, bit6, bit5, bit4, bit3, bit2, bit1, bit0)
					.expandField { this@asField.asField }))

val Int.asField: Field
	get() =
		intName(
			byteName(
				stack(byte3, byte2, byte1, byte0)
					.expandField { this@asField.asField }))

fun <T> Stack<T>.expandField(fn: T.() -> Field): Field =
	map(fn).asField

val Stack<Field>.asField: Field
	get() =
		listName(value)

val Stack<Value>.field: Field
	get() =
		when (this) {
			is EmptyStack -> listName(emptyName())
			is LinkStack -> listName(map { itemName.invoke(this) }.value)
		}

val String.expandSentence: Field
	get() =
		stringName(
			utf8ByteSeq.reverseStack
				.expandField { asField })

val Literal.asField: Field
	get() =
		when (this) {
			is StringLiteral -> textName(string.nativeField)
			is NumberLiteral -> numberName(number.bigDecimal.nativeField)
		}

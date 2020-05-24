package leo16.compiler

import leo13.Stack
import leo13.map
import leo13.push
import leo13.stack
import leo16.Field
import leo16.invoke
import leo16.names.*
import leo16.value

data class Binary(val byteStack: Stack<Byte>) {
	override fun toString() = asField.toString()
}

val Stack<Byte>.binary get() = Binary(this)
val emptyBinary = Binary(stack())

fun Binary.plus(byte: Byte): Binary =
	byteStack.push(byte).binary

val Binary.asField: Field
	get() =
		_binary.invoke(byteStack.map { asField }.value)
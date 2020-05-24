package leo16.compiler

import leo.base.clampedByte
import leo.base.fold
import leo.base.iterate
import leo13.Stack
import leo13.map
import leo13.push
import leo13.stack
import leo16.Field
import leo16.invoke
import leo16.names.*
import leo16.plus
import leo16.value

data class Compiled(val primitiveStack: Stack<Primitive>, val byteSize: Int) {
	override fun toString() = asField.toString()
}

val emptyCompiled get() = Compiled(stack(), 0)

operator fun Compiled.plus(primitive: Primitive) =
	Compiled(primitiveStack.push(primitive), byteSize + primitive.byteSize)

tailrec operator fun Compiled.plus(alignment: Alignment): Compiled =
	if (byteSize and alignment.mask == 0) this
	else plus(0.clampedByte.primitive).plus(alignment)

val Compiled.asField: Field
	get() =
		_compiled(primitiveStack.map { asField }.value.plus(_byte(_size(byteSize.asField))))

fun compiled(vararg primitives: Primitive) =
	emptyCompiled.fold(primitives) { plus(it) }

fun Compiled.plusZeros(byteSize: Int): Compiled =
	iterate(byteSize) { plus(0.toByte().primitive) }
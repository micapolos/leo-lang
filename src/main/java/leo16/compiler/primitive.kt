package leo16.compiler

import leo16.Field
import leo16.invoke
import leo16.names.*

sealed class Primitive {
	override fun toString() = asField.toString()
}

data class BytePrimitive(val byte: Byte) : Primitive() {
	override fun toString() = super.toString()
}

data class ShortPrimitive(val short: Short) : Primitive() {
	override fun toString() = super.toString()
}

data class IntPrimitive(val int: Int) : Primitive() {
	override fun toString() = super.toString()
}

data class LongPrimitive(val long: Long) : Primitive() {
	override fun toString() = super.toString()
}

data class FloatPrimitive(val float: Float) : Primitive() {
	override fun toString() = super.toString()
}

data class DoublePrimitive(val double: Double) : Primitive() {
	override fun toString() = super.toString()
}

val Byte.primitive: Primitive get() = BytePrimitive(this)
val Short.primitive: Primitive get() = ShortPrimitive(this)
val Int.primitive: Primitive get() = IntPrimitive(this)
val Long.primitive: Primitive get() = LongPrimitive(this)
val Float.primitive: Primitive get() = FloatPrimitive(this)
val Double.primitive: Primitive get() = DoublePrimitive(this)

val Primitive.asField: Field
	get() =
		_primitive(
			when (this) {
				is BytePrimitive -> byte.asField
				is ShortPrimitive -> short.asField
				is IntPrimitive -> int.asField
				is LongPrimitive -> long.asField
				is FloatPrimitive -> float.asField
				is DoublePrimitive -> double.asField
			}
		)

val Primitive.byteSize: Int
	get() =
		when (this) {
			is BytePrimitive -> 1
			is ShortPrimitive -> 2
			is IntPrimitive -> 4
			is LongPrimitive -> 8
			is FloatPrimitive -> 4
			is DoublePrimitive -> 8
		}

val Boolean.asField get() = _boolean(toString()())
val Byte.asField get() = _byte(toString()())
val Short.asField get() = _short(toString()())
val Int.asField get() = _int(toString()())
val Long.asField get() = _long(toString()())
val Float.asField get() = _float(toString()())
val Double.asField get() = _double(toString()())

@file:Suppress("UNCHECKED_CAST")

package leo14.untyped.typed

import leo14.*
import leo14.Number
import java.math.BigDecimal

typealias Value = Any?
typealias Evaluate = () -> Value
typealias Fn = (Value) -> Value

val Literal.nativeValue: Value
	get() =
		when (this) {
			is StringLiteral -> string
			is NumberLiteral -> number.bigDecimal
		}

val Value.valueLiteralOrNull: Literal?
	get() =
		when (this) {
			is String -> literal(this)
			is BigDecimal -> literal(number(this))
			else -> null
		}

val nullValue: Value = null
inline val Value.asPair: Pair<*, *> get() = this as Pair<*, *>
inline val Value.asString get() = this as String
inline val Value.asNumber get() = this as Number
inline val Value.asInt get() = this as Int
inline val Value.asFn get() = this as Fn
inline val Value.asArray get() = this as Array<Value>

tailrec fun Int.plusListLength(value: Value): Int =
	if (value == null) this
	else inc().plusListLength(value.asPair.first)

tailrec fun Value.listReverseFill(array: Array<Value>, index: Int) {
	if (this != null) {
		this as Pair<*, *>
		val decIndex = index.dec()
		array[decIndex] = second
		first.listReverseFill(array, decIndex)
	}
}

val Value.listSize: Int get() = 0.plusListLength(this)

val Value.listAsArray: Array<Value>
	get() {
		val size = listSize
		val array = arrayOfNulls<Value>(size)
		listReverseFill(array, size)
		return array
	}

val Value.nativeString: String
	get() =
		when (this) {
			is Array<*> -> this.contentDeepToString()
			else -> toString()
		}

val Value.nativeScriptLine: ScriptLine
	get() =
		line("#<$nativeString>")

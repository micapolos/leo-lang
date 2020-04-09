@file:Suppress("UNCHECKED_CAST")

package leo14.untyped.typed

import leo14.Literal
import leo14.Number
import leo14.NumberLiteral
import leo14.StringLiteral

typealias Value = Any?
typealias Evaluate = () -> Value
typealias Fn = (Value) -> Value

val Literal.value: Value
	get() =
		when (this) {
			is StringLiteral -> string
			is NumberLiteral -> number
		}

val nullValue: Value = null
inline val Value.asPair: Pair<*, *> get() = this as Pair<*, *>
inline val Value.asString get() = this as String
inline val Value.asNumber get() = this as Number
inline val Value.asInt get() = this as Int
inline val Value.asFn get() = this as Fn

tailrec fun Int.plusValueArrayLength(value: Value): Int =
	if (value == null) this
	else inc().plusValueArrayLength(value.asPair.first)

tailrec fun Value.valueReverseFill(array: Array<Value>, index: Int) {
	if (this != null) {
		this as Pair<*, *>
		val decIndex = index.dec()
		array[decIndex] = second
		first.valueReverseFill(array, decIndex)
	}
}

val Value.arraySize: Int get() = 0.plusValueArrayLength(this)

val Value.asArray: Array<Value>
	get() {
		val size = arraySize
		val array = arrayOfNulls<Value>(size)
		valueReverseFill(array, size)
		return array
	}
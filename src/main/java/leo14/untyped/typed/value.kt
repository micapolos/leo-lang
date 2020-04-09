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

inline val Value.asPair: Pair<*, *> get() = this as Pair<*, *>
inline val Value.asString get() = this as String
inline val Value.asNumber get() = this as Number
inline val Value.asInt get() = this as Int
inline val Value.asFn get() = this as Fn

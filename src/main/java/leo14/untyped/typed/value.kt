package leo14.untyped.typed

import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo14.lambda.runtime.Value

val Literal.value: Value
	get() =
		when (this) {
			is StringLiteral -> string
			is NumberLiteral -> number
		}

val Value.valuePair: Pair<*, *> get() = this as Pair<*, *>

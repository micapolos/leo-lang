package leo13.js.compiler

import leo13.lambda.code.code
import leo13.lambda.js.Value
import leo13.lambda.js.value
import leo13.lambda.pair
import leo13.script.v2.Script

data class Typed(val value: Value, val type: Type)

infix fun Value.of(type: Type) = Typed(this, type)
val emptyTyped = value(code("null")) of emptyType

fun Typed.plus(string: String, rhs: Typed): Typed =
	pair(value, rhs.value) of type.plus(string fieldTo rhs.type)

val Script.typed
	get() =
		let { script ->
			compile {
				write(script)
			}
		}
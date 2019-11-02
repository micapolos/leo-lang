package leo13.js.compiler

import leo13.lambda.code.code
import leo13.lambda.js.Value
import leo13.lambda.js.value
import leo13.lambda.pair

data class Typed(val value: Value, val types: Types)

infix fun Value.of(types: Types) = Typed(this, types)
val nullTyped = value(code("null")) of emptyTypes

fun typed(number: Number) = number.value of types(numberType)
fun typed(string: String) = value(string) of types(stringType)

fun Typed.plus(string: String, rhs: Typed): Typed =
	pair(value, value) of types.plus(type(string fieldTo rhs.types))

val Script.typed
	get() =
		let { script ->
			compile {
				write(script)
			}
		}
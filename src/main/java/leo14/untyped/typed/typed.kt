package leo14.untyped.typed

import leo14.*
import leo14.Number
import leo14.lambda.runtime.asString
import leo14.untyped.*

data class Typed(val type: Thunk, val value: Any?)

fun typed(type: Thunk, value: Any?) = Typed(type, value)

val Any?.asNumber get() = this as Number
val Any?.asCompiled get() = this as Compiled

val emptyTyped = typed(emptyThunk, null)
val String.typed get() = typed(thunk(value(textName)), this)
val Number.typed get() = typed(thunk(value(numberName)), this)

val Typed.script: Script
	get() =
		null
			?: type.matchEmpty {
				script()
			}
			?: type.match(numberName) {
				script(literal(value.asNumber))
			}
			?: type.match(textName) {
				script(literal(value.asString))
			}
			?: type.match(compiledName) {
				value.asCompiled.type.script
			}
			?: error("unknown type")


val Literal.typed
	get() =
		when (this) {
			is StringLiteral -> typed(thunk(value(textName))) { string }
			is NumberLiteral -> typed(thunk(value(numberName))) { number }
		}

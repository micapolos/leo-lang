package leo14.untyped.typed

import leo14.*
import leo14.Number
import leo14.lambda.runtime.asString
import leo14.lambda.runtime.pair
import leo14.lambda.runtime.valueInvoke
import leo14.untyped.*

data class Typed(val type: Type, val value: Any?)

fun typed(type: Type, value: Any?) = Typed(type, value)

val Any?.asNumber get() = this as Number
val Any?.asCompiled get() = this as Compiled

val emptyTyped = typed(emptyType, null)
val String.typed get() = typed(textType, this)
val Number.typed get() = typed(numberType, this)
val Compiled.typed get() = typed(compiledType, this)

val Literal.typed
	get() =
		when (this) {
			is StringLiteral -> string.typed
			is NumberLiteral -> number.typed
		}

val Typed.script: Script
	get() =
		null
			?: type.thunk.matchEmpty {
				script()
			}
			?: type.thunk.match(numberName) {
				script(literal(value.asNumber))
			}
			?: type.thunk.match(textName) {
				script(literal(value.asString))
			}
			?: type.thunk.match(compiledName) {
				value.asCompiled.type.script
			}
			?: error("unknown type")


fun Typed.plus(begin: Begin, rhs: Typed) =
	typed(
		type(type.thunk.plus(begin.string lineTo rhs.type.thunk)),
		if (rhs.value == null) value
		else if (value == null) rhs.value
		else pair.valueInvoke(value).valueInvoke(rhs))

fun Typed.plus(literal: Literal) =
	typed(
		type(type.thunk.plus(thunk(value(literal.selectName)))),
		if (value == null) literal.typed
		else pair.valueInvoke(value).valueInvoke(literal.typed))

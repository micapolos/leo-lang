package leo14.untyped.typed

import leo14.*
import leo14.Number
import leo14.invoke
import leo14.lambda.runtime.Value
import leo14.untyped.Thunk
import leo14.untyped.script
import java.lang.reflect.Constructor
import java.lang.reflect.Method

val nullValue: Value = null

val Value.valueScript: Script
	get() =
		when (this) {
			is String -> leo(this)
			is Boolean -> leo("boolean"(nativeScript))
			is Int -> leo("int"(nativeScript))
			is Number -> leo(line(literal(this)))
			is Method -> leo("method"(nativeScript))
			is Constructor<*> -> leo("constructor"(nativeScript))
			is Class<*> -> leo("class"(nativeScript))
			is Field -> leo(name(rhs.valueScript))
			is Thunk -> script
			else -> nativeScript
		}

val Value.nativeScript: Script
	get() =
		leo("native"(toString()))

val Literal.value: Value
	get() =
		when (this) {
			is StringLiteral -> string
			is NumberLiteral -> number
		}
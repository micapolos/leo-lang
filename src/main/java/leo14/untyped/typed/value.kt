package leo14.untyped.typed

import leo14.*
import leo14.Number
import leo14.lambda.runtime.Value
import leo14.untyped.Thunk
import leo14.untyped.script
import java.lang.reflect.Constructor
import java.lang.reflect.Method

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
			is Thunk -> script
			else -> nativeScript
		}

val Value.nativeScript: Script
	get() =
		leo("native"(toString()))
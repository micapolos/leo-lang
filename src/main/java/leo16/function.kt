package leo16

import leo15.givingName

data class Function(val scope: Scope, val script: Script)

infix fun Scope.function(script: Script) = Function(this, script)

operator fun Function.invoke(value: Value): Value =
	scope.plus(value.givenBinding).evaluate(script)!!

val Function.struct: Struct
	get() =
		script(givingName.invoke(script)).struct

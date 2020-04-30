package leo16

data class Function(val scope: Scope, val script: Script)

infix fun Scope.function(script: Script) = Function(this, script)

operator fun Function.invoke(value: Value): Value =
	scope.plus(value.givenBinding).evaluate(script)
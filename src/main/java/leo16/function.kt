package leo16

data class Function(val scope: Scope, val script: Script)

operator fun Function.invoke(value: Value): Value =
	TODO()
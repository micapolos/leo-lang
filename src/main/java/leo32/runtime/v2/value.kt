package leo32.runtime.v2

data class Value(
	val scope: Scope,
	val scriptOrNull: Script?)

val Scope.emptyValue
	get() =
		Value(this, null)


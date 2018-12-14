package leo.base

data class Variable<V>(
	var value: V)

fun <V> newVariable(value: V): Variable<V> =
	Variable(value)
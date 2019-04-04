package leo32.runtime

data class Call(
	val function: Function)

fun call(function: Function) =
	Call(function)

fun Call.invoke(term: Term) =
	function.invoke(parameter(term))
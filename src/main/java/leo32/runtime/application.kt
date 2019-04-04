package leo32.runtime

data class Application(
	val lhs: Function,
	val op: Op)

fun Function.application(op: Op) =
	Application(this, op)

fun Application.invoke(parameter: Parameter) =
	op.invoke(lhs.invoke(parameter), parameter)
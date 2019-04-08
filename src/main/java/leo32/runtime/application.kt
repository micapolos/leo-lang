package leo32.runtime

data class Application(
	val lhs: Template,
	val op: Op)

fun Template.application(op: Op) =
	Application(this, op)

fun Application.invoke(parameter: Parameter) =
	op.invoke(lhs.invoke(parameter), parameter)
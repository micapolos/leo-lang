@file:Suppress("unused")

package leo32.runtime

object Argument
val argument = Argument

fun Argument.invoke(parameter: Parameter): Term =
	parameter.term
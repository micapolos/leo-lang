package leo.term

import leo.base.string

data class Type<in V>(
	val isSimpleFn: V.() -> Boolean)

fun <V : Any> Type<V>.string(application: Application<V>): String = ""
	.plus(application.word)
	.plus(if (application.argumentOrNull == null) "" else argumentString(application.argumentOrNull))

fun <V : Any> Type<V>.argumentString(argument: V): String = ""
	.plus(if (argument.isSimpleFn()) " " else "(")
	.plus(argument)
	.plus(if (argument.isSimpleFn()) "" else ")")

fun <V : Any> Type<V>.string(term: Term<V>): String = ""
	.plus(if (term.receiverOrNull == null) "" else term.receiverOrNull.string.plus(", "))
	.plus(string(term.application))
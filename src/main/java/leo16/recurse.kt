package leo16

import leo16.names.*

data class Recurse(val gives: Gives) {
	override fun toString() = asField.toString()
}

val Recurse.asField get() = _recursing(gives.asValue)
val Gives.recurse get() = Recurse(this)

fun Recurse.apply(arg: Value): Value =
	gives.pattern
		.gives(gives.function.dictionary
			//.plus(_recurse(_any()).value.pattern.definitionTo(gives.function.body))
			.function(gives.function.bodyValue))
		.apply(arg)
		?: _recurse(arg).value

package leo16

import leo16.names.*

data class Recurse(val function: Function) {
	override fun toString() = asField.toString()
}

val Recurse.asField get() = _recursing(function.asValue)
val Function.recurse get() = Recurse(this)

fun Recurse.apply(arg: Value): Value =
	function.pattern
		.gives(function.compiled.dictionary
			//.plus(_recurse(_any()).value.pattern.definitionTo(gives.function.body))
			.compiled(function.compiled.bodyValue))
		.apply(arg)
		?: _recurse(arg).value

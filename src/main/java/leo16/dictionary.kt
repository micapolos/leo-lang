package leo16

import leo13.Stack
import leo13.fold
import leo13.mapFirst
import leo13.push
import leo13.reverse
import leo13.stack
import leo16.names.*

data class Dictionary(val definitionStack: Stack<Definition>) {
	override fun toString() = asField.toString()
}

val Stack<Definition>.dictionary get() = Dictionary(this)
val emptyDictionary = stack<Definition>().dictionary

operator fun Dictionary.plus(definition: Definition): Dictionary =
	definitionStack.push(definition).dictionary

operator fun Dictionary.plus(dictionary: Dictionary): Dictionary =
	fold(dictionary.definitionStack.reverse) { plus(it) }

fun Dictionary.apply(value: Value): Value? =
	definitionStack.mapFirst { apply(value) }

fun Dictionary.resolve(value: Value): Value =
	definitionStack.mapFirst { apply(value) } ?: value

fun Dictionary.resolve(evaluated: Evaluated): Evaluated =
	definitionStack.mapFirst { apply(evaluated) } ?: evaluated

fun Dictionary.compile(value: Value): Evaluated? =
	emptyScope.emptyEvaluator.plus(value).evaluated

fun Dictionary.evaluate(value: Value): Value? =
	compile(value)?.value

val Dictionary.asField: Field
	get() =
		_dictionary(_definition(definitionStack.expandField { asField }))

package leo16

import leo13.*
import leo15.dictionaryName
import leo15.libraryName

data class Dictionary(val definitionStack: Stack<Definition>) {
	override fun toString() = asField.toString()
}

val Stack<Definition>.dictionary get() = Dictionary(this)
val emptyDictionary = stack<Definition>().dictionary
operator fun Dictionary.plus(definition: Definition): Dictionary = definitionStack.push(definition).dictionary

fun Dictionary.apply(value: Value): Value? =
	definitionStack.mapFirst { apply(value) }

fun Dictionary.resolve(value: Value): Value =
	definitionStack.mapFirst { apply(value) } ?: value

fun Dictionary.compile(value: Value): Evaluated? =
	emptyScope.evaluator.plus(value).compiled

fun Dictionary.evaluate(value: Value): Value? =
	compile(value)?.value

val Dictionary.asField: Field
	get() =
		dictionaryName(definitionName(definitionStack.expandSentence { asField }))

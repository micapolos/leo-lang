package leo16

import leo13.*
import leo15.libraryName

data class Library(val definitionStack: Stack<Definition>) {
	override fun toString() = asField.toString()
}

val Stack<Definition>.library get() = Library(this)
val emptyLibrary = stack<Definition>().library
operator fun Library.plus(definition: Definition): Library = definitionStack.push(definition).library

fun Library.apply(value: Value): Value? =
	definitionStack.mapFirst { apply(value) }

fun Library.resolve(value: Value): Value =
	definitionStack.mapFirst { apply(value) } ?: value

fun Library.compile(value: Value): Evaluated? =
	emptyScope.evaluator.plus(value).compiled

fun Library.evaluate(value: Value): Value? =
	compile(value)?.value

val Library.asField: Field
	get() =
		libraryName(definitionName(definitionStack.expandSentence { asField }))

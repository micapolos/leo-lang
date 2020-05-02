package leo16

import leo13.*
import leo15.bindingName
import leo15.libraryName

data class Library(val bindingStack: Stack<Binding>) {
	override fun toString() = asField.toString()
}

val Stack<Binding>.library get() = Library(this)
val emptyLibrary = stack<Binding>().library
operator fun Library.plus(binding: Binding): Library = bindingStack.push(binding).library

fun Library.apply(value: Value): Value? =
	bindingStack.mapFirst { apply(value) }

fun Library.resolve(value: Value): Value =
	bindingStack.mapFirst { apply(value) } ?: value

fun Library.compile(value: Value): Compiled? =
	emptyScope.compiler.plus(value).compiled

fun Library.evaluate(value: Value): Value? =
	compile(value)?.value

val Library.asField: Field
	get() =
		libraryName(bindingName(bindingStack.expandSentence { asField }))

package leo16

import leo13.*
import leo15.bindingName
import leo15.libraryName

data class Library(val bindingStack: Stack<Binding>) {
	override fun toString() = asSentence.toString()
}

val Stack<Binding>.library get() = Library(this)
val emptyLibrary = stack<Binding>().library
operator fun Library.plus(binding: Binding): Library = bindingStack.push(binding).library

fun Library.apply(value: Value): Value? =
	bindingStack.mapFirst { apply(value) }

fun Library.resolve(value: Value): Value =
	bindingStack.mapFirst { apply(value) } ?: value

fun Library.compile(script: Script): Compiled? =
	emptyScope.compiler.plus(script).compiled

fun Library.evaluate(script: Script): Value? =
	compile(script)?.value

val Library.asSentence: Sentence
	get() =
		libraryName(bindingName(bindingStack.expandSentence { asSentence }))

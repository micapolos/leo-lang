package leo16

import leo13.*
import leo15.libraryName
import leo15.scopeName

data class Scope(val bindingStack: Stack<Binding>) {
	override fun toString() = asSentence.toString()
}

val Stack<Binding>.scope get() = Scope(this)
val emptyScope = stack<Binding>().scope
operator fun Scope.plus(binding: Binding): Scope = bindingStack.push(binding).scope

fun Scope.apply(value: Value): Value? =
	bindingStack.mapFirst { apply(value) }

fun Scope.compile(script: Script): Compiled? =
	emptyLibrary.compiler.plus(script).compiled

fun Scope.evaluate(script: Script): Value? =
	compile(script)?.value

val Scope.asSentence: Sentence
	get() =
		scopeName.invoke(bindingStack.map { asSentence }.script)

val Scope.librarySentence: Sentence
	get() =
		libraryName(bindingStack.map { pattern.asSentence }.script)


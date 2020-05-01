package leo16

import leo15.functionName

data class Function(val library: Library, val script: Script) {
	override fun toString() = asSentence.toString()
}

infix fun Library.function(script: Script) = Function(this, script)

operator fun Function.invoke(value: Value): Value =
	library.plus(value.givenBinding).evaluate(script)!!

val Function.asSentence: Sentence
	get() =
		functionName(library.asSentence, script.asSentence)

val Function.valueSentence: Sentence
	get() =
		functionName(script)

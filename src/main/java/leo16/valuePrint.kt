package leo16

import leo13.Stack
import leo13.map
import leo15.givingName
import leo15.libraryName
import leo15.listName
import leo15.patternName

val Value.print: Value
	get() =
		fieldStack.map { print }.value

val Field.print: Field
	get() =
		printValueSentence.field

val Field.printValueSentence: Sentence
	get() =
		when (this) {
			is SentenceField -> sentence.print
			is FunctionField -> function.printSentence
			is LibraryField -> library.printSentence
		}

val Sentence.print: Sentence
	get() =
		word(value.print)

val Library.printSentence: Sentence
	get() =
		libraryName(patternName(bindingStack.printField { printSentence.field }))

val Binding.printSentence: Sentence
	get() =
		patternName(pattern.value)

val Function.printSentence: Sentence
	get() =
		givingName(bodyValue.print)

fun <T> Stack<T>.printField(fn: T.() -> Field): Sentence =
	map(fn).printSentence

val Stack<Field>.printSentence: Sentence
	get() =
		listName(value)

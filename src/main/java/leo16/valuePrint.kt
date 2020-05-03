package leo16

import leo13.Stack
import leo13.map
import leo15.*

val Value.print: Value
	get() =
		fieldStack.map { print }.value

val Field.print: Field
	get() =
		when (this) {
			is SentenceField -> sentence.printSentence.field
			is FunctionField -> function.printSentence.field
			is DictionaryField -> dictionary.printSentence.field
			is NativeField -> this
		}

val Sentence.printSentence: Sentence
	get() =
		word.sentenceTo(value.print)

val Dictionary.printSentence: Sentence
	get() =
		dictionaryName.sentenceTo(patternName(definitionStack.printField { printField }))

val Definition.printField: Field
	get() =
		patternName(pattern.value)

val Function.printSentence: Sentence
	get() =
		givingName.sentenceTo(bodyValue.print)

fun <T> Stack<T>.printField(fn: T.() -> Field): Field =
	map(fn).printField

val Stack<Field>.printField: Field
	get() =
		listName(value)

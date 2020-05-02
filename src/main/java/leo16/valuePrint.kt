package leo16

import leo13.Stack
import leo13.map
import leo15.exportName
import leo15.givingName
import leo15.libraryName
import leo15.listName

val Value.print: Value
	get() =
		fieldStack.map { print }.value

val Field.print: Field
	get() =
		printValueSentence.field

val Field.printValueSentence: ValueSentence
	get() =
		when (this) {
			is SentenceField -> sentence.print
			is FunctionField -> function.printValueSentence
			is LibraryField -> library.printValueSentence
		}

val ValueSentence.print: ValueSentence
	get() =
		word(value.print)

val Library.printValueSentence: ValueSentence
	get() =
		libraryName(exportName(bindingStack.printField { printValueSentence.field }))

val Binding.printValueSentence: ValueSentence
	get() =
		exportName(pattern.value)

val Function.printValueSentence: ValueSentence
	get() =
		givingName(script.value.print)

fun <T> Stack<T>.printField(fn: T.() -> Field): ValueSentence =
	map(fn).printSentence

val Stack<Field>.printSentence: ValueSentence
	get() =
		listName(value)

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
		when (this) {
			is SentenceField -> sentence.printField
			is FunctionField -> function.printField
			is LibraryField -> library.printField
		}

val Field.printValueSentence: ValueSentence
	get() =
		// TODO: To it the other way around
		print.sentenceOrNull!!

val ValueSentence.printField: Field
	get() =
		word(value.print)

val Library.printField: Field
	get() =
		libraryName(exportName(bindingStack.printField { printField }))

val Binding.printField: Field
	get() =
		exportName(pattern.value)

val Function.printField: Field
	get() =
		givingName(script.value.print)

fun <T> Stack<T>.printField(fn: T.() -> Field): Field =
	map(fn).printField

val Stack<Field>.printField: Field
	get() =
		listName(value)

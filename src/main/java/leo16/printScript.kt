package leo16

import leo13.map
import leo15.exportName
import leo15.functionName
import leo15.givingName
import leo15.libraryName

val Value.printScript: Script
	get() =
		fieldStack.map { printSentence }.script

val Field.printSentence: Sentence
	get() =
		when (this) {
			is SentenceField -> sentence.printSentence
			is FunctionField -> function.printSentence
			is LibraryField -> library.printSentence
		}

val ValueSentence.printSentence: Sentence
	get() =
		word(value.printScript)

val Library.printSentence: Sentence
	get() =
		libraryName(bindingStack.map { printSentence }.script)

val Binding.printSentence: Sentence
	get() =
		exportName(pattern.asScript)

val Function.printSentence: Sentence
	get() =
		givingName(script)
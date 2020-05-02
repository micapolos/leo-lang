package leo16

import leo13.map

val Value.printScript: Script
	get() =
		fieldStack.map { printSentence }.script

val Field.printSentence: Sentence
	get() =
		printValueSentence.printSentence

val ValueSentence.printSentence: Sentence
	get() =
		word(value.printScript)

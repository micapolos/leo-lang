package leo16

import leo13.map

val Script.value: Value
	get() =
		sentenceStack.map { field }.value

val Sentence.field: Field
	get() =
		word(script.value).field
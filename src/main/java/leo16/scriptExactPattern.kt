package leo16

import leo13.map

val Script.exactPattern: Pattern
	get() =
		exactPatternStruct.pattern

val Script.exactPatternStruct: PatternStruct
	get() =
		sentenceStack.map { exactPatternLine }.struct

val Sentence.exactPatternLine: PatternLine
	get() =
		word.invoke(script.exactPattern)
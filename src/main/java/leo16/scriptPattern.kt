package leo16

import leo.base.notNullIf
import leo13.map
import leo15.anyName

val Script.pattern: Pattern
	get() =
		null
			?: anyPatternOrNull
			?: exactPattern

val Script.anyPatternOrNull: Pattern?
	get() =
		notNullIf(this == script(anyName())) {
			anyPattern
		}

val Script.exactPattern: Pattern
	get() =
		patternStruct.pattern

val Script.patternStruct: PatternStruct
	get() =
		sentenceStack.map { patternLine }.struct

val Sentence.patternLine: PatternLine
	get() =
		word.invoke(script.pattern)
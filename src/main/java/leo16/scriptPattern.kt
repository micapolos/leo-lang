package leo16

import leo.base.notNullIf
import leo13.map
import leo15.anyName

val Script.pattern: Pattern
	get() =
		null
			?: anyPatternOrNull
			?: valuePattern

val Script.anyPatternOrNull: Pattern?
	get() =
		notNullIf(this == script(anyName())) {
			anyPattern
		}

val Script.valuePattern: Pattern
	get() =
		sentenceStack.map { patternField }.value.pattern

val Sentence.patternField: PatternField
	get() =
		// TODO: Function and library
		word.invoke(script.pattern)

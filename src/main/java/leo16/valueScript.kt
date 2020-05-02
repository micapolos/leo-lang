package leo16

import leo13.array
import leo13.map
import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.script
import leo14.untyped.scriptLine

val Value.script: Script
	get() =
		script(*fieldStack.map { scriptLine }.array)

val Field.scriptLine: ScriptLine
	get() =
		when (this) {
			is SentenceField -> sentence.scriptLine
			is FunctionField -> function.printSentence.scriptLine
			is LibraryField -> library.printSentence.scriptLine
			is LiteralField -> literal.scriptLine
		}

val Sentence.scriptLine: ScriptLine
	get() =
		word lineTo value.script
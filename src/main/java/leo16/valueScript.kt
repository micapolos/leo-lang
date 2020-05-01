package leo16

import leo13.map

val Value.script: Script
	get() =
		when (this) {
			is StructValue -> struct.script
			is FunctionValue -> function.struct.script
			is ScopeValue -> script(library.librarySentence)
		}

val Struct.script: Script
	get() =
		lineStack.map { sentence }.script

val Line.sentence: Sentence
	get() =
		word(value.script)

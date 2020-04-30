package leo16

import leo13.map
import leo15.functionName

val Value.script: Script
	get() =
		when (this) {
			is StructValue -> struct.script
			is FunctionValue -> function.script
		}

val Struct.script: Script
	get() =
		lineStack.map { sentence }.script

val Line.sentence: Sentence
	get() =
		word(value.script)

val Function.script: Script
	get() =
		script(functionName(script))
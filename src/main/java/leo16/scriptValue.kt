package leo16

import leo13.map

val Script.value: Value
	get() =
		struct.value

val Script.struct: Struct
	get() =
		sentenceStack.map { line }.struct

val Sentence.line: Line
	get() =
		word.invoke(script.value)
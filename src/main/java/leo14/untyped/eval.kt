package leo14.untyped

import leo13.fold
import leo13.reverse
import leo14.Script
import leo14.tokenStack

val Script.eval: Script
	get() =
		evalProgram.script

val Script.evalProgram: Program
	get() =
		emptyReader
			.fold(tokenStack.reverse) { write(it)!! }
			.run { this as UnquotedReader }
			.unquoted
			.resolver
			.program

val Program.eval: Program
	get() =
		script.evalProgram

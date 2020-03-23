package leo14.untyped

import leo13.fold
import leo13.reverse
import leo14.Script
import leo14.tokenStack

val Script.eval: Script
	get() =
		evalThunk.script

val Script.evalThunk: Thunk
	get() =
		emptyReader
			.fold(tokenStack.reverse) { write(it)!! }
			.run { this as UnquotedReader }
			.unquoted
			.resolver
			.thunk

val Script.evalProgram: Program
	get() =
		evalThunk.program

val Program.eval: Program
	get() =
		script.evalProgram

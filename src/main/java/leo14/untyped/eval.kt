package leo14.untyped

import leo13.fold
import leo13.reverse
import leo14.Script
import leo14.tokenStack

val Script.eval
	get() =
		emptyTokenizer
			.fold(tokenStack.reverse) { write(it)!! }
			.evaluator
			.program
			.script

val Program.eval
	get() =
		emptyTokenizer
			.fold(script.tokenStack.reverse) { write(it)!! }
			.evaluator
			.program

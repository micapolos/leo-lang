package leo14.untyped

import leo13.fold
import leo13.reverse
import leo14.Script
import leo14.tokenStack

val Script.eval: Script
	get() =
		evalResolver.printScript

val Script.evalThunk: Thunk
	get() =
		evalResolver.thunk

val Script.evalResolver: Resolver
	get() =
		emptyReader
			.fold(tokenStack.reverse) { write(it)!! }
			.run { this as UnquotedReader }
			.unquoted
			.resolver

val Script.evalValue: Value
	get() =
		evalThunk.value

val Value.eval: Value
	get() =
		script.evalValue

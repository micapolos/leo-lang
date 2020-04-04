package leo14.untyped.typed

import leo13.fold
import leo13.reverse
import leo14.Script
import leo14.tokenStack

val Script.eval: Script
	get() =
		emptyLeo.fold(tokenStack.reverse) { write(it) }.resolver.typed.script

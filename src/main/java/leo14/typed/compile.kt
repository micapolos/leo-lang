package leo14.typed

import leo13.stack
import leo14.Script
import leo14.any
import leo14.compile
import leo14.ret

val Script.anyCompile: Typed<Any>
	get() =
		typedCompiler(stack(), { it.any }, ret()).compile(this)


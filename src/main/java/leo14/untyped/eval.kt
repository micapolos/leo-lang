package leo14.untyped

import leo14.Script

val Script.eval
	get() =
		context().resolver().tokenReader().append(this).resolver.program.script

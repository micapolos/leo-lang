package leo14.untyped

import leo14.Script

val Script.eval
	get() =
		context().resolver().tokenizer().append(this).resolver.program.script

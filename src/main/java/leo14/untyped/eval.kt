package leo14.untyped

import leo14.Script

val Script.eval
	get() =
		context().liner().tokenizer().append(this).liner.script

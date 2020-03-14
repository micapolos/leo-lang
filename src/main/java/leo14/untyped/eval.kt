package leo14.untyped

import leo14.Script

val Script.eval
	get() =
		context().interpreter().tokenizer().plus(this).interpreter.script
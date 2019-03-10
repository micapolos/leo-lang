package leo32

import leo.base.appendableString

val Leo.code
	get() =
		appendableString { it.appendCode(this) }

val Function.code
	get() =
		appendableString { it.appendCode(this) }
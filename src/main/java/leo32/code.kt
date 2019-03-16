package leo32

import leo.base.appendableString

val <T> Scope<T>.code
	get() =
		appendableString { it.appendCode(this) }

val <T> Function<T>.code
	get() =
		appendableString { it.appendCode(this) }
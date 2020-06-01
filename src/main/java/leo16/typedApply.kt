package leo16

val Typed.resolve: Typed
	get() =
		apply ?: this

val Typed.apply: Typed?
	get() =
		null
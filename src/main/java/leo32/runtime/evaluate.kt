package leo32.runtime

val Script.evaluate get() =
	term().plus(this).script
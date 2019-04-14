package leo32.runtime

val Script.evaluate get() =
	term().invoke(this).script

val Line.evaluate get() =
	term().invoke(this).script
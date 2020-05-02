package leo16

val Script.eval: Script
	get() =
		emptyCompiler.plus(this).compiled.value.printScript
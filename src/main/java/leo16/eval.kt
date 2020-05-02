package leo16

val Script.eval: Script
	get() =
		emptyCompiler.plus(value).compiled.value.printScript
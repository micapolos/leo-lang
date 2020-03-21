package leo14.untyped

import leo13.recurseName

data class Pattern(val program: Program)

fun pattern(program: Program) = Pattern(program)

fun Pattern.matches(program: Program) =
	this.program.matches(program)

val recursePattern =
	pattern(program(recurseName))

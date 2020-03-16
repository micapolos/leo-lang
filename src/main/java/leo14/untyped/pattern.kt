package leo14.untyped

import leo13.givenName
import leo13.thisName

data class Pattern(val program: Program)

fun pattern(program: Program) = Pattern(program)

fun Pattern.matches(program: Program) =
	this.program.matches(program)

val givenPattern =
	pattern(program(givenName))

val thisPattern =
	pattern(program(thisName))

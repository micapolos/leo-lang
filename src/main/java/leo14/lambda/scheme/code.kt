package leo14.lambda.scheme

import leo13.int
import leo14.lambda.Variable
import leo14.lambda.code.Gen

val Int.varCode: Code
	get() =
		if (this < 0) error("$this.varCode")
		else code("v$this")

fun paramCode(gen: Gen): Code =
	gen.depth.varCode

fun Variable<Code>.index(gen: Gen) =
	gen.depth - index.int - 1

package leo14.js.compiler

import leo14.Script
import leo14.lambda.js.show
import leo14.literal
import leo14.script

val Script.show
	get() =
		typed.term.show

fun main() = script(literal("Hello, world!")).show

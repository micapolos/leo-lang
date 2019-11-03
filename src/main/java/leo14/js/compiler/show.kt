package leo13.js.compiler

import leo14.Script
import leo14.lambda.js.show
import leo14.line
import leo14.script

val Script.show
	get() =
		typed.value.show

fun main() = script(line("Hello, world!")).show

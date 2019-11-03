package leo13.js.compiler

import leo13.lambda.js.show
import leo13.script.v2.Script
import leo13.script.v2.line
import leo13.script.v2.script

val Script.show
	get() =
		typed.value.show

fun main() = script(line("Hello, world!")).show

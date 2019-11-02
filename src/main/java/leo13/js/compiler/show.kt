package leo13.js.compiler

import leo13.lambda.js.show

val Script.show
	get() =
		typed.value.show

fun main() = script(line("Hello, world!")).show

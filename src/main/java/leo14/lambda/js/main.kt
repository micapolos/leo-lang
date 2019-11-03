package leo14.lambda.js

import leo14.dsl.*
import leo14.lambda.first
import leo14.lambda.pair
import leo14.lambda.second
import leo14.script

val helloWorld get() = value("Hello, world!")
val pair = pair(value("first"), value("second"))
val pairFirst get() = pair.first
val pairSecond get() = pair.second

val helloWorldScript = script(native("Hello, world!"))
val pairFirstScript = script(
	function(function(argument(previous))),
	apply(native("first")),
	apply(native("second")))


fun main() = helloWorldScript.show

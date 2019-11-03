package leo13.lambda.js

import leo13.lambda.first
import leo13.lambda.pair
import leo13.lambda.second
import leo13.script.v2.*

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
